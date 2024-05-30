package com.prosvirnin.webregistration.model.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosvirnin.webregistration.model.user.Master;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Master master;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
}
