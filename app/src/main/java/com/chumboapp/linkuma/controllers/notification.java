package com.chumboapp.linkuma.controllers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.chumboapp.linkuma.R;

/**
 * Created by MANOLO on 11/01/2016.
 */
public class notification extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(getApplicationContext(),DayActivity.class);
        i.putExtra("CHUMBOAPP_DAY", "04/01/2016");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,0);

        Notification mNotification = new Notification.Builder(this).
                setContentTitle("Nuevos eventos").
                setContentText("¡Echalé un vistazo a tus eventos de mañana!").
                setSmallIcon(R.drawable.logo_link_small).
                setContentIntent(pendingIntent).build();

        nm.notify(1,mNotification);
    }
}
