package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlarmViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public AlarmViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
