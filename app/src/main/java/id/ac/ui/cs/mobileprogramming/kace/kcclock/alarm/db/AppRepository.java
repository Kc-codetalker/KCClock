package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AppRepository {
    private TimeBasedAlarmDao alarmDao;
    private LiveData<List<TimeBasedAlarm>> alarmListLiveData;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        alarmDao = db.timeBasedAlarmDao();
        alarmListLiveData = alarmDao.getAll();
    }

    public void insert(TimeBasedAlarm alarm) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            alarmDao.insert(alarm);
        });
    }

    public void update(TimeBasedAlarm alarm) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            alarmDao.update(alarm);
        });
    }

    public LiveData<List<TimeBasedAlarm>> getAlarmListLiveData() {
        return alarmListLiveData;
    }
}
