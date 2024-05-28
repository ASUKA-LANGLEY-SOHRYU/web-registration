package com.prosvirnin.webregistration.repository;

import com.prosvirnin.webregistration.model.service.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("from Category where master.id = :id")
    List<Category> findByMasterId(@Param("id") Long id);
}
