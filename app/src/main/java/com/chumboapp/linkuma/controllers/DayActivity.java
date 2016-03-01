package com.chumboapp.linkuma.controllers;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chumboapp.linkuma.R;
import com.tyczj.extendedcalendarview.CalendarProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class DayActivity extends AppCompatActivity {

    private ListView mlistView = null;
    private TextView mDayView = null;
    private static final String SEPARATOR = "@@++@@";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        // TODO Ocultar barra de título

        Bundle extras = getIntent().getExtras();
        String day = "no day";
        if (extras != null) {
            day = extras.getString("CHUMBOAPP_DAY");
        }
        Log.i("Day",day);
        mDayView = (TextView) findViewById(R.id.day_text_view);
        mDayView.setText(day);

        ArrayList<String> values = new ArrayList<String>();
        mlistView = (ListView) findViewById(R.id.day_list_view);
        Cursor mCursor = getContentResolver().query(CalendarProvider.CONTENT_URI, new String[]{CalendarProvider.EVENT},
                CalendarProvider.LOCATION + " LIKE ?", new String[]{day + SEPARATOR + "%"}, null);
        String empty_day = getResources().getString(R.string.empty_day);
        if (mCursor == null) {

            Toast.makeText(DayActivity.this, empty_day,
                    Toast.LENGTH_LONG).show();
        } else {
            int index = mCursor.getColumnIndex(CalendarProvider.EVENT);
            while (mCursor.moveToNext()) {
                // Gets the value from the column.
                String newWord = mCursor.getString(index);
                Log.e("CHUMBOAPP", newWord);
                values.add(newWord);
            }
            if (values.size() == 0) {
                Toast.makeText(DayActivity.this, empty_day,
                        Toast.LENGTH_LONG).show();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);

            // Assign adapter to ListView
            mlistView.setAdapter(adapter);

            // ListView Item Click Listener
            mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // ListView Clicked item value
                    String itemValue = (String) mlistView.getItemAtPosition(position);
                    TextView t = (TextView) findViewById(R.id.day_text_view);
                    String date = (String) t.getText();
                    Intent intent = new Intent(DayActivity.this, EventActivity.class);
                    intent.putExtra("CHUMBOAPP_NAME", itemValue);
                    intent.putExtra("CHUMBOAPP_DATE", date);
                    startActivity(intent);

                }
            });
        }
    }
}