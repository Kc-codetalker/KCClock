package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.service.AlarmRescheduleService;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.service.AlarmRingService;

public class TimeBasedAlarmReceiver extends BroadcastReceiver {
    public static final String MONDAY = "ON MONDAY";
    public static final String TUESDAY = "ON TUESDAY";
    public static final String WEDNESDAY = "ON WEDNESDAY";
    public static final String THURSDAY = "ON THURSDAY";
    public static final String FRIDAY = "ON FRIDAY";
    public static final String SATURDAY = "ON SATURDAY";
    public static final String SUNDAY = "ON SUNDAY";
    public static final String RECURRING = "IS RECURRING";
    public static final String NAME = "ALARM NAME";
    public static final String HOUR = "ALARM HOUR";
    public static final String MINUTE = "ALARM MINUTE";
    public static final String VIBRATE = "IS VIBRATE";
    public static final String USE_SOUND = "IS USE SOUND";
    public static final String AUDIO_URI = "AUDIO URI";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            // Reboot all alarm
            Log.d("ACTION_BOOT_COMPLETED", "Mau reschedule alarms.");
            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startAlarmRescheduleService(context);
        }
        else {
            // Start ringing for that alarm
            String toastText = String.format("Alarm Received");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            if (!intent.getBooleanExtra(RECURRING, false) || alarmIsToday(intent)) {
                startAlarmService(context, intent);
            }
        }
    }

    private boolean alarmIsToday(Intent intent) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int today = cal.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                if (intent.getBooleanExtra(MONDAY, false))
                    return true;
                break;
            case Calendar.TUESDAY:
                if (intent.getBooleanExtra(TUESDAY, false))
                    return true;
                break;
            case Calendar.WEDNESDAY:
                if (intent.getBooleanExtra(WEDNESDAY, false))
                    return true;
                break;
            case Calendar.THURSDAY:
                if (intent.getBooleanExtra(THURSDAY, false))
                    return true;
                break;
            case Calendar.FRIDAY:
                if (intent.getBooleanExtra(FRIDAY, false))
                    return true;
                break;
            case Calendar.SATURDAY:
                if (intent.getBooleanExtra(SATURDAY, false))
                    return true;
                break;
            case Calendar.SUNDAY:
                if (intent.getBooleanExtra(SUNDAY, false))
                    return true;
                break;
        }
        return false;
    }

    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmRingService.class);
        intentService.putExtra(NAME, intent.getStringExtra(NAME));
        intentService.putExtra(HOUR, intent.getIntExtra(HOUR, 0));
        intentService.putExtra(MINUTE, intent.getIntExtra(MINUTE, 0));
        intentService.putExtra(VIBRATE, intent.getBooleanExtra(VIBRATE, false));
        intentService.putExtra(USE_SOUND, intent.getBooleanExtra(USE_SOUND, false));
        intentService.putExtra(AUDIO_URI, intent.getStringExtra(AUDIO_URI));
        Log.d("Name start service:", intent.getStringExtra(NAME));
        Log.d("Hour start service:", Integer.toString(intent.getIntExtra(HOUR, 0)));
        Log.d("Minute start service:", Integer.toString(intent.getIntExtra(MINUTE, 0)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void startAlarmRescheduleService(Context context) {
//        Intent intentService = new Intent(context, AlarmRescheduleService.class);
        Log.d("ACTION_BOOT_COMPLETED", "Call service reschedule alarms.");
        AlarmRescheduleService.enqueueWork(context, new Intent());
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                context.startService(intentService);
//            }
//        };
//        thread.start();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(intentService);
//        } else {
//            context.startService(intentService);
//        }
    }
}
