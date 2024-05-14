package com.prosvirnin.webregistration.model.auth;

import com.prosvirnin.webregistration.core.Mapper;
import com.prosvirnin.webregistration.model.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest{
    private String email;
    private String password;
}
