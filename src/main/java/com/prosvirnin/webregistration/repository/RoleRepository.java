package com.prosvirnin.webregistration.repository;

import com.prosvirnin.webregistration.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
