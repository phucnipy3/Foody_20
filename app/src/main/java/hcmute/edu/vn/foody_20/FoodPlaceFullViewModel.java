package hcmute.edu.vn.foody_20;

import java.sql.Time;

public class FoodPlaceFullViewModel {
    private int id;
    private String name;
    private String address;
    private String type;
    private String image;
    private Time openTime;
    private Time closeTime;
    private String reviewContent;
    private int reviewCount;
    private int checkinCount;
    private float rate;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private double distance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Time getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Time openTime) {
        this.openTime = openTime;
    }

    public Time getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Time closeTime) {
        this.closeTime = closeTime;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getCheckinCount() {
        return checkinCount;
    }

    public void setCheckinCount(int checkinCount) {
        this.checkinCount = checkinCount;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public FoodPlaceFullViewModel(int id, String name, String address, String type, String image, Time openTime, Time closeTime, String reviewContent, int reviewCount, int checkinCount, float rate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.image = image;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.reviewContent = reviewContent;
        this.reviewCount = reviewCount;
        this.checkinCount = checkinCount;
        this.rate = rate;
    }
}
