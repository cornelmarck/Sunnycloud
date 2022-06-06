package com.cornelmarck.sunnycloud.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeRange {
    private final Instant from;
    private final Instant to;

    public TimeRange(Instant from, Instant to) {
        this.from = from;
        this.to = to;
    }

    public Duration getDuration() {
        return Duration.between(from, to);
    }

    public Instant getFrom() {
        return from;
    }

    public Instant getTo() {
        return to;
    }

    public List<TimeRange> split(Duration maxDuration) {
        List<TimeRange> out = new ArrayList<>();
        Instant begin = from;
        while (begin.isBefore(to)) {
            Instant end = Collections.min(List.of(begin.plus(maxDuration), to));
            out.add(new TimeRange(begin, end));
            begin = end;
        }
        return out;
    }
}
