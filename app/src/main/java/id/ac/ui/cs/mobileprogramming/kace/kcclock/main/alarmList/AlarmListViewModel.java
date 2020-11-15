package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarmList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.AppDatabase;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarm;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarmDao;

public class AlarmListViewModel extends AndroidViewModel {
    //    private MutableLiveData<String> mText;
    private LiveData<List<TimeBasedAlarm>> alarmsLiveData;
    TimeBasedAlarmDao alarmDao;

    public AlarmListViewModel(@NonNull Application application) {
        super(application);

//        alarmRepository = new AlarmRepository(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        alarmDao = db.timeBasedAlarmDao();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            alarmsLiveData = alarmDao.getAll();
        });
    }

    public LiveData<List<TimeBasedAlarm>> getAlarmsLiveData() {
        return alarmsLiveData;
    }

    public void updateAlarm(TimeBasedAlarm alarm) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            alarmDao.update(alarm);
        });
    }

//    public AlarmListViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is alarm fragment");
//    }

//    public LiveData<String> getText() {
//        return mText;
//    }
}
