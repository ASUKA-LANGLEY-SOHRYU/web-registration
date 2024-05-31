package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.model.service.dto.ScheduleRequest;
import com.prosvirnin.webregistration.model.service.dto.ScheduleResponse;
import com.prosvirnin.webregistration.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MasterService masterService;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, MasterService masterService) {
        this.scheduleRepository = scheduleRepository;
        this.masterService = masterService;
    }

    @Transactional
    public boolean save(Authentication authentication, ScheduleRequest request){
        var master = masterService.getAuthenticatedMaster(authentication);
        scheduleRepository.saveAll(request.toSchedules(master));
        return true;
    }

    public List<ScheduleResponse> findAllByAuthenticatedMaster(Authentication authentication){
        return findAllByMasterId(masterService.getAuthenticatedMaster(authentication).getId());
    }

    public List<ScheduleResponse> findAllByMasterId(Long masterId){
        var schedules = scheduleRepository.findAllByMasterId(masterId);
        return ScheduleResponse.fromSchedules(schedules);
    }

    @Transactional
    public boolean edit(ScheduleRequest request, Authentication authentication){
        var master = masterService.getAuthenticatedMaster(authentication);
        scheduleRepository.deleteAllByDatesAndMasterId(request.getDates(), master.getId());
        save(authentication, request);
        return true;
    }

    @Transactional
    public boolean delete(List<LocalDate> dates, Authentication authentication){
        var master = masterService.getAuthenticatedMaster(authentication);
        scheduleRepository.deleteAllByDatesAndMasterId(dates, master.getId());
        return true;
    }
}
