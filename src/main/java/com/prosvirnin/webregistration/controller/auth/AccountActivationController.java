package com.prosvirnin.webregistration.controller.auth;

import com.prosvirnin.webregistration.model.account.ActivationRequest;
import com.prosvirnin.webregistration.model.account.ActivationResponse;
import com.prosvirnin.webregistration.service.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@Tag(
        name = "Account activation"
)
public class AccountActivationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AccountActivationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(
            description = "A method for activating the account. " +
                    "The code should be sent to your email after registration."
    )
    @PostMapping("/activate")
    public ResponseEntity<ActivationResponse> activate(@RequestBody ActivationRequest request,
                                                       Authentication authentication){
        return ResponseEntity.ok(authenticationService.activate(request.getCode(), authentication));
    }
}
