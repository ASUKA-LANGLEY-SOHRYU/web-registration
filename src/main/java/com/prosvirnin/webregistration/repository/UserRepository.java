package com.prosvirnin.webregistration.repository;

import com.prosvirnin.webregistration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
