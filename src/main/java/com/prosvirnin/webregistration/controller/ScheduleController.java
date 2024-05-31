package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.model.service.dto.ScheduleRequest;
import com.prosvirnin.webregistration.model.service.dto.ScheduleResponse;
import com.prosvirnin.webregistration.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/masters")
@Tag(
        name = "Schedule controller"
)
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Operation(description = "Creates a schedule")
    @PostMapping("/me/schedules")
    private ResponseEntity<String> createSchedule(Authentication authentication, @RequestBody ScheduleRequest request){
        if(scheduleService.save(authentication, request))
            return ResponseEntity.ok("OK!");
        return ResponseEntity.ok("ERROR!");
    }

    @Operation(description = "Returns all master schedules by token")
    @GetMapping("/me/schedules")
    private ResponseEntity<List<ScheduleResponse>> getMySchedules(Authentication authentication){
        return ResponseEntity.ok(scheduleService.findAllByAuthenticatedMaster(authentication));
    }

    @Operation(description = "Returns all master schedules by master id")
    @GetMapping("/{id}/schedules")
    private ResponseEntity<List<ScheduleResponse>> getSchedulesByMasterId(@PathVariable("id") Long id){
        return ResponseEntity.ok(scheduleService.findAllByMasterId(id));
    }

    @Operation(description = "Changes the schedule on specific days")
    @PutMapping("/me/schedules")
    private ResponseEntity<String> editMySchedules(Authentication authentication,
                                                   @RequestBody ScheduleRequest request){
        if (scheduleService.edit(request, authentication))
            return ResponseEntity.ok("OK!");
        return ResponseEntity.ok("ERROR!");
    }

    @Operation(description = "Deletes a schedule on specified days")
    @DeleteMapping("/me/schedules")
    private ResponseEntity<String> deleteMySchedules(Authentication authentication,
                                                     @RequestBody List<LocalDate> dates){
        if (scheduleService.delete(dates, authentication))
            return ResponseEntity.ok("OK!");
        return ResponseEntity.ok("ERROR!");
    }
}
