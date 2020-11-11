package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm;

import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
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
import android.widget.TimePicker;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;

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

    private TimeBasedAlarmViewModel alarmViewModel;

    public static TimeBasedAlarmFragment newInstance() {
        return new TimeBasedAlarmFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_based_alarm, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        alarmViewModel = ViewModelProviders.of(this).get(TimeBasedAlarmViewModel.class);
        setUIData();
    }

    @Override
    public void onStop() {
        this.saveStateToViewModel();
        super.onStop();
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
        } catch (NullPointerException e) {
            Log.d("TimeBasedAlarmViewModel", e.toString());
        }
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
        int hour = TimePickerUtil.getTimePickerHour(this.timePicker);
        int minute = TimePickerUtil.getTimePickerMinute(this.timePicker);
        String alarmName = this.nameField.getText().toString();
        boolean isVibrate = this.vibrateCheckBox.isChecked();
        boolean isUseSound = this.soundCheckBox.isChecked();
        boolean isRingSun = this.toggleSun.isChecked();
        boolean isRingMon = this.toggleMon.isChecked();
        boolean isRingTue = this.toggleTue.isChecked();
        boolean isRingWed = this.toggleWed.isChecked();
        boolean isRingThu = this.toggleThu.isChecked();
        boolean isRingFri = this.toggleFri.isChecked();
        boolean isRingSat = this.toggleSat.isChecked();
        this.saveStateToViewModel();
        Log.d("Hour:", Integer.toString(hour));
        Log.d("Minute:", Integer.toString(minute));
        Log.d("Name:", alarmName);
        Log.d("Vibrate:", Boolean.toString(isVibrate));
        Log.d("Sound:", Boolean.toString(isUseSound));
    }

}
