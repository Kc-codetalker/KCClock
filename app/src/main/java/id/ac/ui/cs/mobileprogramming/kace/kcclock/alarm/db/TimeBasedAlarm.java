package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.AUDIO_URI;
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
    @PrimaryKey
    private int id;

    private int hour, minute;

    @ColumnInfo(name = "is_enabled")
    private boolean isEnabled;

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

    @ColumnInfo(name = "audio_uri")
    private String audioUri;

    /**
     * This is required for Entity to be compiled
     * @param recurring
     */
    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public int getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public boolean isUseSound() {
        return useSound;
    }

    public boolean isOnSunday() {
        return onSunday;
    }

    public boolean isOnMonday() {
        return onMonday;
    }

    public boolean isOnTuesday() {
        return onTuesday;
    }

    public boolean isOnWednesday() {
        return onWednesday;
    }

    public boolean isOnThursday() {
        return onThursday;
    }

    public boolean isOnFriday() {
        return onFriday;
    }

    public boolean isOnSaturday() {
        return onSaturday;
    }

    public String getName() {
        return name;
    }

    public String getAudioUri() {
        return audioUri;
    }

    public TimeBasedAlarm(int id, int hour, int minute, boolean isEnabled, boolean isVibrate, boolean useSound,
                          boolean onSunday, boolean onMonday, boolean onTuesday, boolean onWednesday,
                          boolean onThursday, boolean onFriday, boolean onSaturday, String name, String audioUri) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.isEnabled = isEnabled;
        this.isRecurring = onSunday || onMonday || onTuesday || onWednesday || onThursday || onFriday || onSaturday;
        this.isVibrate = isVibrate;
        this.useSound = useSound;
        this.name = name;
        this.audioUri = audioUri;

        this.onSunday = onSunday;
        this.onMonday = onMonday;
        this.onTuesday = onTuesday;
        this.onWednesday = onWednesday;
        this.onThursday = onThursday;
        this.onFriday = onFriday;
        this.onSaturday = onSaturday;
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
        intent.putExtra(AUDIO_URI, audioUri);
        Log.d("Name:", name);
        Log.d("Hour:", Integer.toString(hour));
        Log.d("Minute:", Integer.toString(minute));

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(ctx, this.id, intent, 0);

        // Create calendar for getting "when next time is"
        Calendar calNextTime = getNextTimeCal();

        // "Send" alarm pendingIntent to alarmManager
        if (!isRecurring) {
            // One-time alarm
            String toastText = String.format("One Time Alarm %s scheduled at %02d:%02d", name, hour, minute);
            Toast.makeText(ctx, toastText, Toast.LENGTH_LONG).show();

            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calNextTime.getTimeInMillis(),
                    alarmPendingIntent
            );
        } else {
            // Recurring alarm
            String toastText = String.format("Recurring Alarm %s scheduled at %02d:%02d", name, hour, minute);
            Toast.makeText(ctx, toastText, Toast.LENGTH_LONG).show();

            final int A_DAY = 24*3600*1000;
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calNextTime.getTimeInMillis(),
                    A_DAY,
                    alarmPendingIntent
            );
        }

        this.isEnabled = true;
    }

    public void disableAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimeBasedAlarmReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        this.isEnabled = false;

        String toastText = String.format("Alarm cancelled for %02d:%02d with id %d", hour, minute, id);
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
    }

    private Calendar getNextTimeCal() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, this.hour);
        cal.set(Calendar.MINUTE, this.minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1 to schedule for "next time"
        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        }
        return cal;
    }

    public String getRecurrenceStr(Context ctx) {
        if (this.isRecurring) {
            ArrayList<String> days = new ArrayList<>();

            if (onSunday){
                days.add(ctx.getString(R.string.short_sunday));
            }
            if (onMonday) {
                days.add(ctx.getString(R.string.short_monday));
            }
            if (onTuesday) {
                days.add(ctx.getString(R.string.short_tuesday));
            }
            if (onWednesday) {
                days.add(ctx.getString(R.string.short_wednesday));
            }
            if (onThursday) {
                days.add(ctx.getString(R.string.short_thursday));
            }
            if (onFriday) {
                days.add(ctx.getString(R.string.short_friday));
            }
            if (onSaturday) {
                days.add(ctx.getString(R.string.short_saturday));
            }

            int dayCount = days.size();
            if (dayCount == 7) return ctx.getString(R.string.every_day);
            else return android.text.TextUtils.join(", ", days);
        }

        Date date = this.getNextTimeCal().getTime();
        DateFormat dateFormat = new SimpleDateFormat("E, MMM dd");
        return dateFormat.format(date);
    }
}
