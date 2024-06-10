package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.model.service.dto.TimeSlot;

import java.time.Duration;
import java.time.LocalTime;

public class TimeHelper {

    public static boolean isInRange(TimeSlot range, LocalTime from, LocalTime to){
        return !from.isBefore(range.getFrom()) && !to.isAfter(range.getTo());
    }

    public static boolean isInRange(TimeSlot range, TimeSlot innerRange){
        return isInRange(range, innerRange.getFrom(), innerRange.getTo());
    }

    public static boolean isInRange(TimeSlot range, LocalTime from, Duration duration){
        return isInRange(range, from, from.plus(duration));
    }

    public static boolean isOverlapping(TimeSlot a, TimeSlot b){
        return !(a.getFrom().isAfter(b.getTo()) || a.getTo().isBefore(b.getFrom()));
    }

    public static boolean isOverlappingNotStrict(TimeSlot a, TimeSlot b){
        return a.getFrom().isBefore(b.getTo()) && b.getFrom().isBefore(a.getTo());
    }
}
