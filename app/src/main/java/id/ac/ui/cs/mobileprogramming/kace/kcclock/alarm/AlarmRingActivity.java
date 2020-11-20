package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarm;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.service.AlarmRingService;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.ALARM_TYPE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.AUDIO_URI;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.EVENT;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.HOUR;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.MINUTE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.NAME;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.TIME_BASED_ALARM;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.USE_SOUND;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.VIBRATE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.EventBasedAlarm.EVENT_MAP;

public class AlarmRingActivity extends AppCompatActivity {
    @BindView(R.id.dismissButton) Button dismiss;

    @Nullable
    @BindView(R.id.snoozeButton) Button snooze;
    @Nullable
    @BindView(R.id.alarmName) TextView nameView;
    @Nullable
    @BindView(R.id.alarmTime) TextView timeView;
    @Nullable
    @BindView(R.id.alarmDate) TextView dateView;
    @Nullable
    @BindView(R.id.alarmEvent) TextView eventView;

    private String alarmType;
    private String name;
    private int hour;
    private int minute;
    private boolean isVibrate;
    private boolean useSound;
    private String audioUri;

    private String event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adjustActivityTheme();
        getSupportActionBar().hide();

        Intent intent = getIntent();
        alarmType = intent.getStringExtra(ALARM_TYPE);
        if (alarmType.equals(TIME_BASED_ALARM)) {
            setContentView(R.layout.activity_alarm_ring);
        } else {
            setContentView(R.layout.activity_alarm_ring_event);
        }

        ButterKnife.bind(this);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        this.getIntentExtras(intent, cal);
        this.setUIData(cal);

        dismiss.setOnClickListener(v -> {
            Intent intentService = new Intent(getApplicationContext(), AlarmRingService.class);
            getApplicationContext().stopService(intentService);
            finish();
        });

        if (alarmType.equals(TIME_BASED_ALARM)) {
            snooze.setOnClickListener(v -> {
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                cal.add(Calendar.MINUTE, R.integer.default_snooze_mins);

                int id = new Random().nextInt(Integer.MAX_VALUE);
                TimeBasedAlarm alarm = new TimeBasedAlarm(id, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true,
                        isVibrate, useSound, false, false, false, false, false,
                        false, false, name, audioUri);

                Context ctx = getApplicationContext();
                alarm.scheduleAlarm(ctx);

                Intent intentService = new Intent(ctx, AlarmRingService.class);
                ctx.stopService(intentService);
                finish();
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("onNewIntent", "Masuk on New Intent");
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        this.getIntentExtras(getIntent(), cal);
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

    private void getIntentExtras(Intent intent, Calendar cal) {
//        Intent intent = getIntent();
        isVibrate = intent.getBooleanExtra(VIBRATE, false);
        useSound = intent.getBooleanExtra(USE_SOUND, false);
        audioUri = intent.getStringExtra(AUDIO_URI);

        if (alarmType.equals(TIME_BASED_ALARM)) {
            name = intent.getStringExtra(NAME);
            hour = intent.getIntExtra(HOUR, cal.get(Calendar.HOUR_OF_DAY));
            minute = intent.getIntExtra(MINUTE, cal.get(Calendar.MINUTE));
            Log.d("Name activity:", name);
            Log.d("Hour activity:", Integer.toString(hour));
            Log.d("Minute activity:", Integer.toString(minute));
        } else {
            event = intent.getStringExtra(EVENT);
        }
    }

    private String getCalendarDateStr() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("E, MMM dd yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    private void setUIData(Calendar cal) {
        if (alarmType.equals(TIME_BASED_ALARM)) {
            nameView.setText(name);

            String time = String.format("%02d:%02d", hour, minute);
            timeView.setText(time);

            String date = getCalendarDateStr();
            dateView.setText(date);
        } else {
            int eventTextId = EVENT_MAP.get(event);
            eventView.setText(getString(eventTextId));
        }
    }

}
