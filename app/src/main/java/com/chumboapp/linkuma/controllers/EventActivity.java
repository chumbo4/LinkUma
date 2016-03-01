package com.chumboapp.linkuma.controllers;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.chumboapp.linkuma.R;
import com.tyczj.extendedcalendarview.CalendarProvider;

import java.util.StringTokenizer;

public class EventActivity extends AppCompatActivity {
    private static final String SEPARATOR = "@@++@@";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        // TODO Ocultar barra de título

        TextView mNameView = (TextView) findViewById(R.id.name_event_view);
        TextView mDayView = (TextView) findViewById(R.id.date_start_event_view);

        TextView mHourView = (TextView) findViewById(R.id.hour_start_event_view);
        TextView mDescriptionView = (TextView) findViewById(R.id.description_event_view);
        TextView mLocationView = (TextView) findViewById(R.id.direction_event_view);

        Bundle extras = getIntent().getExtras();
        String name =   getResources().getString(R.string.no_event);
        String day = getResources().getString(R.string.no_day);
        if (extras != null) {
            name = extras.getString("CHUMBOAPP_NAME");
            day = extras.getString("CHUMBOAPP_DATE");
        }
        mNameView.setText(name);
        mDayView.setText(day);

        Cursor mCursor = getContentResolver().query(CalendarProvider.CONTENT_URI, new String[]{CalendarProvider.DESCRIPTION, CalendarProvider.LOCATION},
                CalendarProvider.LOCATION + " LIKE ? AND " + CalendarProvider.EVENT + " = ?", new String[]{day + SEPARATOR + "%", name}, null);
        int index = mCursor.getColumnIndex(CalendarProvider.DESCRIPTION);
        int index1 = mCursor.getColumnIndex(CalendarProvider.LOCATION);
        mCursor.moveToNext();
        String description_aux = mCursor.getString(index);
        String location_aux = mCursor.getString(index1);

        StringTokenizer st = new StringTokenizer(description_aux,SEPARATOR);
        String description = st.nextToken();
        String location = st.nextToken();

        StringTokenizer st1 = new StringTokenizer(location_aux,SEPARATOR);
        String day_start = st1.nextToken();
        String hour = st1.nextToken();

        mHourView.setText(hour);
        mDescriptionView.setText(description);
        mLocationView.setText(location);



    }
}
