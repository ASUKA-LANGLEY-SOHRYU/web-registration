package com.prosvirnin.webregistration.model.service.dto;

import com.prosvirnin.webregistration.model.service.Service;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ServiceDTOResponse {
    private Long id;

    private String name;

    private String description;

    private Long price;

    private Duration duration;

    public static ServiceDTOResponse fromService(Service service){
        long durationInMinutes = service.getDuration().toMinutes();
        long hours = durationInMinutes / 60;
        long minutes = durationInMinutes % 60;

        var duration = new Duration(hours, minutes);
        return ServiceDTOResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .price(service.getPrice())
                .description(service.getDescription())
                .duration(duration)
                .build();
    }
}
