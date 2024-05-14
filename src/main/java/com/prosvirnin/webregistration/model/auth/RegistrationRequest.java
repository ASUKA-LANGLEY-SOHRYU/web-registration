package com.prosvirnin.webregistration.model.auth;

import com.prosvirnin.webregistration.core.Mapper;
import com.prosvirnin.webregistration.model.user.Client;
import com.prosvirnin.webregistration.model.user.Master;
import com.prosvirnin.webregistration.model.user.Role;
import com.prosvirnin.webregistration.model.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class RegistrationRequest implements Mapper<User> {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date birthDate;

    @Override
    public User map() {
        var user = User.builder()
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .phone(phoneNumber)
                .build();
        if (birthDate != null) {
            Client client = new Client();
            client.setBirthDate(birthDate);
            client.setUser(user);
            user.setClient(client);
            user.setRoles(Set.of(Role.USER, Role.CLIENT));
        } else {
            Master master = new Master();
            master.setUser(user);
            user.setMaster(master);
            user.setRoles(Set.of(Role.USER, Role.MASTER));
        }
        return user;
    }
}
