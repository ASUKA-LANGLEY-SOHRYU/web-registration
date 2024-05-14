package com.prosvirnin.webregistration.model.auth;

import com.prosvirnin.webregistration.core.Mapper;
import com.prosvirnin.webregistration.model.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest implements Mapper<User> {
    private String email;
    private String firstName;
    private String password;

    @Override
    public User map() {
        return User.builder()
                .email(email)
                .firstName(firstName)
                .password(password)
                .build();
    }
}
