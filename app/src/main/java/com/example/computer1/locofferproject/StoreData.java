package com.example.computer1.locofferproject;

public class StoreData {
    private String name, category, address, offer, imageFile;
    private double storeLat, storeLong;

    public StoreData() {}

    public StoreData(String category,String name,String address,String offer, String imageFile) {
        this.name = name;
        this.category = category;
        this.address = address;
        this.offer = offer;
        this.imageFile = imageFile;
    }

    public StoreData(String category,String name,String address,String offer, double storeLat, double storeLong) {
        this.name = name;
        this.category = category;
        this.address = address;
        this.offer = offer;
        this.storeLat = storeLat;
        this.storeLong = storeLong;

    }

    public double getStoreLat() {
        return storeLat;
    }

    public void setStoreLat(double storeLat) {
        this.storeLat = storeLat;
    }

    public double getStoreLong() {
        return storeLong;
    }

    public void setStoreLong(double storeLong) {
        this.storeLong = storeLong;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

}
