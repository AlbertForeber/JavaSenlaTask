package task.utils;

import java.util.Calendar;
import java.util.IllegalFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataConverter {
    private DataConverter() {}

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
}
