package com.prosvirnin.webregistration.model;

import com.prosvirnin.webregistration.model.user.Master;
import jakarta.persistence.*;

import java.sql.Blob;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "file")
    private Blob file;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;

// файл уже содержит информацию о типе.
//    @Column(name = "type")
//    private String type;
}
