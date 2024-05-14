package com.prosvirnin.webregistration.model.user.dto;

import com.prosvirnin.webregistration.core.Mapper;
import com.prosvirnin.webregistration.model.user.Address;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class EditAddressDTO implements Mapper<Address> {

    private String country;
    private String city;
    private String street;
    private String house;
    private String office;

    @Override
    public Address map() {
        return Address.builder()
                .country(country)
                .city(city)
                .street(street)
                .house(house)
                .office(office)
                .build();
    }
}
