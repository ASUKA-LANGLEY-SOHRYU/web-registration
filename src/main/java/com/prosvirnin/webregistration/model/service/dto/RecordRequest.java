package com.prosvirnin.webregistration.model.service.dto;

import com.prosvirnin.webregistration.model.service.Record;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class RecordRequest {
    private LocalDate date;
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Schema(type = "string", pattern = "HH:mm:ss", example = "14:30:00")
    private LocalTime timeFrom;
    private Long serviceId;
    private String comment;
}
