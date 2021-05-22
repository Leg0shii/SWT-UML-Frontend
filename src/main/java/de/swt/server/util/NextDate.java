package de.swt.server.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class NextDate {

    public Date getNextDate(ArrayList<Long> milliList) {

        //Umwandlung Arraylist -> Array
        int size = milliList.size();
        Long[] millArray = new Long[size];
        millArray = milliList.toArray(millArray);

        //Hilfsgrößen mit aktueller Uhrzeit, Differenz zu Terminen in der Arraylist und dem bisher nächsten Datum
        long nowMillis = System.currentTimeMillis();
        long diffMillis = 3153600000000L; //Bissel Dirty aber ist für diesen Zweck ok, glaube ich
        long nextDateMillis = 0;

        //Durch Liste iterieren und nächstes Datum finden, falls es eins gibt
        for (int i = 0; i < size; i++){

            //Falls Datum in der Vergangenheit, zähle Wochenweise hoch bis Zukunft erreicht
            while (millArray[i] - nowMillis < 0){
                millArray[i] = millArray[i] + 604800000L;
            }

            //Ermittle Abstand und vergleiche mit momentan kürzestem Abstand
            if (millArray[i] - nowMillis < diffMillis){
                nextDateMillis = millArray[i];
                diffMillis = millArray[i] - nowMillis;
            }
        }
        //Falls kein Datum in der Zukunft verfügbar: Fehler
        if (nextDateMillis == 0){
            System.out.println("GetNextDate: ERROR - NO FUTURE DATE!");
            return null;
        }
        //Ansonsten bestes Datum zurückgeben
        return new Date(nextDateMillis);
    }

}
