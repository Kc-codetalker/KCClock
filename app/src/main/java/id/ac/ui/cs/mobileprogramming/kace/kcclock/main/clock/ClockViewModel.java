package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.clock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClockViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ClockViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fetching weather...");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String text) {
        mText.setValue(text);
    }
}
