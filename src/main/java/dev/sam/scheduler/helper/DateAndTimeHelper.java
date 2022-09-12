package dev.sam.scheduler.helper;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Helper functions for dates and times
 */
public class DateAndTimeHelper {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Convert database datetime string into local date time
     * @param dateTimeStr The database string
     * @return The local date time of UTC
     */
    public static OffsetDateTime dbDateStringToDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, formatter).atOffset(ZoneOffset.UTC);
    }

    /**
     * Convert offset datetime to local datetime
     * @param time The time to convert
     * @return
     */
    public static String offsetDateTimeToLocalDateTimeStr(OffsetDateTime time) {
        return time.atZoneSameInstant(ZoneId.systemDefault()).format(formatter);
    }

    public static String localDateTimeToUTCDbStr(LocalDateTime time) {
        return time.atOffset(ZoneOffset.UTC).format(formatter);
    }

    /**
     * Convert offset date time to database string
     * @param offsetDateTime The datetime to convert.
     * @return The string for the database.
     */
    public static String offsetDateTimeToDbStr(OffsetDateTime offsetDateTime) {
        return offsetDateTime.format(formatter);
    }

    /**
     * Convert local date and time to offset date time in UTC
     * @param date The local date
     * @param time The local time
     * @return The offset date time
     */
    public static OffsetDateTime localDateAndTimeToUTC(LocalDate date, LocalTime time) {
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime();
    }

    /**
     * Convert offset datetime to localdate object
     * @param offsetDateTime The datetime to convert
     * @return The localdate
     */
    public static LocalDate offsetDateTimeToLocalDate(OffsetDateTime offsetDateTime) {
        ZonedDateTime zonedDateTime = offsetDateTime.toZonedDateTime().withZoneSameInstant(ZoneId.systemDefault());
        return zonedDateTime.toLocalDate();
    }

    /**
     * Convert offsetDateTime to local time string
     * @param offsetDateTime The time input
     * @return The local time string
     */
    public static String offsetDateTimeToLocalTimeStr(OffsetDateTime offsetDateTime) {
        ZonedDateTime zonedDateTime = offsetDateTime.toZonedDateTime().withZoneSameInstant(ZoneId.systemDefault());
        return zonedDateTime.format(timeFormatter);
    }
}
