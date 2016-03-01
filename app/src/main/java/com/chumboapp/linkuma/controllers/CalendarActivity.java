package com.chumboapp.linkuma.controllers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import com.chumboapp.linkuma.R;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

/**
 * Created by MANOLO on 09/01/2016.
 */
public class CalendarActivity extends AppCompatActivity {
    private ExtendedCalendarView mCalendar = null;
    private int d = 0;
    private int m = 0;
    private int y = 0;
    private String date = "";
    private static final int ALARM_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        // TODO Ocultar barra de título

        mCalendar = (ExtendedCalendarView)findViewById(R.id.calendar);
        mCalendar.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapter, View view,
                                     int position, long id, Day day) {
                d = day.getDay();
                m = day.getMonth() + 1;
                y = day.getYear();
                String aux_m =  String.valueOf(m);
                String aux_d = String.valueOf(d);
                if (m < 10){
                    aux_m = "0" + String.valueOf(m);
                }
                if (d < 10){
                    aux_d = "0" + String.valueOf(d);
                }
                date = aux_d + "/" + aux_m + "/" + String.valueOf(y);
            }
    });

        //TODO CREATE ALARMN_MANAGER
      /*  Intent myIntent = new Intent(this,notification.class);
        AlarmManager alarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
        // Set the alarm to start at 22:00.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 29);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                24 * 60 * 60 * 1000, pendingIntent);*/
    }


    public void goToDay(View v){
        if (date == ""){
            String select = getResources().getString(R.string.select_day);
            Toast.makeText(CalendarActivity.this, select,
                    Toast.LENGTH_LONG).show();
        } else{
            Intent intent = new Intent(CalendarActivity.this, DayActivity.class);
            intent.putExtra("CHUMBOAPP_DAY", date);
            Log.i("fecha", date);
            startActivity(intent);
        }
    }

}
