package com.prosvirnin.webregistration.model.service;

import com.prosvirnin.webregistration.model.Image;
import com.prosvirnin.webregistration.model.service.category.Category;
import com.prosvirnin.webregistration.model.user.Master;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "master_id", nullable = false)
    private Master master;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Long price;

    @Temporal(TemporalType.TIME)
    @Column(name = "duration", nullable = false)
    private Date duration;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private ServiceStatus serviceStatus;
}
