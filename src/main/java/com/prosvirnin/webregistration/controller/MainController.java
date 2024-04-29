package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping
    public String hello(){
        return "Hello";
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hi(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok("hrllo " + user.getEmail());
    }
}
