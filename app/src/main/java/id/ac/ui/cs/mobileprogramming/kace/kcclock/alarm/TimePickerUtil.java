package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm;

import android.os.Build;
import android.widget.TimePicker;

public class TimePickerUtil {

    public static int getTimePickerHour(TimePicker tp) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp.getHour();
        } else {
            return tp.getCurrentHour();
        }
    }

    public static int getTimePickerMinute(TimePicker tp) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp.getMinute();
        } else {
            return tp.getCurrentMinute();
        }
    }

    public static void setTimePickerHour(TimePicker tp, int hour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tp.setHour(hour);
        } else {
            tp.setCurrentHour(hour);
        }
    }

    public static void setTimePickerMinute(TimePicker tp, int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tp.setMinute(minute);
        } else {
            tp.setCurrentMinute(minute);
        }
    }
}
