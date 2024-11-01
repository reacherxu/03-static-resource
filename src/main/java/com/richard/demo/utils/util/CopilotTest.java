package com.richard.demo.utils.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CopilotTest {

    private int calculateDaysBetweenDates(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
