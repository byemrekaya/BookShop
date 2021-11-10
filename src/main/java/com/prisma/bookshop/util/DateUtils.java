package com.prisma.bookshop.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DateUtils {
    public static Optional<LocalDate> parseDate(String date, String pattern) {
        return Optional.ofNullable(date)
                .filter(dateVal -> !dateVal.isEmpty())
                .map(dateVal -> LocalDate.parse(dateVal, DateTimeFormatter.ofPattern(pattern)));
    }
}