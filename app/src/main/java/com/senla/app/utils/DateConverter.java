package com.senla.app.utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateConverter {

    private DateConverter() { }

    static public int[] getDateInArray(String date) {

        Pattern dateFormat = Pattern.compile("^(3[0-1]|[1-2][0-9]|0?[1-9])\\.(1[0-2]|0?[1-9])\\.([1-2][0-9]{3})$");
        Matcher matcher = dateFormat.matcher(date);

        if (matcher.matches()) {
            int fromDate = Integer.parseInt(matcher.group(1));
            int fromMonth = Integer.parseInt(matcher.group(2));
            int fromYear = Integer.parseInt(matcher.group(3));

            return new int[]{fromDate, fromMonth, fromYear};
        }

        return null;
    }

    static public String calendarToString(Calendar date) {
        return String.format(
                "%d.%d.%d",
                date.get(Calendar.DATE),
                date.get(Calendar.MONTH) + 1,
                date.get(Calendar.YEAR)
        );
    }

    static public Calendar jsonStringToCalendar(String date) {
        return localDateToCalendar(LocalDate.parse(date));
    }

    static public Calendar dateSqlToCalendar(Date date) {
        if (date == null) return null;

        return localDateToCalendar(date.toLocalDate());
    }

    static public Calendar localDateToCalendar(LocalDate localDate) {
        return new GregorianCalendar(
                localDate.getYear(),
                localDate.getMonth().getValue() - 1,
                localDate.getDayOfMonth()
        );
    }

    static public LocalDate calendarToLocalDate(Calendar calendar) {
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();

        return LocalDateTime.ofInstant(calendar.toInstant(), zid).toLocalDate();
    }
}
