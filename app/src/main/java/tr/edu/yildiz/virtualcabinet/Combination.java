package tr.edu.yildiz.virtualcabinet;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "combination")
public class Combination {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String name;
    private Integer headId, faceId, upperBodyId, lowerBodyId, feetId;

    @Ignore
    public Combination(String name, Integer headId, Integer faceId, Integer upperBodyId, Integer lowerBodyId, Integer feetId) {
        this.name = name;
        this.headId = headId;
        this.faceId = faceId;
        this.upperBodyId = upperBodyId;
        this.lowerBodyId = lowerBodyId;
        this.feetId = feetId;
    }

    public Combination(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHeadId() {
        return headId;
    }

    public void setHeadId(Integer headId) {
        this.headId = headId;
    }

    public Integer getFaceId() {
        return faceId;
    }

    public void setFaceId(Integer faceId) {
        this.faceId = faceId;
    }

    public Integer getUpperBodyId() {
        return upperBodyId;
    }

    public void setUpperBodyId(Integer upperBodyId) {
        this.upperBodyId = upperBodyId;
    }

    public Integer getLowerBodyId() {
        return lowerBodyId;
    }

    public void setLowerBodyId(Integer lowerBodyId) {
        this.lowerBodyId = lowerBodyId;
    }

    public Integer getFeetId() {
        return feetId;
    }

    public void setFeetId(Integer feetId) {
        this.feetId = feetId;
    }
}
