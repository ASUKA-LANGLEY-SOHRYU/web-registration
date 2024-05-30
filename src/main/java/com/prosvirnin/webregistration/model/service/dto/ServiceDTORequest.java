package com.prosvirnin.webregistration.model.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ServiceDTORequest {
    private String name;

    private String description;

    private Long price;

    private Duration duration;

    @JsonIgnore
    public java.time.Duration getDBDuration(){
        return java.time.Duration.ofHours(duration.getHours())
                .plus(java.time.Duration.ofMinutes(duration.getMinutes()));
    }
}
