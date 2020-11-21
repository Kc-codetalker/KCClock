package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.db.AppRepository;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.db.TimeBasedAlarm;

public class AlarmRescheduleService extends JobIntentService {

    public static final int JOB_ID = 0x01;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, AlarmRescheduleService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        try {
            AppRepository alarmRepository = new AppRepository(getApplication());
            Log.d("Reschedule alarm", "Preparation now...");

            List<TimeBasedAlarm> alarms = alarmRepository.getTimeBasedAlarmsLiveData().getValue();
            for (TimeBasedAlarm a : alarms) {
                if (a.isEnabled()) {
                    Log.d("Reshceduling alarm", a.getName());
                    a.scheduleAlarm(getApplicationContext());
                }
            }
        } catch (Exception e) {
            Log.d("[Alarm Reschedule]", e.toString());
        }

    }
}

//public class AlarmRescheduleService extends LifecycleService {
//    public AlarmRescheduleService() {
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//
//        AppRepository alarmRepository = new AppRepository(getApplication());
//        Log.d("Reschedule alarm", "Preparation now...");
//
//        alarmRepository.getAlarmListLiveData().observe(this, alarms -> {
//            for (TimeBasedAlarm a : alarms) {
//                if (a.isEnabled()) {
//                    Log.d("Reshceduling alarm", a.getName());
//                    a.scheduleAlarm(getApplicationContext());
//                }
//            }
//        });
//
////        stopSelf();
//        return START_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        super.onBind(intent);
//        return null;
//    }
//}
