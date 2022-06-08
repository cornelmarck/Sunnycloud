package com.cornelmarck.sunnycloud.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class TimeUtils {
    public static LocalDateTime getLocalDateTime(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static LocalDateTime roundUpToQuarter(LocalDateTime time) {
        time = time.minusNanos(1);
        return time.truncatedTo(ChronoUnit.HOURS).plusMinutes(15 * (time.getMinute()/15 + 1));
    }

    public static Instant toInstant(LocalDateTime timestamp, ZoneId zoneId) {
        ZoneOffset offset = zoneId.getRules().getOffset(timestamp);
        return timestamp.toInstant(offset);
    }
}

