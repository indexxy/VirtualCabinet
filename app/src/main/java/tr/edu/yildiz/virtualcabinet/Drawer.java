package tr.edu.yildiz.virtualcabinet;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "drawer")
public class Drawer {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String name;

    public Drawer(String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
