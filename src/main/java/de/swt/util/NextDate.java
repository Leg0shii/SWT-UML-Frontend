package de.swt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        try {
            for (String sDate : dates) {
                Date date = new SimpleDateFormat("u, H:m").parse(sDate);
                dateList.add(date);
            }
        } catch (ParseException e){
            System.out.println("Please enter date using format u, H:m where H = Hour m = Minute u = Number of Weekday (1 = Monday,..)");
        }
        return dateList;
    }

}
