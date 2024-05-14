package com.prosvirnin.webregistration.controller.auth;

import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(
        name = "User controller"
)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            description = "Getting a user by token"
    )
    @GetMapping("/me")
    public ResponseEntity<User> me (Authentication authentication)
    {
        return ResponseEntity.ok(userService.getAuthenticatedUser(authentication));
    }

}
