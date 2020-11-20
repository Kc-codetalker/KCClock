package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.application.App.getAppContext;

@Entity(tableName = "event_based_alarm")
public class EventBasedAlarm extends Alarm {
    @Ignore
    public static final String EVENT_BATTERY_FULL_CHARGE = "EVENT_BATTERY_FULL_CHARGE";

    @Ignore
    public static final HashMap<String, String> EVENT_MAP = new HashMap<String, String>(){{
        put(EVENT_BATTERY_FULL_CHARGE,
                getAppContext().getResources().getString(R.string.event_battery_full_charged));
    }};

    @PrimaryKey
    @NonNull
    private String event;

    @ColumnInfo(name = "is_enabled")
    private boolean isEnabled;

    @ColumnInfo(name = "is_vibrate")
    private boolean isVibrate;

    @ColumnInfo(name = "use_sound")
    private boolean useSound;

    @ColumnInfo(name = "audio_uri")
    private String audioUri;

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public boolean isUseSound() {
        return useSound;
    }

    public String getEvent() {
        return event;
    }

    public String getAudioUri() {
        return audioUri;
    }

    public EventBasedAlarm(String event, boolean isEnabled, boolean isVibrate,
                           boolean useSound, String audioUri) {
        this.event = event;
        this.isEnabled = isEnabled;
        this.isVibrate = isVibrate;
        this.useSound = useSound;
        this.audioUri = audioUri;
    }

    public void enableAlarm(Context context) {
        this.isEnabled = true;

        String toastText = String.format("Alarm %s enabled", EVENT_MAP.get(event));
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
    }

    public void disableAlarm(Context context) {
        this.isEnabled = false;

        String toastText = String.format("Alarm %s cancelled", EVENT_MAP.get(event));
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
    }
}
