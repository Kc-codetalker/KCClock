package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;

public class AlarmDetailActivityViewModel extends ViewModel {
    private MutableLiveData<Integer> selectedAlarmId = new MutableLiveData<>();

    public AlarmDetailActivityViewModel() {
        selectedAlarmId.setValue(R.id.radioButtonTimeBased);
    }

    public LiveData<Integer> getSelectedAlarm() {
        return selectedAlarmId;
    }

    public void setSelectedAlarm(int id) {
        this.selectedAlarmId.setValue(id);
    }
}
