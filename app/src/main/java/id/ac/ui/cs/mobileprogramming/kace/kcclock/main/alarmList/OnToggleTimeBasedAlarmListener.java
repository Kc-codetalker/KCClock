package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarmList;

import android.widget.TextView;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarm;

public interface OnToggleTimeBasedAlarmListener {
    void onToggle(TimeBasedAlarm alarm, TextView alarmTime, TextView alarmName,
                  TextView alarmRecurrence);
}
