package com.prosvirnin.webregistration.model.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest {
    String email;
    String password;
}
