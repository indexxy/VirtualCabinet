package tr.edu.yildiz.virtualcabinet;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Calendar;


@Entity(tableName = "outfit",
        foreignKeys = {
        @ForeignKey(onDelete = ForeignKey.CASCADE, entity = Drawer.class, parentColumns = "id", childColumns = "drawerId")
    }
)
public class Outfit {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private  Integer drawerId;
    private String category, color, pattern, photo;
    private Calendar buyDate;
    private float price;

    public Outfit(
            Integer drawerId, String category, String color, String pattern, Calendar buyDate, float price, String photo
    ) {
        this.drawerId = drawerId;
        this.category = category;
        this.color = color;
        this.pattern = pattern;
        this.buyDate = buyDate;
        this.price = price;
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public Integer getDrawerId() {
        return drawerId;
    }

    public void setDrawerId(Integer drawerId) {
        this.drawerId = drawerId;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Calendar getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Calendar buyDate) {
        this.buyDate = buyDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
