package com.prosvirnin.webregistration.controller.auth;

import com.prosvirnin.webregistration.model.auth.AuthenticationRequest;
import com.prosvirnin.webregistration.model.auth.AuthenticationResponse;
import com.prosvirnin.webregistration.service.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (
            @RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (
            @RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
