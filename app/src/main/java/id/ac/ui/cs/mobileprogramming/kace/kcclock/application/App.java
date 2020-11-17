package id.ac.ui.cs.mobileprogramming.kace.kcclock.application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID = "ALARM_NOTIFICATION_CHANNEL";
    public static final String CHANNEL_NAME = "Alarm Notification Channel";

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        createNotificationChannnel();
    }

    private void createNotificationChannnel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public static Context getAppContext(){
        return mContext;
    }
}
