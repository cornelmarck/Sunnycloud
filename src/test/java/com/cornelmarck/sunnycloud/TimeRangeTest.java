package com.cornelmarck.sunnycloud;

import com.cornelmarck.sunnycloud.util.TimeRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TimeRangeTest {

    @Test
    public void splitTwoPieces() {
        TimeRange range = new TimeRange(LocalDateTime.parse("2021-03-21T23:15:00Z"), LocalDateTime.parse("2021-05-01T04:13:12Z"));
        Duration maxDuration = Duration.of(28, ChronoUnit.DAYS);
        List<TimeRange> split = range.split(maxDuration);
        Assertions.assertEquals(2, split.size());
        Assertions.assertEquals(maxDuration, split.get(0).getDuration());
    }

    @Test
    public void splitZeroLengthInterval() {
        TimeRange range = new TimeRange(LocalDateTime.parse("2021-03-21T23:15:00Z"), LocalDateTime.parse("2021-03-21T23:15:00Z"));
        Duration maxDuration = Duration.of(28, ChronoUnit.DAYS);
        List<TimeRange> split = range.split(maxDuration);
        Assertions.assertTrue(split.isEmpty());
    }
}
