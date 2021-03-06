package com.example.thealarmmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton btnToggle = findViewById(R.id.btn_toggle);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);

        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);
        btnToggle.setChecked(alarmUp);

        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String toastMessage;
                if(b){
                    long repeatInterval = 1000L;
                    long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;
                    if (alarmManager != null) {
                        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                        triggerTime, repeatInterval, notifyPendingIntent);
                    }
                    toastMessage = "B???t b??o th???c!";
                } else {
                    mNotificationManager.cancelAll();
                    if (alarmManager != null) {
                        alarmManager.cancel(notifyPendingIntent);
                    }
                    toastMessage = "B??o th???c ???? t???t!";
                }
                Toast.makeText(MainActivity.this, toastMessage,Toast.LENGTH_SHORT).show();
            }
        });

        createNotificationChannel();
    }
    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,"Th??ng b??o B??o th???c",
                            NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("????y l?? m?? t??? c???a k??nh th??ng b??o n??y");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }
    //????? ?????m b???o r???ng c??c c???nh b??o th???c thi, b???n c?? th??? s??? d???ng setAndAllowWhileIdle () ho???c setExactAndAllowWhileIdle ().
    //B???n s??? d???ng c???nh b??o setInexactRepeating () v?? s??? d???ng th???i gian kh??ng ch??nh x??c s??? ti???t ki???m t??i nguy??n h??n,
    // cho ph??p h??? th???ng g???p c??c c???nh b??o t??? c??c ???ng d???ng kh??c nhau l???i v???i nhau.
}