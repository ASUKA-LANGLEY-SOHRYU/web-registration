package com.prosvirnin.webregistration.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    private String name;

    public Role(Long id){
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return getName();
    }

}
