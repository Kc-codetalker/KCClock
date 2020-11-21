package id.ac.ui.cs.mobileprogramming.kace.kcclock.setting;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.db.Setting;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.db.Setting.SETTING_LANG;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.spinnerLanguage) Spinner languageSpinner;

    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);

        this.adjustActivityTheme();
        setContentView(R.layout.settings_activity);
        ButterKnife.bind(this);

        String[] langArr = getResources().getStringArray(R.array.language_entries);
        ArrayAdapter<String> eventAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, langArr);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(eventAdapter);

        setUIData();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_settings);
        }
    }

    @Override
    public void onStop() {
        this.saveStateToViewModel();
        this.viewModel.saveSettings();
        super.onStop();
    }

    private void adjustActivityTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                setTheme(R.style.LightTheme);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                setTheme(R.style.DarkTheme);
                break;
        }
    }

    private void setUIData() {
        try {
            String[] langArr = getResources().getStringArray(R.array.language_values);
            List<String> langLst = Arrays.asList(langArr);

            viewModel.getSettingsLiveData().observe(this, settings -> {
                for (Setting s : settings) {
                    switch (s.getSettingName()) {
                        case SETTING_LANG:
                            String value = s.getValue();
                            languageSpinner.setSelection(langLst.indexOf(value));
                            break;
                    }
                }
            });
        } catch (NullPointerException e) {
            Log.d("Settings SetUI", e.toString());
            e.printStackTrace();
        }
    }

    private void saveStateToViewModel() {
        String[] langArr = getResources().getStringArray(R.array.language_values);
        List<String> langLst = Arrays.asList(langArr);

        int langPos = languageSpinner.getSelectedItemPosition();
        if (langPos == -1) langPos = 0;

        viewModel.setSelectedLanguageValue(langLst.get(langPos));
    }
}