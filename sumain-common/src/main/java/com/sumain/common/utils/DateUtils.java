package com.sumain.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public final static String DATE_PATTERN = "yyyy-MM-dd";

    public static LocalDateTime toLocalDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(String dateTime, String formatter){
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(formatter));
    }


    public static LocalDateTime toLocalDateTime(String dateTime){
        return toLocalDateTime(dateTime,DATE_TIME_PATTERN);
    }

    public static String format(LocalDateTime localDateTime){
        return format(localDateTime,DATE_TIME_PATTERN);
    }

    public static String format(LocalDateTime localDateTime,String formatter){
        return localDateTime.format(DateTimeFormatter.ofPattern(formatter));
    }

    public static LocalDate toLocalDate(String date){
        return toLocalDate(date,DATE_PATTERN);
    }

    public static LocalDate toLocalDate(String date, String formatter){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(formatter));
    }

    public static String format(LocalDate localDate){
        return format(localDate,DATE_PATTERN);
    }

    public static String format(LocalDate localDate,String formatter){
        return localDate.format(DateTimeFormatter.ofPattern(formatter));
    }
}
