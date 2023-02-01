package com.object.haru;

public class MainData {

    private String title, category, time, money, location;
    private int profile;

    public MainData(int profile, String title, String category, String time, String money, String location) {
        this.profile = profile;
        this.title = title;
        this.category = category;
        this.time = time;
        this.money = money;
        this.location = location;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
