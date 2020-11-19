package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventBasedAlarmDao {

    @Query("SELECT * FROM event_based_alarm ORDER BY event ASC")
    LiveData<List<EventBasedAlarm>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EventBasedAlarm alarm);

    @Update
    void update(EventBasedAlarm alarm);

    @Delete
    void delete(EventBasedAlarm alarm);

    @Query("DELETE FROM time_based_alarm")
    void deleteAll();
}
