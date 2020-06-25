package hcmute.edu.vn.foody_20;

public class WifiViewModel {
    private String name;
    private String password;
    private int foodPlaceId;

    public WifiViewModel(String name, String password, int foodPlaceId) {
        this.name = name;
        this.password = password;
        this.foodPlaceId = foodPlaceId;
    }

    public int getFoodPlaceId() {
        return foodPlaceId;
    }

    public void setFoodPlaceId(int foodPlaceId) {
        this.foodPlaceId = foodPlaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
