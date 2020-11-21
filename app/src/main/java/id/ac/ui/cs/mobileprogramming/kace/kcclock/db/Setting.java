package id.ac.ui.cs.mobileprogramming.kace.kcclock.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "setting")
public class Setting {
    @Ignore
    public static final String SETTING_LANG = "LANGUAGE";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "setting_name")
    private String settingName;

    private String value;

    @NonNull
    public String getSettingName() {
        return settingName;
    }

    public String getValue() {
        return value;
    }

    public Setting(String settingName, String value) {
        this.settingName = settingName;
        this.value = value;
    }
}
