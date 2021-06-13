package tr.edu.yildiz.virtualcabinet;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DrawerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Drawer drawer);

    @Query("SELECT * FROM drawer")
    public List<Drawer> getDrawers();


    @Query("DELETE FROM drawer where id = :id")
    public void deleteById(Integer id);

}
