package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarmList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.AppRepository;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.EventBasedAlarm;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarm;

public class AlarmListViewModel extends AndroidViewModel {

    private LiveData<List<TimeBasedAlarm>> timeBasedAlarmsLiveData;
    private LiveData<List<EventBasedAlarm>> eventBasedAlarmsLiveData;

    private AppRepository repo;

    public AlarmListViewModel(@NonNull Application application) {
        super(application);

        repo = new AppRepository(application);
        timeBasedAlarmsLiveData = repo.getTimeBasedAlarmsLiveData();
        eventBasedAlarmsLiveData = repo.getEventBasedAlarmsLiveData();
    }

    public LiveData<List<TimeBasedAlarm>> getTimeBasedAlarmsLiveData() {
        return timeBasedAlarmsLiveData;
    }

    public LiveData<List<EventBasedAlarm>> getEventBasedAlarmsLiveData() {
        return eventBasedAlarmsLiveData;
    }

    public void updateAlarm(TimeBasedAlarm alarm) {
        repo.update(alarm);
    }
    public void updateAlarm(EventBasedAlarm alarm) {
        repo.update(alarm);
    }
}
