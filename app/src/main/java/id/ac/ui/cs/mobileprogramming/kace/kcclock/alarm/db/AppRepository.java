package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class AppRepository {
    private TimeBasedAlarmDao timeBasedAlarmDao;
    private EventBasedAlarmDao eventBasedAlarmDao;
    private LiveData<List<TimeBasedAlarm>> timeBasedAlarmsLiveData;
    private LiveData<List<EventBasedAlarm>> eventBasedAlarmsLiveData;

    public AppRepository(Context ctx) {
        AppDatabase db = AppDatabase.getDatabase(ctx);
        timeBasedAlarmDao = db.timeBasedAlarmDao();
        eventBasedAlarmDao = db.eventBasedAlarmDao();

        timeBasedAlarmsLiveData = timeBasedAlarmDao.getAll();
        eventBasedAlarmsLiveData = eventBasedAlarmDao.getAll();
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

    public LiveData<Alarm> getEventBasedAlarmByEvent(String event) {
        EventBasedAlarm alarm =  eventBasedAlarmDao.getAlarmByEvent(event).getValue();
        MutableLiveData<Alarm> alarmLiveData = new MutableLiveData<>();
        alarmLiveData.setValue(alarm);
        return alarmLiveData;
    }

    public LiveData<List<TimeBasedAlarm>> getTimeBasedAlarmsLiveData() {
        return timeBasedAlarmsLiveData;
    }

    public LiveData<List<EventBasedAlarm>> getEventBasedAlarmsLiveData() {
        return eventBasedAlarmsLiveData;
    }
}
