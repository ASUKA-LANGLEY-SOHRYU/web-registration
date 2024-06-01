package com.prosvirnin.webregistration.model.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class EditClientRequest implements EditUserRequest{
    private EditUserDTO user;
    private LocalDate birthDate;
}
