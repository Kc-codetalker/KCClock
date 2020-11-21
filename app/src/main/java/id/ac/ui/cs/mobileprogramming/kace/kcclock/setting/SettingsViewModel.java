package id.ac.ui.cs.mobileprogramming.kace.kcclock.setting;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.db.AppRepository;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.db.Setting;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.db.Setting.SETTING_LANG;

public class SettingsViewModel extends AndroidViewModel {
    private MutableLiveData<String> selectedLanguageValue = new MutableLiveData<>();

    private LiveData<List<Setting>> settingsLiveData = new MutableLiveData<>();
    private AppRepository repo;

    public SettingsViewModel(@NonNull Application application) {
        super(application);

        repo = new AppRepository(application);

        try {
            settingsLiveData = repo.getSettingsLiveData();
        } catch (Exception e) {
            Log.d("SettingsViewModel", e.toString());
            e.printStackTrace();
            String[] langArr = application.getResources().getStringArray(R.array.language_values);
            selectedLanguageValue.setValue(langArr[0]);
        }
    }

    public LiveData<List<Setting>> getSettingsLiveData() {
        return this.settingsLiveData;
    }

    public LiveData<String> getSelectedLanguageValue() {
        return selectedLanguageValue;
    }

    public void setSelectedLanguageValue(String value) {
        this.selectedLanguageValue.setValue(value);
    }

    public void saveSettings() {
        Setting langSetting = new Setting(SETTING_LANG, selectedLanguageValue.getValue());
        repo.insert(langSetting);
    }
}
