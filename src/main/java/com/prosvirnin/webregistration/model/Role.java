package com.prosvirnin.webregistration.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;


public enum Role {
    USER,
    MASTER,
    CLIENT,
    ADMIN
}
