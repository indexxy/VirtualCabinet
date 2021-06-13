package tr.edu.yildiz.virtualcabinet;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OutfitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Outfit outfit);

    @Query("SELECT * FROM outfit WHERE category LIKE :category ")
    public List<Outfit> getOutfitsByCategory(String category);

    @Query("SELECT * FROM outfit WHERE drawerId == :drawerId")
    public List<Outfit> getOutfitsByDrawer(Integer drawerId);

    @Query("SELECT * FROM outfit WHERE id == :id")
    public Outfit getOutfitById(Integer id);

    @Query("SELECT * FROM outfit")
    public List<Outfit> getOutfits();

    @Delete
    public void delete(Outfit outfit);

}
