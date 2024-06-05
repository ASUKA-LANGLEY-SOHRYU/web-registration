package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.model.service.Record;
import com.prosvirnin.webregistration.model.service.RecordStatus;
import com.prosvirnin.webregistration.model.service.dto.RecordRequest;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.model.service.dto.RecordResponse;
import com.prosvirnin.webregistration.repository.RecordRepository;
import com.prosvirnin.webregistration.repository.ScheduleRepository;
import com.prosvirnin.webregistration.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RecordService {
    private final RecordRepository recordRepository;
    private final ClientService clientService;
    private final MasterService masterService;
    private final ScheduleRepository scheduleRepository;
    private final ServiceRepository serviceRepository;
    private final MailSender mailSender;

    @Autowired
    public RecordService(RecordRepository recordRepository, ClientService clientService, MasterService masterService, ScheduleRepository scheduleRepository, ServiceRepository serviceRepository, MailSender mailSender) {
        this.recordRepository = recordRepository;
        this.clientService = clientService;
        this.masterService = masterService;
        this.scheduleRepository = scheduleRepository;
        this.serviceRepository = serviceRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    public Long create(Authentication authentication, Long masterId, RecordRequest request){
        var client = clientService.getAuthenticatedClient(authentication);
        var master = masterService.findById(masterId);
        var schedules = scheduleRepository.findAllByMasterIdAndDate(masterId, request.getDate());
        var service = serviceRepository.findById(request.getServiceId()).orElseThrow();
        boolean isInRange = false;
        for (var schedule : schedules){
            var timeFrom = schedule.getTimeFrom();
            var timeTo = schedule.getTimeTo();
            if (isInTimeRange(timeFrom, timeTo, request.getTimeFrom(), service.getDuration())){
                isInRange = true;
                break;
            }
        }
        if (!isInRange)
            return null;

        client.getMasters().add(master);
        master.getClients().add(client);
        masterService.save(master);
        clientService.save(client);

        mailSender.send(master.getUser().getEmail(), "Запись",
                String.format("Запись на %s %s.\nУслуга: %s.\nКлиент: %s %s\nКомментарий: %s",
                        request.getDate(),
                        request.getTimeFrom(),
                        service.getName(),
                        client.getUser().getFirstName(),
                        client.getUser().getLastName(),
                        request.getComment()
                )
        );

        return recordRepository.save(Record.builder()
                        .date(request.getDate())
                        .client(client)
                        .master(master)
                        .comment(request.getComment())
                        .timeFrom(request.getTimeFrom())
                        .timeTo(request.getTimeFrom().plus(service.getDuration()))
                        .service(service)
                        .recordStatus(RecordStatus.CREATED)
                .build()).getId();
    }

    private boolean isInTimeRange(LocalTime start, LocalTime end, LocalTime target){
        return !target.isBefore(start) && !target.isAfter(end);
    }

    private boolean isInTimeRange(LocalTime start, LocalTime end, LocalTime from, Duration duration){
        return isInTimeRange(start, end, from) && isInTimeRange(start, end, from.plus(duration));
    }

    public List<RecordResponse> findAllRecordsByAuthentication(Authentication authentication){
        var masterId = masterService.getAuthenticatedMaster(authentication).getId();
        return recordRepository.findAllByMasterId(masterId).stream()
                .map(r -> RecordResponse.fromRecord(r, false))
                .toList();
    }

    public List<RecordResponse> findAllRecordsByMasterId(Long masterId){
        return recordRepository.findAllByMasterId(masterId).stream()
                .map(r -> RecordResponse.fromRecord(r, true))
                .toList();
    }

    @Transactional
    public String cancel(Authentication authentication, Long recordId){
        var user = (User) authentication.getPrincipal();
        var record = recordRepository.findById(recordId).orElseThrow();
        if (((user.getClient() != null) && !user.getClient().getId().equals(record.getClient().getId())) ||
            ((user.getMaster() != null) && !user.getMaster().getId().equals(record.getMaster().getId())))
                return "ERROR!";
        record.setRecordStatus(RecordStatus.CANCELLED);
        var master = record.getMaster();
        mailSender.send(master.getUser().getEmail(), "Запись",
                String.format("Запись отменена на %s %s.\nУслуга: %s.\nКлиент: %s %s",
                        record.getDate(),
                        record.getTimeFrom(),
                        record.getService().getName(),
                        record.getClient().getUser().getFirstName(),
                        record.getClient().getUser().getLastName()
                )
        ); //TODO: Добавить уведомления
        recordRepository.save(record);
        return "OK!";
    }

    @Transactional
    public String cancelAllByDate(Authentication authentication, LocalDate date){
        var master = masterService.getAuthenticatedMaster(authentication);
        var records = recordRepository.findAllByDate(date);
        records.forEach(r -> r.setRecordStatus(RecordStatus.CANCELLED));
        recordRepository.saveAll(records);
        return "OK!";
    }
}
