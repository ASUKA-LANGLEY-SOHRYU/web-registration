package com.prosvirnin.webregistration.repository;

import com.prosvirnin.webregistration.model.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    @Query("from Service where master.id = :id")
    List<Service> findByMasterId(@Param("id") Long id);

    @Query("from Service where category.id = :id")
    List<Service> findByCategoryId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("delete from Service where category.id = :id")
    void deleteByCategoryId(Long id);
}
