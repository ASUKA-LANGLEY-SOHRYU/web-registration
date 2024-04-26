package com.prosvirnin.webregistration.service.auth;

import com.prosvirnin.webregistration.exception.auth.EmailAlreadyExistsException;
import com.prosvirnin.webregistration.model.Role;
import com.prosvirnin.webregistration.model.User;
import com.prosvirnin.webregistration.model.account.ActivationResponse;
import com.prosvirnin.webregistration.model.auth.AuthenticationRequest;
import com.prosvirnin.webregistration.model.auth.AuthenticationResponse;
import com.prosvirnin.webregistration.repository.UserRepository;
import com.prosvirnin.webregistration.service.MailSender;
import com.prosvirnin.webregistration.service.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    private final MailSender mailSender;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.mailSender = mailSender;
    }

    public AuthenticationResponse register(AuthenticationRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(String.format(
                    "Email %s already exist.", request.getEmail()));
        }
        var role = Role.USER;
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(role))
                .activationCode(getNewActivationCode())
                .build();
        userRepository.save(user);

        mailSender.send(user.getEmail(), "Activation code", String.format(
                "Ваш код активаци: %s", user.getActivationCode()
        ));

        return getAuthenticationResponse(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return getAuthenticationResponse(user);
    }

    public ActivationResponse activate(String code, Authentication authentication){
        var user = (User) authentication.getPrincipal();
        if (user.getActivationCode().equals(code)) {
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
        return Integer.toString(ThreadLocalRandom.current().nextInt(1000,9999));
    }
}
