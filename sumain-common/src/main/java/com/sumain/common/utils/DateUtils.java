package com.sumain.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public final static String DATE_PATTERN = "yyyy-MM-dd";

    public final static String TIME_PATTERN = "HH:mm:ss";

    public static LocalDateTime toLocalDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(String dateTime, String formatter) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(formatter));
    }


    public static LocalDateTime toLocalDateTime(String dateTime) {
        return toLocalDateTime(dateTime, DATE_TIME_PATTERN);
    }

    public static String format(LocalDateTime localDateTime) {
        return format(localDateTime, DATE_TIME_PATTERN);
    }

    public static String format(LocalDateTime localDateTime, String formatter) {
        return localDateTime.format(DateTimeFormatter.ofPattern(formatter));
    }

    public static LocalDate toLocalDate(String date) {
        return toLocalDate(date, DATE_PATTERN);
    }

    public static LocalDate toLocalDate(String date, String formatter) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(formatter));
    }

    public static String format(LocalDate localDate) {
        return format(localDate, DATE_PATTERN);
    }

    public static String format(LocalDate localDate, String formatter) {
        return localDate.format(DateTimeFormatter.ofPattern(formatter));
    }

    public static LocalTime toLocalTime(String time, String formatter) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(formatter));
    }

    public static LocalTime toLocalTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    public static LocalDateTime plusDateTime(LocalDateTime sourceTime, Long timeNum, ChronoUnit unit) {
        if (unit == ChronoUnit.DAYS) return sourceTime.plusDays(timeNum);
        if (unit == ChronoUnit.HOURS) return sourceTime.plusHours(timeNum);
        if (unit == ChronoUnit.SECONDS) return sourceTime.plusSeconds(timeNum);
        if (unit == ChronoUnit.MINUTES) return sourceTime.plusMinutes(timeNum);
        if (unit == ChronoUnit.WEEKS) return sourceTime.plusWeeks(timeNum);
        if (unit == ChronoUnit.MONTHS) return sourceTime.plusMonths(timeNum);
        if (unit == ChronoUnit.YEARS) return sourceTime.plusYears(timeNum);
        throw new RuntimeException("plus time error:unsupported " + unit.name() + " unit");
    }
}
