package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.db.AppDatabase;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.db.AppRepository;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.db.EventBasedAlarm;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.service.AlarmRescheduleService;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.service.AlarmRingService;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.db.EventBasedAlarm.EVENT_BATTERY_FULL_CHARGE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.application.App.getAppContext;

public class AlarmReceiver extends BroadcastReceiver {
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
    public static final String EVENT = "EVENT";
    public static final String ALARM_TYPE = "ALARM TYPE";
    public static final String TIME_BASED_ALARM = "TIME BASED ALARM";
    public static final String EVENT_BASED_ALARM = "EVENT BASED ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            // Reboot all alarm
            Log.d("ACTION_BOOT_COMPLETED", "Mau reschedule alarms.");
            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startAlarmRescheduleService(context);

        } else if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            // Start ringing for that alarm
            // Are we charging / charged?
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
            boolean isFullCharged = status == BatteryManager.BATTERY_STATUS_FULL;

            boolean alarmEnabled = this.checkAlarmEventEnabled(context, EVENT_BATTERY_FULL_CHARGE);

            if (alarmEnabled && isFullCharged) {
                String toastText = String.format("Event Based Alarm Received");
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
                startAlarmService(context, intent, EVENT_BASED_ALARM, EVENT_BATTERY_FULL_CHARGE);
            }
        }
        else {
            // Start ringing for that alarm
            String toastText = String.format("Alarm Received");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            if (!intent.getBooleanExtra(RECURRING, false) || alarmIsToday(intent)) {
                startAlarmService(context, intent, TIME_BASED_ALARM, null);
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

    private boolean checkAlarmEventEnabled(Context ctx, String event) {
        MutableLiveData<EventBasedAlarm> eventAlarm = new MutableLiveData<>();
        try {
//            AppRepository repo = new AppRepository(ctx);
            AppDatabase.databaseWriteExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    AppRepository repo = new AppRepository(ctx);
                    /* background thread */
                    eventAlarm.postValue(repo.getEventBasedAlarmByEvent(event));
                }
            });
//            EventBasedAlarm eventAlarm = repo.getEventBasedAlarmByEvent(event);
            return eventAlarm.getValue().isEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void startAlarmService(Context context, Intent intent, String alarmType, String eventType) {
        Intent intentService = new Intent(context, AlarmRingService.class);
        switch (alarmType) {
            case TIME_BASED_ALARM:
                intentService.putExtra(ALARM_TYPE, TIME_BASED_ALARM);
                intentService.putExtra(NAME, intent.getStringExtra(NAME));
                intentService.putExtra(HOUR, intent.getIntExtra(HOUR, 0));
                intentService.putExtra(MINUTE, intent.getIntExtra(MINUTE, 0));
                intentService.putExtra(VIBRATE, intent.getBooleanExtra(VIBRATE, false));
                intentService.putExtra(USE_SOUND, intent.getBooleanExtra(USE_SOUND, false));
                intentService.putExtra(AUDIO_URI, intent.getStringExtra(AUDIO_URI));
                Log.d("Name start service:", intent.getStringExtra(NAME));
                Log.d("Hour start service:", Integer.toString(intent.getIntExtra(HOUR, 0)));
                Log.d("Minute start service:", Integer.toString(intent.getIntExtra(MINUTE, 0)));
                break;
            case EVENT_BASED_ALARM:
                AppRepository repo = new AppRepository(getAppContext());
                EventBasedAlarm alarm = repo.getEventBasedAlarmByEvent(eventType);
                if (alarm.isEnabled()) {
                    intentService.putExtra(ALARM_TYPE, EVENT_BASED_ALARM);
                    intentService.putExtra(EVENT, alarm.getEvent());
                    intentService.putExtra(VIBRATE, alarm.isVibrate());
                    intentService.putExtra(USE_SOUND, alarm.isUseSound());
                    intentService.putExtra(AUDIO_URI, alarm.getAudioUri());
                }
                break;
            default:
                return;
        }

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
