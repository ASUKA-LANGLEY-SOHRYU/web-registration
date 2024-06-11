package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.model.service.dto.RecordRequest;
import com.prosvirnin.webregistration.model.service.dto.RecordResponse;
import com.prosvirnin.webregistration.service.RecordService;
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
@Tag(name = "Record controller")
public class RecordController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @Operation(description = "Creates a record")
    @PostMapping("/{id}/records")
    public ResponseEntity<Long> createRecord(Authentication authentication, @PathVariable("id") Long id,
                                             @RequestBody RecordRequest request) {
        return ResponseEntity.ok(recordService.create(authentication, id, request));
    }

    @Operation(description = "Retrieving records by master id. Private information is hidden from other users.")
    @GetMapping("/{id}/records")
    public ResponseEntity<List<RecordResponse>> getAllRecordsByMasterId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(recordService.findAllRecordsByMasterId(id));
    }

    @Operation(description = "Retrieving records by master token")
    @GetMapping("/me/records")
    public ResponseEntity<List<RecordResponse>> getAllMyRecords(Authentication authentication) {
        return ResponseEntity.ok(recordService.findAllRecordsByAuthentication(authentication));
    }

    @Operation(description = "Cancels record by record id")
    @DeleteMapping("/records/{id}")
    public ResponseEntity<String> cancelRecord(Authentication authentication, @PathVariable("id") Long id) {
        return ResponseEntity.ok(recordService.cancel(authentication, id));
    }

    @Operation(description = "Cancels all records for a given day by master token")
    @DeleteMapping("/me/records")
    public ResponseEntity<String> cancelRecordsByDate(Authentication authentication,
                                                      @RequestBody LocalDate date) {
        return ResponseEntity.ok(recordService.cancelAllByDate(authentication, date));
    }

    //TODO:Контроллеры для получения мастеров/юзеров
}
