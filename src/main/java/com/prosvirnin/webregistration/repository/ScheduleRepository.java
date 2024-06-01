package com.prosvirnin.webregistration.repository;

import com.prosvirnin.webregistration.model.service.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("from Schedule where master.id = :id")
    List<Schedule> findAllByMasterId(Long id);

    @Query("from Schedule s where s.master.id = :id and s.date = :date")
    List<Schedule> findAllByMasterIdAndDate(Long id, LocalDate date);

    @Modifying
    @Transactional
    @Query("delete from Schedule where date in (:dates) and master.id = :id")
    void deleteAllByDatesAndMasterId(List<LocalDate> dates, Long id);
}
