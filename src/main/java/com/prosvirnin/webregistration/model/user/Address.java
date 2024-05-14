package com.prosvirnin.webregistration.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosvirnin.webregistration.model.user.dto.EditAddressDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @Column(name = "master_id")
    private Long masterId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    @JsonIgnore
    private Master master;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "house")
    private String house;

    @Column(name = "office")
    private String office;

    public void setFields(Address address){
        if (address.getCountry() != null)
            country = address.getCountry();
        if (address.getCity() != null)
            city = address.getCity();
        if (address.getStreet() != null)
            street = address.getStreet();
        if (address.getHouse() != null)
            house = address.getHouse();
        if (address.getOffice() != null)
            office = address.getOffice();
    }
}
