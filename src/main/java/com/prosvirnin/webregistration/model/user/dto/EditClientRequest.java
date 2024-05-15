package com.prosvirnin.webregistration.model.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class EditClientRequest implements EditUserRequest{
    private EditUserDTO user;
    private Date birthDate;
}
