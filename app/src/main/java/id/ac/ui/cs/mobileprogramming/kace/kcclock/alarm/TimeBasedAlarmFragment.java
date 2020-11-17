package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarm;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.util.TimePickerUtil;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.FRIDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.HOUR;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.MINUTE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.MONDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.NAME;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.SATURDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.SUNDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.THURSDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.TUESDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.USE_SOUND;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.VIBRATE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.WEDNESDAY;

public class TimeBasedAlarmFragment extends Fragment {
    @BindView(R.id.alarmTimePicker) TimePicker timePicker;
    @BindView(R.id.alarmNameField) EditText nameField;
    @BindView(R.id.checkBoxVibrate) CheckBox vibrateCheckBox;
    @BindView(R.id.checkBoxSound) CheckBox soundCheckBox;
    @BindView(R.id.btnDaySelectSun) ToggleButton toggleSun;
    @BindView(R.id.btnDaySelectMon) ToggleButton toggleMon;
    @BindView(R.id.btnDaySelectTue) ToggleButton toggleTue;
    @BindView(R.id.btnDaySelectWed) ToggleButton toggleWed;
    @BindView(R.id.btnDaySelectThu) ToggleButton toggleThu;
    @BindView(R.id.btnDaySelectFri) ToggleButton toggleFri;
    @BindView(R.id.btnDaySelectSat) ToggleButton toggleSat;
    @BindView(R.id.alarmDayDesc) TextView alarmDesc;

    private TimeBasedAlarmViewModel alarmViewModel;

    public static TimeBasedAlarmFragment newInstance() {
        return new TimeBasedAlarmFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_based_alarm, container, false);
        ButterKnife.bind(this, view);
        toggleSun.setOnClickListener(v -> onDayToggle());
        toggleMon.setOnClickListener(v -> onDayToggle());
        toggleTue.setOnClickListener(v -> onDayToggle());
        toggleWed.setOnClickListener(v -> onDayToggle());
        toggleThu.setOnClickListener(v -> onDayToggle());
        toggleFri.setOnClickListener(v -> onDayToggle());
        toggleSat.setOnClickListener(v -> onDayToggle());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        alarmViewModel = ViewModelProviders.of(this).get(TimeBasedAlarmViewModel.class);
        Intent intent = getActivity().getIntent();
        updateViewModelWithIntent(intent);
        setUIData();
    }

    @Override
    public void onStop() {
        this.saveStateToViewModel();
        super.onStop();
    }

    private void updateViewModelWithIntent(Intent intent) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);
        try {
            alarmViewModel.setId(intent.getIntExtra("id", 0));
            alarmViewModel.setHour(intent.getIntExtra(HOUR, currentHour));
            alarmViewModel.setMinute(intent.getIntExtra(MINUTE, currentMinute));
            alarmViewModel.setAlarmName(intent.getStringExtra(NAME));
            alarmViewModel.setIsVibrate(intent.getBooleanExtra(VIBRATE, false));
            alarmViewModel.setIsUseSound(intent.getBooleanExtra(USE_SOUND, false));
            alarmViewModel.setIsRingSun(intent.getBooleanExtra(SUNDAY, false));
            alarmViewModel.setIsRingMon(intent.getBooleanExtra(MONDAY, false));
            alarmViewModel.setIsRingTue(intent.getBooleanExtra(TUESDAY, false));
            alarmViewModel.setIsRingWed(intent.getBooleanExtra(WEDNESDAY, false));
            alarmViewModel.setIsRingThu(intent.getBooleanExtra(THURSDAY, false));
            alarmViewModel.setIsRingFri(intent.getBooleanExtra(FRIDAY, false));
            alarmViewModel.setIsRingSat(intent.getBooleanExtra(SATURDAY, false));
        } catch (Exception e) {
            Log.d("Exception:", e.toString());
        }
    }

    private void setUIData() {
        try {
            alarmViewModel.getHour().observe(this, hour ->
                    TimePickerUtil.setTimePickerHour(timePicker, hour));
            alarmViewModel.getMinute().observe(this, min ->
                    TimePickerUtil.setTimePickerMinute(timePicker, min));
            alarmViewModel.getAlarmName().observe(this, name -> nameField.setText(name));
            alarmViewModel.getIsVibrate().observe(this, is -> vibrateCheckBox.setChecked(is));
            alarmViewModel.getIsUseSound().observe(this, is -> soundCheckBox.setChecked(is));
            alarmViewModel.getIsRingSun().observe(this, is -> toggleSun.setChecked(is));
            alarmViewModel.getIsRingMon().observe(this, is -> toggleMon.setChecked(is));
            alarmViewModel.getIsRingTue().observe(this, is -> toggleTue.setChecked(is));
            alarmViewModel.getIsRingWed().observe(this, is -> toggleWed.setChecked(is));
            alarmViewModel.getIsRingThu().observe(this, is -> toggleThu.setChecked(is));
            alarmViewModel.getIsRingFri().observe(this, is -> toggleFri.setChecked(is));
            alarmViewModel.getIsRingSat().observe(this, is -> toggleSat.setChecked(is));
            alarmViewModel.getAlarmDayDesc().observe(this, desc -> alarmDesc.setText(desc));
        } catch (NullPointerException e) {
            Log.d("TimeBasedAlarmViewModel", e.toString());
        }
    }

    private void setAlarmDescText() {
        Activity activity = getActivity();
        ArrayList<String> days = new ArrayList<>();
        String desc = activity.getString(R.string.next_time);

        if (toggleSun.isChecked()){
            days.add(activity.getString(R.string.short_sunday));
        }
        if (toggleMon.isChecked()) {
            days.add(activity.getString(R.string.short_monday));
        }
        if (toggleTue.isChecked()) {
            days.add(activity.getString(R.string.short_tuesday));
        }
        if (toggleWed.isChecked()) {
            days.add(activity.getString(R.string.short_wednesday));
        }
        if (toggleThu.isChecked()) {
            days.add(activity.getString(R.string.short_thursday));
        }
        if (toggleFri.isChecked()) {
            days.add(activity.getString(R.string.short_friday));
        }
        if (toggleSat.isChecked()) {
            days.add(activity.getString(R.string.short_saturday));
        }

        int dayCount = days.size();
        if (dayCount == 7) desc = activity.getString(R.string.every_day);
        else if (dayCount > 0) desc = activity.getString(R.string.every)
                + " "
                + android.text.TextUtils.join(", ", days);

        alarmViewModel.setAlarmDayDesc(desc);
    }

    public void onDayToggle() {
        setAlarmDescText();
    }

    private void saveStateToViewModel() {
        alarmViewModel.setAlarmName(nameField.getText().toString());
        alarmViewModel.setHour(TimePickerUtil.getTimePickerHour(timePicker));
        alarmViewModel.setMinute(TimePickerUtil.getTimePickerMinute(timePicker));
        alarmViewModel.setIsVibrate(vibrateCheckBox.isChecked());
        alarmViewModel.setIsUseSound(soundCheckBox.isChecked());
        alarmViewModel.setIsRingSun(toggleSun.isChecked());
        alarmViewModel.setIsRingMon(toggleMon.isChecked());
        alarmViewModel.setIsRingTue(toggleTue.isChecked());
        alarmViewModel.setIsRingWed(toggleWed.isChecked());
        alarmViewModel.setIsRingThu(toggleThu.isChecked());
        alarmViewModel.setIsRingFri(toggleFri.isChecked());
        alarmViewModel.setIsRingSat(toggleSat.isChecked());
    }

    public void saveAlarm() {
        this.saveStateToViewModel();
        TimeBasedAlarm alarm = alarmViewModel.createAlarm();
        alarmViewModel.scheduleAlarm(getContext(), alarm);
    }

}
