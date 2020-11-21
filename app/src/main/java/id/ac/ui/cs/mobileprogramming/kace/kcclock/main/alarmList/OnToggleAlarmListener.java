package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarmList;

import android.widget.TextView;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.db.EventBasedAlarm;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.db.TimeBasedAlarm;

public interface OnToggleAlarmListener {
    void onToggle(TimeBasedAlarm alarm, TextView alarmTime, TextView alarmName,
                  TextView alarmRecurrence);
    void onToggle(EventBasedAlarm alarm, TextView alarmTime, TextView alarmName,
                  TextView alarmRecurrence);
}
