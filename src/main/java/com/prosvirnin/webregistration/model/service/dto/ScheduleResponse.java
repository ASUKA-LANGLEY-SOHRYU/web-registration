package com.prosvirnin.webregistration.model.service.dto;

import com.prosvirnin.webregistration.model.service.Schedule;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class ScheduleResponse {
    private LocalDate date;
    private List<TimeSlot> timeSlots;

    public static List<ScheduleResponse> fromSchedules(List<Schedule> schedules){
        var hashMap = new HashMap<LocalDate, List<TimeSlot>>();
        for (var schedule : schedules){
            var date = schedule.getDate();
            if(!hashMap.containsKey(date))
                hashMap.put(date, new ArrayList<>());
            hashMap.get(date).add(new TimeSlot(schedule.getTimeFrom(),
                    schedule.getTimeTo()));
        }
        var result = new ArrayList<ScheduleResponse>(hashMap.size());
        for (var entry : hashMap.entrySet()){
            result.add(ScheduleResponse.builder()
                            .date(entry.getKey())
                            .timeSlots(entry.getValue())
                            .build());
        }
        return result;
    }
}
