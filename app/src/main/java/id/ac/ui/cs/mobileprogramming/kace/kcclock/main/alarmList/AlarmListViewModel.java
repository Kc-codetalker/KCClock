package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarmList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlarmListViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public AlarmListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is alarm fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
