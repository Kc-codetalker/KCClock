package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.AppRepository;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarm;

public class AlarmRescheduleService extends LifecycleService {
    public AlarmRescheduleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        AppRepository alarmRepository = new AppRepository(getApplication());

        alarmRepository.getAlarmListLiveData().observe(this, alarms -> {
            for (TimeBasedAlarm a : alarms) {
                if (a.isEnabled()) {
                    Log.d("Reshceduling alarm", a.getName());
                    a.scheduleAlarm(getApplicationContext());
                }
            }
        });

        stopSelf();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}
