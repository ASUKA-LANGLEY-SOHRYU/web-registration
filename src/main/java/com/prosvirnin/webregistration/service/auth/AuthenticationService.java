package com.prosvirnin.webregistration.service.auth;

import com.prosvirnin.webregistration.exception.auth.EmailAlreadyExistsException;
import com.prosvirnin.webregistration.model.auth.RegistrationRequest;
import com.prosvirnin.webregistration.model.user.ActivationCode;
import com.prosvirnin.webregistration.model.user.Role;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.model.account.ActivationResponse;
import com.prosvirnin.webregistration.model.auth.AuthenticationRequest;
import com.prosvirnin.webregistration.model.auth.AuthenticationResponse;
import com.prosvirnin.webregistration.repository.ActivationCodeRepository;
import com.prosvirnin.webregistration.repository.ClientRepository;
import com.prosvirnin.webregistration.repository.MasterRepository;
import com.prosvirnin.webregistration.repository.UserRepository;
import com.prosvirnin.webregistration.service.MailSender;
import com.prosvirnin.webregistration.service.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final ActivationCodeRepository activationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailSender mailSender;

    private final ClientRepository clientRepository;

    private final MasterRepository masterRepository;

    @Value("${sendActivationCode}")
    private Boolean sendActivationCode;

    @Autowired
    public AuthenticationService(UserRepository userRepository, ActivationCodeRepository activationCodeRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager, MailSender mailSender, ClientRepository clientRepository, MasterRepository masterRepository) {
        this.userRepository = userRepository;
        this.activationCodeRepository = activationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.mailSender = mailSender;
        this.clientRepository = clientRepository;
        this.masterRepository = masterRepository;
    }
    @Transactional
    public AuthenticationResponse register(RegistrationRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(String.format(
                    "Email %s already exist.", request.getEmail()));
        }

        var user = request.map();
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (sendActivationCode)
            sendActivationCode(user);
        else
            userRepository.save(user);
        if (user.getRoles().contains(Role.CLIENT))
            clientRepository.save(user.getClient());
        if (user.getRoles().contains(Role.MASTER))
            masterRepository.save(user.getMaster());

        return getAuthenticationResponse(user);
    }

    private void sendActivationCode(User user) {
        var activationCode = ActivationCode.builder()
                .code(getNewActivationCode())
                .build();

        user.setActivationCode(activationCode);
        activationCode.setUser(user);

        mailSender.send(
                user.getEmail(),
                "Activation code",
                String.format("Ваш код активаци: %s", user.getActivationCode().getCode())
        );
        userRepository.save(user);
        activationCodeRepository.save(activationCode);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return getAuthenticationResponse(user);
    }

    public ActivationResponse activate(String code, Authentication authentication){
        var user = (User) authentication.getPrincipal();
        if (user.getActivationCode().getCode().equals(code)) {
            user.setActivationCode(null);
            return ActivationResponse.ok();
        }
        return ActivationResponse.wrongCode();
    }

    private AuthenticationResponse getAuthenticationResponse(User user){
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private String getNewActivationCode(){
        return Integer.toString(ThreadLocalRandom
            .current()
            .nextInt(1000,9999)
        );
    }
}
