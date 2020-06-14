package hcmute.edu.vn.foody_20;

public class FoodPlaceCardViewModel {
    private int id;
    private String image;
    private String name;
    private String review;

    public FoodPlaceCardViewModel(int id, String image, String name, String review) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
