package com.prosvirnin.webregistration.model.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditResponse {
    private String message;
    private EditStatus status;
}
