package com.prosvirnin.webregistration.model.user.dto;

import com.prosvirnin.webregistration.model.user.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String emailToChange;
    private String firstName;
    private String lastName;
    private String phone;
    private List<Role> roles;
    private MasterProfile master;
    private ClientProfile client;
}
