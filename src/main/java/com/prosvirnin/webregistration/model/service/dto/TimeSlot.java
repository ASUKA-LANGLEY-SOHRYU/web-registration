package com.prosvirnin.webregistration.model.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@Builder
public class TimeSlot {
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Schema(type = "string", pattern = "HH:mm:ss", example = "14:30:00")
    private LocalTime from;
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Schema(type = "string", pattern = "HH:mm:ss", example = "14:30:00")
    private LocalTime to;

    public static TimeSlot from(LocalTime start, Duration duration){
        return new TimeSlot(start, start.plus(duration));
    }

    public static TimeSlot from(LocalTime start, LocalTime end){
        return new TimeSlot(start, end);
    }
}
