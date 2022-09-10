package dev.sam.scheduler.helper;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateAndTimeHelper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static OffsetDateTime dbDateStringToDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, formatter).atOffset(ZoneOffset.UTC);
    }

    public static String offsetDateTimeToLocalTimeStr(OffsetDateTime time) {
        return time.atZoneSameInstant(ZoneId.systemDefault()).format(formatter);
    }

    public static String localDateTimeToUTCDbStr(LocalDateTime time) {
        return time.atOffset(ZoneOffset.UTC).format(formatter);
    }

    public static String offsetDateTimeToDbStr(OffsetDateTime offsetDateTime) {
        return offsetDateTime.format(formatter);
    }

    public static OffsetDateTime localDateAndTimeToUTC(LocalDate date, LocalTime time) {
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime();
    }
}
