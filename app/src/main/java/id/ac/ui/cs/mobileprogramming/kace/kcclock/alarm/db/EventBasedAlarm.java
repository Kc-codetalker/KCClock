package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "event_based_alarm")
public class EventBasedAlarm {
    @Ignore
    public static final String EVENT_BATTERY_FULL_CHARGE = "EVENT_BATTERY_FULL_CHARGE";

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "is_enabled")
    private boolean isEnabled;

    @ColumnInfo(name = "is_recurring")
    private boolean isRecurring;

    @ColumnInfo(name = "is_vibrate")
    private boolean isVibrate;

    @ColumnInfo(name = "use_sound")
    private boolean useSound;

    private String name, event;

    @ColumnInfo(name = "audio_uri")
    private String audioUri;

    public int getId() {
        return id;
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

    public String getName() {
        return name;
    }

    public String getEvent() {
        return event;
    }

    public String getAudioUri() {
        return audioUri;
    }

    public EventBasedAlarm(int id, String event, boolean isEnabled, boolean isVibrate,
                           boolean useSound, boolean isRecurring, String name, String audioUri) {
        this.id = id;
        this.event = event;
        this.isEnabled = isEnabled;
        this.isRecurring = isRecurring;
        this.isVibrate = isVibrate;
        this.useSound = useSound;
        this.name = name;
        this.audioUri = audioUri;
    }
}
