package com.prosvirnin.webregistration.model.service.dto;

import com.prosvirnin.webregistration.model.service.Record;
import com.prosvirnin.webregistration.model.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Data
@Builder
public class RecordResponse {
    private Long id;
    private Long masterId;
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
    private String comment;

    public static RecordResponse fromRecord(Record record, User me) {
        var result = RecordResponse.builder()
                .id(record.getId())
                .date(record.getDate())
                .timeFrom(record.getTimeFrom())
                .timeTo(record.getTimeTo())
                .status(record.getRecordStatus().name())
                .service(ServiceDTOResponse.fromService(record.getService()))
                .build();
        if (isUserShouldSeePrivateData(me, record)) {
            result.setClient(ClientResponse.fromClient(record.getClient().getUser()));
            result.setComment(record.getComment());
        }
        return result;
    }

    private static boolean isUserShouldSeePrivateData(User me, Record record) {
        return me == null ||
                (me.getMaster() != null && Objects.equals(me.getMaster().getId(), record.getMaster().getId())) ||
                (me.getClient() != null && Objects.equals(me.getClient().getId(), record.getClient().getId()));
    }
}
