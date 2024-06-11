package com.prosvirnin.webregistration.service.auth;

import com.prosvirnin.webregistration.exception.auth.EmailAlreadyExistsException;
import com.prosvirnin.webregistration.model.auth.AuthenticationRequest;
import com.prosvirnin.webregistration.model.auth.AuthenticationResponse;
import com.prosvirnin.webregistration.model.auth.RegistrationRequest;
import com.prosvirnin.webregistration.model.user.Role;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.repository.ClientRepository;
import com.prosvirnin.webregistration.repository.MasterRepository;
import com.prosvirnin.webregistration.repository.UserRepository;
import com.prosvirnin.webregistration.service.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ClientRepository clientRepository;
    private final MasterRepository masterRepository;
    private final AccountActivationService accountActivationService;


    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager, ClientRepository clientRepository, MasterRepository masterRepository, AccountActivationService accountActivationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.clientRepository = clientRepository;
        this.masterRepository = masterRepository;
        this.accountActivationService = accountActivationService;
    }

    @Transactional
    public AuthenticationResponse register(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()) ||
                userRepository.existsByEmailToChange(request.getEmail())) {
            throw new EmailAlreadyExistsException(String.format(
                    "Email %s already exist.", request.getEmail()));
        }

        var user = request.map();
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        accountActivationService.sendActivationCode(user);
        if (!accountActivationService.getSendActivationCode())
            userRepository.save(user);

        if (user.getRoles().contains(Role.CLIENT))
            clientRepository.save(user.getClient());
        if (user.getRoles().contains(Role.MASTER))
            masterRepository.save(user.getMaster());

        return getAuthenticationResponse(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return getAuthenticationResponse(user);
    }

    private AuthenticationResponse getAuthenticationResponse(User user) {
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

}
