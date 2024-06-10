package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.model.service.dto.TimeSlot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
public class MasterServiceTest {

    @Test
    public void getAvailableTimeSlots_withoutRecordsTimeSlots_isNotEmpty(){
        var allTimeSlots = List.of(TimeSlot.from(LocalTime.of(0, 30), LocalTime.of(2, 0)));
        List<TimeSlot> recordsTimeSlots = List.of();
        var duration = Duration.ofMinutes(30);
        List<LocalTime> availableTimeSlots = MasterService.getAvailableTimeSlots(allTimeSlots, recordsTimeSlots, duration);

        var expect = List.of(
                LocalTime.of(0,30),
                LocalTime.of(1,0),
                LocalTime.of(1,30)
        );

        Assertions.assertIterableEquals(expect, availableTimeSlots);
    }

    @Test
    public void getAvailableTimeSlots_withRecordsTimeSlots_isEmpty(){
        var allTimeSlots = List.of(TimeSlot.from(LocalTime.of(0, 30), LocalTime.of(2, 30)));
        List<TimeSlot> recordsTimeSlots = List.of(TimeSlot.from(LocalTime.of(0, 30), LocalTime.of(2, 30)));
        var duration = Duration.ofMinutes(30);
        List<LocalTime> availableTimeSlots = MasterService.getAvailableTimeSlots(allTimeSlots, recordsTimeSlots, duration);

        Assertions.assertTrue(availableTimeSlots.isEmpty());
    }

    @Test
    public void getAvailableTimeSlots_withRecordsTimeSlotsAndManyTimeSlots_isNotEmpty(){
        var allTimeSlots = List.of(
                TimeSlot.from(LocalTime.of(0, 30), LocalTime.of(2, 30)),
                TimeSlot.from(LocalTime.of(14, 30), LocalTime.of(21, 30))
        );
        List<TimeSlot> recordsTimeSlots = List.of(
                TimeSlot.from(LocalTime.of(1, 0), LocalTime.of(1, 30)),
                TimeSlot.from(LocalTime.of(16, 0), LocalTime.of(20, 10))
        );
        var duration = Duration.ofHours(1);
        List<LocalTime> availableTimeSlots = MasterService.getAvailableTimeSlots(allTimeSlots, recordsTimeSlots, duration);

        var expected = List.of(
                LocalTime.of(1, 30),
                LocalTime.of(14, 30),
                LocalTime.of(20, 10)
        );

        Assertions.assertIterableEquals(expected, availableTimeSlots);
    }

    @Test
    public void getAvailableTimeSlots_withManyTimeSlots_isNotEmpty(){
        var allTimeSlots = List.of(
                TimeSlot.from(LocalTime.of(0, 30), LocalTime.of(2, 30)),
                TimeSlot.from(LocalTime.of(14, 30), LocalTime.of(21, 30))
        );
        List<TimeSlot> recordsTimeSlots = List.of();
        var duration = Duration.ofHours(2);
        List<LocalTime> availableTimeSlots = MasterService.getAvailableTimeSlots(allTimeSlots, recordsTimeSlots, duration);

        var expected = List.of(
                LocalTime.of(0, 30),
                LocalTime.of(14, 30),
                LocalTime.of(16, 30),
                LocalTime.of(18, 30)
        );

        Assertions.assertIterableEquals(expected, availableTimeSlots);
    }

    @Test
    public void getAvailableTimeSlots_withRecordsTimeSlots(){
        var allTimeSlots = List.of(TimeSlot.from(LocalTime.of(0, 30), LocalTime.of(2, 30)));
        List<TimeSlot> recordsTimeSlots = List.of(TimeSlot.from(LocalTime.of(1, 0), LocalTime.of(1, 30)));
        var duration = Duration.ofMinutes(30);
        List<LocalTime> availableTimeSlots = MasterService.getAvailableTimeSlots(allTimeSlots, recordsTimeSlots, duration);

        var expect = List.of(
                LocalTime.of(0,30),
                LocalTime.of(1,30),
                LocalTime.of(2,0)
        );

        Assertions.assertIterableEquals(expect, availableTimeSlots);
    }

    @Test
    public void getSlotsBetweenTime_returnsNotEmptyList(){
        var duration = Duration.ofMinutes(30);
        var start = LocalTime.of(0,0);
        var end = LocalTime.of(2,10);

        List<TimeSlot> slotsBetweenTime = MasterService.getSlotsBetweenTime(duration, start, end);

        var expect = List.of(TimeSlot.from(LocalTime.of(0,0), LocalTime.of(0,30)),
                TimeSlot.from(LocalTime.of(0,30), LocalTime.of(1,0)),
                TimeSlot.from(LocalTime.of(1,0), LocalTime.of(1,30)),
                TimeSlot.from(LocalTime.of(1,30), LocalTime.of(2,0)));
        Assertions.assertIterableEquals(expect, slotsBetweenTime);
    }

    @Test
    public void getSlotsBetweenTime_returnsEmptyList(){
        var duration = Duration.ofHours(3);
        var start = LocalTime.of(0,0);
        var end = LocalTime.of(2,10);

        List<TimeSlot> slotsBetweenTime = MasterService.getSlotsBetweenTime(duration, start, end);

        var expect = List.of();
        Assertions.assertIterableEquals(expect, slotsBetweenTime);
    }
}
