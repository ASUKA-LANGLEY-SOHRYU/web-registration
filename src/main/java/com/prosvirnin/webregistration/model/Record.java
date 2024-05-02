package com.prosvirnin.webregistration.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_from", nullable = false)
    private Date timeFrom;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_to", nullable = false)
    private Date timeTo;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    //TODO: добавить поле
}
