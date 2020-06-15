package hcmute.edu.vn.foody_20;

import java.math.BigDecimal;

public class FoodViewModel {
    private int id;
    private String foodName;
    private BigDecimal price;
    private String foodImage;
    private int foodPlaceId;
    private int typeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getFoodPlaceId() {
        return foodPlaceId;
    }

    public void setFoodPlaceId(int foodPlaceId) {
        this.foodPlaceId = foodPlaceId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public FoodViewModel(int id, String foodName, BigDecimal price, String foodImage, int foodPlaceId, int typeId) {
        this.id = id;
        this.foodName = foodName;
        this.price = price;
        this.foodImage = foodImage;
        this.foodPlaceId = foodPlaceId;
        this.typeId = typeId;
    }
}
