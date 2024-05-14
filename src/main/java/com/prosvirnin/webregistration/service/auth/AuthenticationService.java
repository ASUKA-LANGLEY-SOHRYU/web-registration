package com.prosvirnin.webregistration.service.auth;

import com.prosvirnin.webregistration.exception.auth.EmailAlreadyExistsException;
import com.prosvirnin.webregistration.model.user.ActivationCode;
import com.prosvirnin.webregistration.model.user.Role;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.model.account.ActivationResponse;
import com.prosvirnin.webregistration.model.auth.AuthenticationRequest;
import com.prosvirnin.webregistration.model.auth.AuthenticationResponse;
import com.prosvirnin.webregistration.repository.ActivationCodeRepository;
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

    @Value("${sendActivationCode}")
    private Boolean sendActivationCode;

    @Autowired
    public AuthenticationService(UserRepository userRepository, ActivationCodeRepository activationCodeRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager, MailSender mailSender) {
        this.userRepository = userRepository;
        this.activationCodeRepository = activationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.mailSender = mailSender;
    }
    @Transactional
    public AuthenticationResponse register(AuthenticationRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(String.format(
                    "Email %s already exist.", request.getEmail()));
        }


        var role = Role.USER;
        var user = request.map();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(role));

        var activationCode = ActivationCode.builder()
                .code(getNewActivationCode())
                .build();

        user.setActivationCode(activationCode);
        activationCode.setUser(user);

        userRepository.save(user);
        activationCodeRepository.save(activationCode);

        if (sendActivationCode) {
            mailSender.send(
                    user.getEmail(),
                    "Activation code",
                    String.format("Ваш код активаци: %s", user.getActivationCode().getCode())
            );
        }

        return getAuthenticationResponse(user);
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
        if (sendActivationCode)
            return Integer.toString(ThreadLocalRandom
                .current()
                .nextInt(1000,9999)
            );
        return null;
    }
}
