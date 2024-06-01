package com.prosvirnin.webregistration.model.service.dto;

import com.prosvirnin.webregistration.model.user.User;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

@Data
@Builder
public class ClientResponse {
    private String fullName;
    private Integer age;
    private String phone;

    public static ClientResponse fromClient(User user){
        var client = user.getClient();
        if (user.getClient() == null)
            return null;
        return ClientResponse.builder()
                .phone(user.getPhone())
                .fullName("%s %s".formatted(user.getFirstName(), user.getLastName()))
                .age(Period.between(LocalDate.now(), client.getBirthDate()).getYears())
                .build();
    }
}
