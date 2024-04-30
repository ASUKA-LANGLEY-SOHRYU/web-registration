package com.prosvirnin.webregistration.controller.auth;

import com.prosvirnin.webregistration.exception.auth.EmailAlreadyExistsException;
import com.prosvirnin.webregistration.model.auth.AuthenticationRequest;
import com.prosvirnin.webregistration.model.auth.AuthenticationResponse;
import com.prosvirnin.webregistration.service.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@CrossOrigin
@Tag(
        name = "Authentication"
)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(
            description = "A method for registering a user by mail and password. After sending this request, " +
                    "an email will be sent to the specified email address with the confirmation " +
                    "code that is needed to activate the account."
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (
            @RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(
            description = "A method for sending a new JWT token."
    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (
            @RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<AuthenticationResponse> handleException(EmailAlreadyExistsException e){
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .error(e.getMessage())
                .build());
    }
}
