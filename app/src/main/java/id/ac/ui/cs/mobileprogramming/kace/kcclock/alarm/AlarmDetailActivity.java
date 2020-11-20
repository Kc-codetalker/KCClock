package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.ALARM_TYPE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.EVENT_BASED_ALARM;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.TIME_BASED_ALARM;

public class AlarmDetailActivity extends AppCompatActivity {
    @BindView(R.id.alarmTypeRadioGroup) RadioGroup alarmTypeRadioGroup;
    @BindView(R.id.radioButtonTimeBased) RadioButton timeBasedRadio;

    private FragmentManager fragManager;
    private AlarmDetailActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.adjustActivityTheme();

        setContentView(R.layout.activity_alarm_detail);
        fragManager = getSupportFragmentManager();
        ButterKnife.bind(this);

        this.viewModel = ViewModelProviders.of(this).get(AlarmDetailActivityViewModel.class);
        Intent intent = getIntent();
        updateViewModelWithIntent(intent);
        setUIData();

        try
        {
            this.getSupportActionBar().setTitle(R.string.appbar_alarm_detail);
        }
        catch (NullPointerException e){}
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

    private void updateViewModelWithIntent(Intent intent) {
        try {
            String alarmType = intent.getStringExtra(ALARM_TYPE);
            switch (alarmType) {
                case TIME_BASED_ALARM:
                    viewModel.setSelectedAlarm(R.id.radioButtonTimeBased);
                    break;
                case EVENT_BASED_ALARM:
                    viewModel.setSelectedAlarm(R.id.radioButtonEventBased);
                    break;
            }
        } catch (Exception e) {
            Log.d("Exception:", e.toString());
        }
    }

    private void setUIData() {
        this.viewModel.getSelectedAlarm().observe(this, id -> {
            switch(id) {
                case R.id.radioButtonTimeBased:
                    this.alarmTypeRadioGroup.check(R.id.radioButtonTimeBased);
                    setSectionVisibility(R.id.sectionEventBased, View.GONE);
                    setSectionVisibility(R.id.sectionTimeBased, View.VISIBLE);
                    setRadioButtonText(R.id.radioButtonEventBased, false);
                    setRadioButtonText(R.id.radioButtonTimeBased, true);
                    break;
                case R.id.radioButtonEventBased:
                    this.alarmTypeRadioGroup.check(R.id.radioButtonEventBased);
                    setSectionVisibility(R.id.sectionTimeBased, View.GONE);
                    setSectionVisibility(R.id.sectionEventBased, View.VISIBLE);
                    setRadioButtonText(R.id.radioButtonTimeBased, false);
                    setRadioButtonText(R.id.radioButtonEventBased, true);
                    break;
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButtonTimeBased:
                if (checked) {
                    setSectionVisibility(R.id.sectionEventBased, View.GONE);
                    setSectionVisibility(R.id.sectionTimeBased, View.VISIBLE);
                    setRadioButtonText(R.id.radioButtonEventBased, false);
                    setRadioButtonText(R.id.radioButtonTimeBased, true);
                    viewModel.setSelectedAlarm(R.id.radioButtonTimeBased);
                }
                break;
            case R.id.radioButtonEventBased:
                if (checked) {
                    setSectionVisibility(R.id.sectionTimeBased, View.GONE);
                    setSectionVisibility(R.id.sectionEventBased, View.VISIBLE);
                    setRadioButtonText(R.id.radioButtonTimeBased, false);
                    setRadioButtonText(R.id.radioButtonEventBased, true);
                    viewModel.setSelectedAlarm(R.id.radioButtonEventBased);
                }
                break;
        }
    }

    private void setSectionVisibility(int id, int value) {
        View sectionLayout = findViewById(id);
        sectionLayout.setVisibility(value);
    }

    private void setRadioButtonText(int id, boolean isActive) {
        RadioButton radio = findViewById(id);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        if (isActive)
            theme.resolveAttribute(R.attr.activeTextColor, typedValue, true);
        else
            theme.resolveAttribute(R.attr.inactiveTextColor, typedValue, true);
        @ColorInt int color = typedValue.data;

        radio.setTextColor(color);
    }

    public void onCancelButtonClicked(View view) {
        finish();
    }

    public void onSaveButtonClicked(View view) {
        if (this.timeBasedRadio.isChecked()) {
            TimeBasedAlarmFragment fragment = (TimeBasedAlarmFragment) fragManager.findFragmentById(R.id.sectionTimeBased);
            try {
                fragment.saveAlarm();
            } catch (NullPointerException e) {
                Log.d("[Exception] saveAlarm:", e.toString());
                e.printStackTrace();
            }
        } else {
            Log.d("Event based selected!", "Not implemented yet!!");
        }
        finish();
    }
}
