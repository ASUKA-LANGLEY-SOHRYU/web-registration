package com.prosvirnin.webregistration.model.service;

import com.prosvirnin.webregistration.model.user.Client;
import com.prosvirnin.webregistration.model.user.Master;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Temporal(TemporalType.TIME)
    @Column(name = "time_from", nullable = false)
    private LocalTime timeFrom;

    @Temporal(TemporalType.TIME)
    @Column(name = "time_to", nullable = false)
    private LocalTime timeTo;

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

    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatus;
}
