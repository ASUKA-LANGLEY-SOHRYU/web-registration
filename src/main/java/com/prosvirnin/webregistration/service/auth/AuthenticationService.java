package com.prosvirnin.webregistration.service.auth;

import com.prosvirnin.webregistration.exception.auth.EmailAlreadyExistsException;
import com.prosvirnin.webregistration.model.Role;
import com.prosvirnin.webregistration.model.User;
import com.prosvirnin.webregistration.model.auth.AuthenticationRequest;
import com.prosvirnin.webregistration.model.auth.AuthenticationResponse;
import com.prosvirnin.webregistration.repository.RoleRepository;
import com.prosvirnin.webregistration.repository.UserRepository;
import com.prosvirnin.webregistration.service.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(AuthenticationRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }
        var role = new Role("ROLE_USER");
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(role))
                .build();
        roleRepository.save(role);
        userRepository.save(user);
        return getAuthenticationResponse(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return getAuthenticationResponse(user);
    }

    private AuthenticationResponse getAuthenticationResponse(User user){
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
