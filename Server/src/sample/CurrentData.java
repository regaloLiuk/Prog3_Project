package sample;

import java.time.LocalDateTime;
import java.util.Calendar;

public class CurrentData {

    private static String monthToString(int month){
        String sett="";
        if (month == 1) {
            sett = "Gennaio";
        } else if (month == 2) {
            sett = "Febbraio";
        } else if (month == 3) {
            sett = "Marzo";
        } else if (month == 4) {
            sett = "Aprile";
        } else if (month == 5) {
            sett = "Maggio";
        } else if (month == 6) {
            sett = "Giugno";
        } else if (month == 7) {
            sett = "Luglio";
        } else if (month == 8) {
            sett = "Agosto";
        } else if (month == 9) {
            sett = "Settembre";
        } else if (month == 10) {
            sett = "Ottobre";
        } else if (month == 11) {
            sett = "Novembre";
        } else if (month == 12) {
            sett = "Dicembre";
        }
        return sett;
    }

    public static String getNow(){
        Calendar calendar=Calendar.getInstance();
        LocalDateTime currentTime = LocalDateTime.now();

        int month = currentTime.getMonthValue();
        int day = currentTime.getDayOfMonth();
        int year = currentTime.getYear();
        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int second = currentTime.getSecond();


        String sett=monthToString(month);


        return day+"/"+month+"/"+year+" "+hour+"."+minute+"."+second;
    }



}
