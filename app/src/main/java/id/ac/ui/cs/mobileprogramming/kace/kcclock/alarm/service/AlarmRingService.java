package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.AlarmRingActivity;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.HOUR;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.MINUTE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.NAME;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.application.App.CHANNEL_ID;

public class AlarmRingService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

//    private Looper serviceLooper;
//    private ServiceHandler serviceHandler;

    public AlarmRingService() {
    }

    // Handler that receives messages from the thread
//    private final class ServiceHandler extends Handler {
//        public ServiceHandler(Looper looper) {
//            super(looper);
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            // Normally we would do some work here, like download a file.
//            // For our sample, we just sleep for 5 seconds.
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                // Restore interrupt status.
//                Thread.currentThread().interrupt();
//            }
//            // Stop the service using the startId, so that we don't stop
//            // the service in the middle of handling another job
//            stopSelf(msg.arg1);
//        }
//    }

    @Override
    public void onCreate() {
        super.onCreate();

//        HandlerThread thread = new HandlerThread("ServiceStartArguments",
//                Process.THREAD_PRIORITY_BACKGROUND);
//        thread.start();
//
//        // Get the HandlerThread's Looper and use it for our Handler
//        serviceLooper = thread.getLooper();
//        serviceHandler = new ServiceHandler(serviceLooper);

        mediaPlayer = MediaPlayer.create(this, R.raw.pokemon_x_obtain_item);
        mediaPlayer.setLooping(true);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name = intent.getStringExtra(NAME);
        int hour = intent.getIntExtra(HOUR, 0);
        int minute = intent.getIntExtra(MINUTE, 0);

        Intent notificationIntent = new Intent(this, AlarmRingActivity.class);
        notificationIntent.putExtra(NAME, name);
        notificationIntent.putExtra(HOUR, hour);
        notificationIntent.putExtra(MINUTE, minute);
        Log.d("Name notif:", name);
        Log.d("Hour notif:", Integer.toString(hour));
        Log.d("Minute notif:", Integer.toString(minute));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        this.startRinging();

        String alarmTitle = String.format("%s Alarm", name);

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
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        mediaPlayer.release();
        vibrator.cancel();
    }

    private void startRinging() {
        mediaPlayer.start();

        if (vibrator.hasVibrator()) {
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
