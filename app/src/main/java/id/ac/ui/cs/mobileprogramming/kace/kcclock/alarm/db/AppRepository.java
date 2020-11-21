package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AppRepository {
    private TimeBasedAlarmDao timeBasedAlarmDao;
    private EventBasedAlarmDao eventBasedAlarmDao;
    private SettingDao settingDao;
    private LiveData<List<TimeBasedAlarm>> timeBasedAlarmsLiveData;
    private LiveData<List<EventBasedAlarm>> eventBasedAlarmsLiveData;
    private LiveData<List<Setting>> settingsLiveData;

    public AppRepository(Context ctx) {
        AppDatabase db = AppDatabase.getDatabase(ctx);
        timeBasedAlarmDao = db.timeBasedAlarmDao();
        eventBasedAlarmDao = db.eventBasedAlarmDao();
        settingDao = db.settingDao();

        timeBasedAlarmsLiveData = timeBasedAlarmDao.getAll();
        eventBasedAlarmsLiveData = eventBasedAlarmDao.getAll();
        settingsLiveData = settingDao.getAll();
    }

    public void insert(TimeBasedAlarm alarm) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            timeBasedAlarmDao.insert(alarm);
        });
    }

    public void insert(EventBasedAlarm alarm) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventBasedAlarmDao.insert(alarm);
        });
    }

    public void insert(Setting setting) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            settingDao.insert(setting);
        });
    }

    public void update(TimeBasedAlarm alarm) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            timeBasedAlarmDao.update(alarm);
        });
    }

    public void update(EventBasedAlarm alarm) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventBasedAlarmDao.update(alarm);
        });
    }

    public void update(Setting setting) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            settingDao.update(setting);
        });
    }

    public LiveData<EventBasedAlarm> getEventBasedAlarmByEvent(String event) {
        return eventBasedAlarmDao.getAlarmByEvent(event);
    }

    public LiveData<Setting> getSettingByName(String name) {
        return settingDao.getSettingByName(name);
    }

    public LiveData<List<TimeBasedAlarm>> getTimeBasedAlarmsLiveData() {
        return timeBasedAlarmsLiveData;
    }

    public LiveData<List<EventBasedAlarm>> getEventBasedAlarmsLiveData() {
        return eventBasedAlarmsLiveData;
    }

    public LiveData<List<Setting>> getSettingsLiveData() {
        return settingsLiveData;
    }
}
