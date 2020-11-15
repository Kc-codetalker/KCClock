package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TimeBasedAlarmDao {

    @Query("SELECT * FROM time_based_alarm ORDER BY id ASC")
    LiveData<List<TimeBasedAlarm>> getAll();

    @Insert
    void insert(TimeBasedAlarm alarm);

    @Update
    void update(TimeBasedAlarm alarm);

    @Delete
    void delete(TimeBasedAlarm alarm);

    @Query("DELETE FROM time_based_alarm")
    void deleteAll();
}
