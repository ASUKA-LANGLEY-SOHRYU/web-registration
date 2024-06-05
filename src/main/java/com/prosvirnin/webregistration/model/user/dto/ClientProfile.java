package com.prosvirnin.webregistration.model.user.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosvirnin.webregistration.model.user.Client;
import com.prosvirnin.webregistration.model.user.Master;
import com.prosvirnin.webregistration.model.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ClientProfile {
    private Long id;
    private List<Long> mastersIds;
    private LocalDate birthDate;

    public static ClientProfile fromClient(Client client){
        return ClientProfile.builder()
                .id(client.getId())
                .birthDate(client.getBirthDate())
                .mastersIds(client.getMasters().stream().map(Master::getId).toList())
                .build();
    }
}
