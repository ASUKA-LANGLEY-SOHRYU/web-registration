package com.prosvirnin.webregistration.model.auth;

import lombok.Data;

@Data
public class AuthenticationRequest {

    String email;
    String password;
}
