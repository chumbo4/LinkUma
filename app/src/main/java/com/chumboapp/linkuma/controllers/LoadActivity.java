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
        values.put(CalendarProvider.DESCRIPTION, "Se dará la bienvenida a los candidatos " +
                "seleccionados para participar en el Programa Yuzz de jóvenes emprendedores."
                +SEPARATOR+"Link by UMA-Atech, 3ª planta");
        values.put(CalendarProvider.LOCATION, "14/01/2016"+SEPARATOR+"16:00-19:00");
        values.put(CalendarProvider.EVENT, "Bienvenida a participantes del Programa Yuzz");
        Calendar cal = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        cal.set(2016, 0, 14, 16, 00);
        int dayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values.put(CalendarProvider.START, cal.getTimeInMillis());
        values.put(CalendarProvider.START_DAY, dayJulian);
        cal.set(2016, 00, 14, 19, 00);
        int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values.put(CalendarProvider.END, cal.getTimeInMillis());
        values.put(CalendarProvider.END_DAY, endDayJulian);

        ContentValues values1 = new ContentValues();
        values1.put(CalendarProvider.COLOR, Event.COLOR_YELLOW);
        values1.put(CalendarProvider.DESCRIPTION, "Aprende a hacer aquello que quieres hacer sin que nada te frene\n" +
                "Disfruta más de lo que haces en tu vida\n" +
                "Inscríbete en: www.nayadepsicologos.com/inscripcion"+SEPARATOR+"Link by UMA-Atech, 3ª planta");
        values1.put(CalendarProvider.LOCATION, "14/01/2016"+SEPARATOR+"17:00-21:00");
        values1.put(CalendarProvider.EVENT, "Curso Iniciación en Mindfulness");
        cal.set(2016, 0, 14, 17, 00);
        dayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values1.put(CalendarProvider.START, cal.getTimeInMillis());
        values1.put(CalendarProvider.START_DAY, dayJulian);
        cal.set(2016, 0, 14, 21, 00);
        endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values1.put(CalendarProvider.END, cal.getTimeInMillis());
        values1.put(CalendarProvider.END_DAY, endDayJulian);



        ContentValues values2 = new ContentValues();
        values2.put(CalendarProvider.COLOR, Event.COLOR_YELLOW);
        values2.put(CalendarProvider.DESCRIPTION, "Clases de la asignatura “Emprendedores en Ingeniería” perteneciente al Máster de Ingeniería Industrial. La docencia se impartirà todos los viernes en la sala 201 de Link by UMA-ATech, en horario de 8.00 a 9.15 am.\n" +
                "\n" +
                "Coordinador del Máster, Antonio Ruiz Molina. Dpto. Economía y Administración de Empresas Universidad de Málaga."+SEPARATOR+"Link By UMA-Atech, 2ª planta\n" +
                "Edificio The Green Ray, Avenida Louis Pasteur 47 (ampliación del Campus Teatinos) \n" +
                "Málaga, Málaga");
        values2.put(CalendarProvider.LOCATION, "15/01/2016"+SEPARATOR+"08:00-09:30");
        values2.put(CalendarProvider.EVENT, "Emprendedores en la Ingeniería");
        cal.set(2016, 0, 15, 8, 00);
        dayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values2.put(CalendarProvider.START, cal.getTimeInMillis());
        values2.put(CalendarProvider.START_DAY, dayJulian);
        cal.set(2016, 0, 15, 9, 30);
        endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values2.put(CalendarProvider.END, cal.getTimeInMillis());
        values2.put(CalendarProvider.END_DAY, endDayJulian);


        ContentValues values3 = new ContentValues();
        values3.put(CalendarProvider.COLOR, Event.COLOR_YELLOW);
        values3.put(CalendarProvider.DESCRIPTION, "La sexta sesión abordará los siguientes temas:\n" +
                "\n" +
                "10.00 – 11.30 h “El empresario con formación de ingeniero”. D. Juan Castillo Rosa. Empresario. Profesor del Departamento de Ingeniería Eléctrica de la ETSII. Universidad de Málaga."+SEPARATOR+"Link By UMA-Atech, 2ª planta\n" +
                "Edificio The Green Ray, Avenida Louis Pasteur 47 (ampliación del Campus Teatinos) \n" +
                "Málaga, Málaga");
        values3.put(CalendarProvider.LOCATION, "15/01/2016"+SEPARATOR+"10:00-11:30");
        values3.put(CalendarProvider.EVENT, "III Taller de Emprendimiento en la práctica – ETS Ingeniería Industrial");
        cal.set(2016, 0, 15, 10, 00);
        dayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values3.put(CalendarProvider.START, cal.getTimeInMillis());
        values3.put(CalendarProvider.START_DAY, dayJulian);
        cal.set(2016, 0, 15, 11, 30);
        endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values3.put(CalendarProvider.END, cal.getTimeInMillis());
        values3.put(CalendarProvider.END_DAY, endDayJulian);



        Uri uri = getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
        getContentResolver().insert(CalendarProvider.CONTENT_URI, values1);
        getContentResolver().insert(CalendarProvider.CONTENT_URI, values2);
        getContentResolver().insert(CalendarProvider.CONTENT_URI, values3);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(LoadActivity.this, CalendarActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME_TO_GO_NEXT_SCRENN);
    }
}
