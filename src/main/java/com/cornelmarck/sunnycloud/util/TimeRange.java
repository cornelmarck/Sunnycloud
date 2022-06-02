package com.cornelmarck.sunnycloud.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeRange {
    private final LocalDateTime from;
    private final LocalDateTime to;

    public TimeRange(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public Duration getDuration() {
        return Duration.between(from, to);
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public List<TimeRange> splitToNonEmpty(Duration maxDuration) {
        List<TimeRange> out = new ArrayList<>();
        LocalDateTime begin = from;
        while (begin.isBefore(to)) {
            LocalDateTime end = Collections.min(List.of(begin.plus(maxDuration), to));
            out.add(new TimeRange(begin, end));
            begin = end;
        }
        return out;
    }


}
