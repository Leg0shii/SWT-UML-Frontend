package de.swt.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class NextDate {

    /**
     * @param milliList List of dates in milliseconds.
     * @return Smallest next date that doesnt lie further ahead then 7 days based on 7d intervalls.
     */
    public Date getNextDate(ArrayList<Long> milliList) {

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

}
