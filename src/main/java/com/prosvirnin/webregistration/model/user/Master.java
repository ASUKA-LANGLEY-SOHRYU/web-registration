package com.prosvirnin.webregistration.model.user;

import com.prosvirnin.webregistration.model.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Master {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "description")
    private String description;

    @Column(name = "link_code", nullable = false)
    private String linkCode;

    @Column(name = "messenger")
    private String messenger;

    @ManyToMany
    @JoinTable(
            name = "master_client",
            joinColumns = @JoinColumn(name = "master_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private List<Client> clients;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "master")
    private Set<Image> additionalImages;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
