package com.prosvirnin.webregistration.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "schedule")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Schedule {

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
    @JoinColumn(name = "master_id", nullable = false)
    private Master master;
}
