package id.ac.ui.cs.mobileprogramming.kace.kcclock.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TimeBasedAlarmDao {

    @Query("SELECT * FROM time_based_alarm ORDER BY hour ASC, minute ASC")
    LiveData<List<TimeBasedAlarm>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TimeBasedAlarm alarm);

    @Update
    void update(TimeBasedAlarm alarm);

    @Delete
    void delete(TimeBasedAlarm alarm);

    @Query("DELETE FROM time_based_alarm")
    void deleteAll();
}
