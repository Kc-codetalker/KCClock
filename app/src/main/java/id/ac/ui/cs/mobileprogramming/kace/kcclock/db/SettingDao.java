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
public interface SettingDao {
    @Query("SELECT * FROM setting ORDER BY setting_name ASC")
    LiveData<List<Setting>> getAll();

    @Query("SELECT * FROM setting WHERE setting_name LIKE :name LIMIT 1")
    Setting getSettingByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Setting setting);

    @Update
    void update(Setting setting);

    @Delete
    void delete(Setting setting);

    @Query("DELETE FROM setting")
    void deleteAll();
}
