package com.prosvirnin.webregistration.model.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class EditClientRequest {
    private EditUserDTO editUserDTO;
    private Date birthDate;
}
