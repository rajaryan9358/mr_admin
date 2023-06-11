package com.ifstatic.mradmin.utilities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormat  {

    public static String getCurrentDate(){

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(c);
    }

    public static void getDateFromCalender(Context context , DateSelectListener dateSelectListener) {

        Calendar calendar;
        int year,day,month;

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"-"+DateFormat.getMonthInCharacters(month)+"-"+year;
                dateSelectListener.onSelectedDate(date);
            }
        },year,month,day);

        dialog.show();
    }

    public interface DateSelectListener {
        void onSelectedDate(String date);
    }

    public static String getMonthInCharacters(int month){

        String monthCharacter = null;

        switch (month){
            case 1 : monthCharacter = "Jan";
                break;
            case 2 : monthCharacter = "Feb";
                break;
            case 3 : monthCharacter = "Mar";
                break;
            case 4 : monthCharacter = "Apr";
                break;
            case 5 : monthCharacter = "May";
                break;
            case 6 : monthCharacter = "Jun";
                break;
            case 7 : monthCharacter = "Jul";
                break;
            case 8 : monthCharacter = "Aug";
                break;
            case 9 : monthCharacter = "Sep";
                break;
            case 10 : monthCharacter = "Oct";
                break;
            case 11 : monthCharacter = "Nov";
                break;
            case 12 : monthCharacter = "Dec";
                break;
        }
        return monthCharacter;
    }

}