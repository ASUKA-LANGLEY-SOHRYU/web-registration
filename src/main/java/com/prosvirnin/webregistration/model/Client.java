package com.prosvirnin.webregistration.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "birthdate", nullable = false)
    private Date birthDate;
}
