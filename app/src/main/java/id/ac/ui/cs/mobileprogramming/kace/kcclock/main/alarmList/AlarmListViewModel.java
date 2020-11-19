package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarmList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.AppRepository;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarm;

public class AlarmListViewModel extends AndroidViewModel {

    private LiveData<List<TimeBasedAlarm>> alarmsLiveData;

    private AppRepository repo;

    public AlarmListViewModel(@NonNull Application application) {
        super(application);

        repo = new AppRepository(application);
        alarmsLiveData = repo.getAlarmListLiveData();
    }

    public LiveData<List<TimeBasedAlarm>> getAlarmsLiveData() {
        return alarmsLiveData;
    }

    public void updateAlarm(TimeBasedAlarm alarm) {
        repo.update(alarm);
    }
}
