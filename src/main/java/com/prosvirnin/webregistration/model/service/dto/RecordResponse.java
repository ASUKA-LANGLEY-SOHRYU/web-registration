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
public class RecordResponse {
    private Long id;
    private LocalDate date;
    private String status;
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Schema(type = "string", pattern = "HH:mm:ss", example = "14:30:00")
    private LocalTime timeFrom;
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Schema(type = "string", pattern = "HH:mm:ss", example = "14:30:00")
    private LocalTime timeTo;
    private ServiceDTOResponse service;
    private ClientResponse client;

    public static RecordResponse fromRecord(Record record, boolean hideClients){
        var result = RecordResponse.builder()
                .id(record.getId())
                .date(record.getDate())
                .timeFrom(record.getTimeFrom())
                .timeTo(record.getTimeTo())
                .status(record.getRecordStatus().name())
                .service(ServiceDTOResponse.fromService(record.getService()))
                .build();
        if (!hideClients)
            result.setClient(ClientResponse.fromClient(record.getClient().getUser()));
        return result;
    }
}
