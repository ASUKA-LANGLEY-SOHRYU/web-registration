package com.prosvirnin.webregistration.repository;

import com.prosvirnin.webregistration.model.service.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query("from Record where master.id = :id")
    List<Record> findAllByMasterId(Long id);

    List<Record> findAllByDate(LocalDate date);
}
