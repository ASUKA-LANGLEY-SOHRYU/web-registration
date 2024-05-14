package com.prosvirnin.webregistration.model.user.dto;

import lombok.Data;

@Data
public class EditUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
