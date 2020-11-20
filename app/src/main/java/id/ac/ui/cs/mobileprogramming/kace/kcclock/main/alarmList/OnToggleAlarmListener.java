package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarmList;

import android.widget.TextView;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.EventBasedAlarm;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarm;

public interface OnToggleAlarmListener {
    void onToggle(TimeBasedAlarm alarm, TextView alarmTime, TextView alarmName,
                  TextView alarmRecurrence);
    void onToggle(EventBasedAlarm alarm, TextView alarmTime, TextView alarmName,
                  TextView alarmRecurrence);
}
