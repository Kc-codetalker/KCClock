package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.FRIDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.HOUR;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.MINUTE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.MONDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.NAME;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.RECURRING;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.SATURDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.SUNDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.THURSDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.TUESDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.USE_SOUND;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.VIBRATE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.WEDNESDAY;

@Entity(tableName = "time_based_alarm")
public class TimeBasedAlarm {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int hour, minute;

    @ColumnInfo(name = "has_started")
    private boolean hasStarted;

    @ColumnInfo(name = "is_recurring")
    private boolean isRecurring;

    @ColumnInfo(name = "is_vibrate")
    private boolean isVibrate;

    @ColumnInfo(name = "use_sound")
    private boolean useSound;

    @ColumnInfo(name = "on_sunday")
    private boolean onSunday;
    @ColumnInfo(name = "on_monday")
    private boolean onMonday;
    @ColumnInfo(name = "on_tuesday")
    private boolean onTuesday;
    @ColumnInfo(name = "on_wednesday")
    private boolean onWednesday;
    @ColumnInfo(name = "on_thursday")
    private boolean onThursday;
    @ColumnInfo(name = "on_friday")
    private boolean onFriday;
    @ColumnInfo(name = "on_saturday")
    private boolean onSaturday;

    private String name;

    public TimeBasedAlarm(int hour, int minute, boolean started, boolean vibrate, boolean sound,
                          boolean sunday, boolean monday, boolean tuesday, boolean wednesday,
                          boolean thursday, boolean friday, boolean saturday, String name) {
        this.hour = hour;
        this.minute = minute;
        this.hasStarted = started;
        this.isRecurring = sunday || monday || tuesday || wednesday || thursday || friday || saturday;
        this.isVibrate = vibrate;
        this.useSound = sound;
        this.name = name;

        this.onSunday = sunday;
        this.onMonday = monday;
        this.onTuesday = tuesday;
        this.onWednesday = wednesday;
        this.onThursday = thursday;
        this.onFriday = friday;
        this.onSaturday = saturday;
    }

    public void logging() {
        Log.d("Alarm ID nih:", Integer.toString(this.id));
    }

    public void scheduleAlarm(Context ctx) {

        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(ctx, TimeBasedAlarmReceiver.class);

        // Set values that is needed by later broadcast receiver
        intent.putExtra(RECURRING, isRecurring);
        intent.putExtra(SUNDAY, onSunday);
        intent.putExtra(MONDAY, onMonday);
        intent.putExtra(TUESDAY, onTuesday);
        intent.putExtra(WEDNESDAY, onWednesday);
        intent.putExtra(THURSDAY, onThursday);
        intent.putExtra(FRIDAY, onFriday);
        intent.putExtra(SATURDAY, onSaturday);
        intent.putExtra(NAME, name);
        intent.putExtra(HOUR, hour);
        intent.putExtra(MINUTE, minute);
        intent.putExtra(VIBRATE, isVibrate);
        intent.putExtra(USE_SOUND, useSound);
        Log.d("Name:", name);
        Log.d("Hour:", Integer.toString(hour));
        Log.d("Minute:", Integer.toString(minute));

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(ctx, this.id, intent, 0);

        // Create calendar for getting "when next time is"
        Calendar calNextTime = Calendar.getInstance();
        calNextTime.setTimeInMillis(System.currentTimeMillis());
        calNextTime.set(Calendar.HOUR_OF_DAY, this.hour);
        calNextTime.set(Calendar.MINUTE, this.minute);
        calNextTime.set(Calendar.SECOND, 0);
        calNextTime.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1 to schedule for "next time"
        if (calNextTime.getTimeInMillis() <= System.currentTimeMillis()) {
            calNextTime.set(Calendar.DAY_OF_MONTH, calNextTime.get(Calendar.DAY_OF_MONTH) + 1);
        }

        // "Send" alarm pendingIntent to alarmManager
        if (!isRecurring) {
            // One-time alarm
//            String toastText = null;
//            try {
//                toastText = String.format("One Time Alarm %s scheduled for %s at %02d:%02d", title, DayUtil.toDay(calendar.get(Calendar.DAY_OF_WEEK)), hour, minute, id);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            Toast.makeText(ctx, toastText, Toast.LENGTH_LONG).show();

            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calNextTime.getTimeInMillis(),
                    alarmPendingIntent
            );
        } else {
            // Recurring alarm
//            String toastText = String.format("Recurring Alarm %s scheduled for %s at %02d:%02d", title, getRecurringDaysText(), hour, minute, alarmId);
//            Toast.makeText(ctx, toastText, Toast.LENGTH_LONG).show();

            final int A_DAY = 24*3600*1000;
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calNextTime.getTimeInMillis(),
                    A_DAY,
                    alarmPendingIntent
            );
        }

        this.hasStarted = true;
    }
}
