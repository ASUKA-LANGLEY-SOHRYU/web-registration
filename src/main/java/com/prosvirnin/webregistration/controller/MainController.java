package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(
        name = "Test controller",
        description = "Just some test endpoints"
)
public class MainController {

    @Operation(
            description = "Just returns string \"Hello\""

    )
    @GetMapping
    public String hello(){
        return "Hello";
    }

    @Operation(
            description = "Returns string \"hello <Email>\", where is the <Email> is email of authenticated user"
    )
    @GetMapping("/hello")
    public ResponseEntity<String> hi(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok("hello " + user.getEmail());
    }
}
