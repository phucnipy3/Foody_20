package hcmute.edu.vn.foody_20;

import java.math.BigDecimal;
import java.sql.Time;

public class FoodPlaceDetailViewModel {
    private int id;
    private String name;
    private String address;
    private String type;
    private Time openTime;
    private Time closeTime;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String provinceName;

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

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    private String contact;

    public FoodPlaceDetailViewModel(int id, String name, String address, String type, Time openTime, Time closeTime, BigDecimal minPrice, BigDecimal maxPrice, String provinceName, String contact) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.provinceName = provinceName;
        this.contact = contact;
    }
    public FoodPlaceDetailViewModel(){

    }
}
