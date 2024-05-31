package com.prosvirnin.webregistration.model.service.dto;

import com.prosvirnin.webregistration.model.service.Schedule;
import com.prosvirnin.webregistration.model.user.Master;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ScheduleRequest {
    private List<LocalDate> dates;
    private List<TimeSlot> timeSlots;

    public List<Schedule> toSchedules(){
        var schedules = new ArrayList<Schedule>(dates.size() * timeSlots.size());
        for (var date : dates){
            for (var timeSlot : timeSlots){
                schedules.add(Schedule.builder()
                                .date(date)
                                .timeFrom(timeSlot.getFrom())
                                .timeTo(timeSlot.getTo())
                                .build());
            }
        }
        return schedules;
    }

    public List<Schedule> toSchedules(Master master){
        var schedules = toSchedules();
        for (var schedule : schedules)
            schedule.setMaster(master);
        return schedules;
    }
}
