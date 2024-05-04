package com.prosvirnin.webregistration.repository;

import com.prosvirnin.webregistration.model.user.ActivationCode;
import org.springframework.data.repository.CrudRepository;

public interface ActivationCodeRepository extends CrudRepository<ActivationCode, Long> {
}
