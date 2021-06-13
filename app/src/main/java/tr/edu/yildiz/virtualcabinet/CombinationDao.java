package tr.edu.yildiz.virtualcabinet;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CombinationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Combination comb);

    @Delete
    public void delete(Combination comb);

    @Query("DELETE FROM combination WHERE id == :id")
    public void deleteById(Integer id);

    @Query("SELECT * FROM combination")
    public List<Combination> getCombinations();

    @Query("SELECT * FROM combination WHERE id == :id")
    public Combination getCombination(Integer id);
}
