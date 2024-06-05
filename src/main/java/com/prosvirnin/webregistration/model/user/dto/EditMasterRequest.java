package com.prosvirnin.webregistration.model.user.dto;

import com.prosvirnin.webregistration.model.user.Address;
import lombok.Builder;
import lombok.Data;

@Data
public class EditMasterRequest implements EditUserRequest{
    private EditUserDTO user;
    private String description;
    private EditAddressDTO address;
    private String linkCode;
    private String messenger;
}
