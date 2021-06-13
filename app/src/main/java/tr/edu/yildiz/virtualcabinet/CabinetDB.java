package tr.edu.yildiz.virtualcabinet;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Outfit.class, Drawer.class, Combination.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class CabinetDB extends RoomDatabase {

    private static CabinetDB instance;
    public abstract DrawerDao drawerDao();
    public abstract OutfitDao outfitDao();
    public abstract CombinationDao combinationDao();

    public static synchronized  CabinetDB getInstance(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), CabinetDB.class,
                    "CabinetDB.db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }

        return instance;
    }
}
