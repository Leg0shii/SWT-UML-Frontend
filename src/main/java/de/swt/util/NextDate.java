package de.swt.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NextDate {

    public static Date getNextDate(ArrayList<Long> milliList) {

        int size = milliList.size();
        Long[] millArray = new Long[size];
        millArray = milliList.toArray(millArray);

        long nowMillis = System.currentTimeMillis();
        long diffMillis = 3153600000000L;
        long nextDateMillis = 0;

        for (int i = 0; i < size; i++){
            while (millArray[i] - nowMillis < 0){
                millArray[i] = millArray[i] + 604800000L;
            }

            if (millArray[i] - nowMillis < diffMillis){
                nextDateMillis = millArray[i];
                diffMillis = millArray[i] - nowMillis;
            }
        }
        if (nextDateMillis == 0){
            System.out.println("GetNextDate: ERROR - NO FUTURE DATE!");
            return null;
        }
        return new Date(nextDateMillis);
    }

    // mm.hh.TT.MM.JJJJ;mm.hh.TT.MM.JJJJ
    public static ArrayList<Date> getDateFromString(String dateString) {

        String dates[] = dateString.split(";");
        ArrayList<Date> dateList = new ArrayList<>();

        for(String sDate : dates) {
            Date date = new Date();
            String[] dateComponents = sDate.split("./");
            date.setMinutes(Integer.parseInt(dateComponents[0]));
            date.setHours(Integer.parseInt(dateComponents[1]));
            date.setDate(Integer.parseInt(dateComponents[2]));
            date.setMonth(Integer.parseInt(dateComponents[3]));
            date.setYear(Integer.parseInt(dateComponents[4]));
            dateList.add(date);
        }
        return dateList;
    }

}
