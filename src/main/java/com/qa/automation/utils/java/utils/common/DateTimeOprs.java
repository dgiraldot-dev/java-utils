package com.qa.automation.utils.java.utils.common;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeOprs {

    private static final Logger LOGGER = LogManager.getLogger(DateTimeOprs.class);

    private DateTimeOprs() {
        // Initialize without attributes
    }

    public static String getCurrentTime(String format) {
        return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
    }

    public static String getDateOfLastDayOfBeforeMonth(String stringDate, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        Date date = null;
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getDateOfFirstDayOfCurrentMonth(String stringDate, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        Date date = null;
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        return simpleDateFormat.format(calendar.getTime());
    }
}
