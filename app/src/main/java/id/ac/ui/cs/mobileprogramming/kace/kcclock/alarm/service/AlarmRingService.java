package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import android.net.Uri;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.AlarmRingActivity;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.ALARM_TYPE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.AUDIO_URI;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.EVENT;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.EVENT_BASED_ALARM;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.HOUR;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.MINUTE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.NAME;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.TIME_BASED_ALARM;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.USE_SOUND;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.VIBRATE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.db.EventBasedAlarm.EVENT_MAP;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.application.App.CHANNEL_ID;

public class AlarmRingService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    public AlarmRingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.pokemon_x_obtain_item);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String alarmType = intent.getStringExtra(ALARM_TYPE);
        boolean isVibrate = intent.getBooleanExtra(VIBRATE, false);
        boolean useSound = intent.getBooleanExtra(USE_SOUND, false);
        String audioUri = intent.getStringExtra(AUDIO_URI);
        String alarmTitle = "";

        Intent notificationIntent = new Intent(this, AlarmRingActivity.class);
        switch (alarmType) {
            case TIME_BASED_ALARM:
                String name = intent.getStringExtra(NAME);
                int hour = intent.getIntExtra(HOUR, 0);
                int minute = intent.getIntExtra(MINUTE, 0);

                notificationIntent.putExtra(ALARM_TYPE, TIME_BASED_ALARM);
                notificationIntent.putExtra(NAME, name);
                notificationIntent.putExtra(HOUR, hour);
                notificationIntent.putExtra(MINUTE, minute);
                notificationIntent.putExtra(VIBRATE, isVibrate);
                notificationIntent.putExtra(USE_SOUND, useSound);
                notificationIntent.putExtra(AUDIO_URI, audioUri);
                Log.d("Name notif:", name);
                Log.d("Hour notif:", Integer.toString(hour));
                Log.d("Minute notif:", Integer.toString(minute));

                alarmTitle = String.format("%s Alarm", name);
                break;
            case EVENT_BASED_ALARM:
                String event = intent.getStringExtra(EVENT);
                notificationIntent.putExtra(EVENT, event);
                notificationIntent.putExtra(VIBRATE, isVibrate);
                notificationIntent.putExtra(USE_SOUND, useSound);
                notificationIntent.putExtra(AUDIO_URI, audioUri);

                int eventTextId = EVENT_MAP.get(event);
                alarmTitle = String.format("%s Alarm", getString(eventTextId));
                break;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        this.startRinging(isVibrate, useSound, audioUri);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText("Tap to see actions.")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        mediaPlayer.release();
        vibrator.cancel();
    }

    private void startRinging(boolean isVibrate, boolean useSound, String audioUri) {
        if (useSound) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(this, Uri.parse(audioUri));
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
                mediaPlayer = MediaPlayer.create(this, R.raw.pokemon_x_obtain_item);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        }

        if (isVibrate && vibrator.hasVibrator()) {
            Log.d("Ada vibrator", "Seharusnya getar dong");
            Log.d("Hey", Integer.toString(Build.VERSION.SDK_INT));
            Log.d("Hey", Integer.toString(Build.VERSION_CODES.O));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("Android O", "Seharusnya getar dong");
                vibrator.vibrate(VibrationEffect.createOneShot(2000, 255));
            }
            long[] pattern = { 0, 1000, 1000 };
            int[] amplitudes = {0, 255, 0};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern,amplitudes,0));
            } else {
                //deprecated in API 26
                Log.d("OMG", Integer.toString(Build.VERSION.SDK_INT));
                Log.d("OMG", Integer.toString(Build.VERSION_CODES.O));
                vibrator.vibrate(pattern, 0);
            }
        }
    }
}
