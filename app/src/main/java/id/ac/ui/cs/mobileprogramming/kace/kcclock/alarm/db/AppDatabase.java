package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TimeBasedAlarm.class, EventBasedAlarm.class, Setting.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TimeBasedAlarmDao timeBasedAlarmDao();
    public abstract EventBasedAlarmDao eventBasedAlarmDao();
    public abstract SettingDao settingDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 10;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "kcclock_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
