package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarm;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.service.AlarmRingService;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.HOUR;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.MINUTE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.NAME;

public class AlarmRingActivity extends AppCompatActivity {
    @BindView(R.id.dismissButton) Button dismiss;
    @BindView(R.id.snoozeButton) Button snooze;
    @BindView(R.id.alarmName) TextView nameView;
    @BindView(R.id.alarmTime) TextView timeView;
    @BindView(R.id.alarmDate) TextView dateView;

    private String name;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adjustActivityTheme();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_alarm_ring);

        ButterKnife.bind(this);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        this.getIntentExtras(cal);
        this.setUIData(cal);

        dismiss.setOnClickListener(v -> {
            Intent intentService = new Intent(getApplicationContext(), AlarmRingService.class);
            getApplicationContext().stopService(intentService);
            finish();
        });

        snooze.setOnClickListener(v -> {
            cal.add(Calendar.MINUTE, R.integer.default_snooze_mins);

            TimeBasedAlarm alarm = new TimeBasedAlarm(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true,
                    false, false, false, false, false, false,
                    false, false, name);

            Context ctx = getApplicationContext();
            alarm.scheduleAlarm(ctx);

            Intent intentService = new Intent(ctx, AlarmRingService.class);
            ctx.stopService(intentService);
            finish();
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("onNewIntent", "Masuk on New Intent");
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one


        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        this.getIntentExtras(cal);
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

    private String getCalendarDateStr() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("E, MMM dd yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    private void getIntentExtras(Calendar cal) {
        Intent intent = getIntent();
        name = intent.getStringExtra(NAME);
        hour = intent.getIntExtra(HOUR, cal.get(Calendar.HOUR_OF_DAY));
        minute = intent.getIntExtra(MINUTE, cal.get(Calendar.MINUTE));
        Log.d("Name activity:", name);
        Log.d("Hour activity:", Integer.toString(hour));
        Log.d("Minute activity:", Integer.toString(minute));
    }

    private void setUIData(Calendar cal) {
        nameView.setText(name);

        String hourStr = Integer.toString(hour);
        String minuteStr = Integer.toString(minute);
        if (hourStr.length() == 1) {
            hourStr = "0" + hour;
        }
        if (minuteStr.length() == 1) {
            minuteStr = "0" + minute;
        }
        String time = hourStr + ":" + minuteStr;
        timeView.setText(time);

        String date = getCalendarDateStr();
        dateView.setText(date);
    }

}
