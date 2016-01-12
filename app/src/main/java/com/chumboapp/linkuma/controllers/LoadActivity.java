package com.chumboapp.linkuma.controllers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.CalendarView;
import android.widget.ImageView;

import com.chumboapp.linkuma.R;
import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class LoadActivity extends AppCompatActivity {
    private ImageView mLogoViewImage = null;
    private static final int TIME_TO_GO_NEXT_SCRENN = 1000;
    private static final String SEPARATOR = "@@++@@";
    private static final int CHUMBO_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        //Change Image (We use logo link)
        mLogoViewImage = (ImageView)findViewById(R.id.logo_image);
        mLogoViewImage.setImageResource(R.drawable.logo_link);

        //TODO delete contain in database
        getContentResolver().delete(CalendarProvider.CONTENT_URI,null,null);

        //TODO  Init download events

        //TODO ADD in Database (START WHEN FINISH DOWNLOAD)
        //
        //(Simulate with some examples)
        ExtendedCalendarView calendar = (ExtendedCalendarView)findViewById(R.id.calendar);

        ContentValues values = new ContentValues();
        values.put(CalendarProvider.COLOR, Event.COLOR_YELLOW);
        values.put(CalendarProvider.DESCRIPTION, "po comiendo, como va a ser si no," +
                "que to hay que decirlo hay que decirlo to, pip pam pum, desmayao, yo no me escuendo" +
                "unos lapillos de vino, quiere sentila en el pesxho cristian," +
                "no me diste ni media, nome diste ni mierda, en la vida hay 5 derechos innegables al ser " +
                "humano, 1 es la vivienda otro es la dignidad otro la ropa y en fin de los otros dos" +
                " no me acuerdo"+SEPARATOR+"El burgo");
        values.put(CalendarProvider.LOCATION, "04/01/2016"+SEPARATOR+"01:01-03:04");
        values.put(CalendarProvider.EVENT, "Comer chumbos");
        Calendar cal = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        cal.set(2016, 0, 04, 01, 01);
        int dayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values.put(CalendarProvider.START, cal.getTimeInMillis());
        values.put(CalendarProvider.START_DAY, dayJulian);
        cal.set(2016, 00, 04, 01, 01);
        int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values.put(CalendarProvider.END, cal.getTimeInMillis());
        values.put(CalendarProvider.END_DAY, endDayJulian);

        ContentValues values1 = new ContentValues();
        values1.put(CalendarProvider.COLOR, Event.COLOR_YELLOW);
        values1.put(CalendarProvider.DESCRIPTION, "Some Description Some Description Some " +
                "Description Some Description Some Description Some Description Some Description" +
                " Some Description Some Description Some Description Some Description" +
                " Some Description Some Description Some Description"+SEPARATOR+"Marbella");
        values1.put(CalendarProvider.LOCATION, "14/01/2016"+SEPARATOR+"10:01-14:00");
        values1.put(CalendarProvider.EVENT, "Cumple");


        cal.set(2016, 0, 14, 0, 01);
        dayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values1.put(CalendarProvider.START, cal.getTimeInMillis());
        values1.put(CalendarProvider.START_DAY, dayJulian);
        cal.set(2016, 0, 14, 0, 01);
        endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values1.put(CalendarProvider.END, cal.getTimeInMillis());
        values1.put(CalendarProvider.END_DAY, endDayJulian);

        ContentValues values2 = new ContentValues();
        values2.put(CalendarProvider.COLOR, Event.COLOR_YELLOW);
        values2.put(CalendarProvider.DESCRIPTION, "Description"+SEPARATOR+"Marbella");
        values2.put(CalendarProvider.LOCATION, "04/01/2016"+SEPARATOR+"11:01-11:30");
        values2.put(CalendarProvider.EVENT, "pim pam pum");


        cal.set(2016, 0, 04, 11, 01);
        dayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values2.put(CalendarProvider.START, cal.getTimeInMillis());
        values2.put(CalendarProvider.START_DAY, dayJulian);
        cal.set(2016, 0, 04, 11, 01);
        endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values2.put(CalendarProvider.END, cal.getTimeInMillis());
        values2.put(CalendarProvider.END_DAY, endDayJulian);

        Uri uri = getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
        getContentResolver().insert(CalendarProvider.CONTENT_URI, values1);
        getContentResolver().insert(CalendarProvider.CONTENT_URI, values2);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(LoadActivity.this, CalendarActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME_TO_GO_NEXT_SCRENN);
    }
}
