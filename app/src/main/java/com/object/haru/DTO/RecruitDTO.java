package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecruitDTO {
    @Expose
    @SerializedName("id")
    private int rid;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("subject")
    private String subject;
    @Expose
    @SerializedName("stTime")
    private String stTime;
    @Expose
    @SerializedName("endTime")
    private String endTime;
    @Expose
    @SerializedName("pay")
    private int pay;
    @Expose
    @SerializedName("addr")
    private String addr;
    @Expose
    @SerializedName("lat")
    private int lat;
    @Expose
    @SerializedName("lon")
    private int lon;
    @Expose
    @SerializedName("rage")
    private String rage;
    @Expose
    @SerializedName("rsex")
    private String rsex;
    @Expose
    @SerializedName("rcareer")
    private String rcareer;
    @Expose
    @SerializedName("count")
    private int count;
    @Expose
    @SerializedName("name")
    private String name;

    public RecruitDTO(int rid, String title, String subject, String stTime, String endTime, int pay, String addr, int lat, int lon, String rage, String rsex, String rcareer, int count, String name) {
        this.rid = rid;
        this.title = title;
        this.subject = subject;
        this.stTime = stTime;
        this.endTime = endTime;
        this.pay = pay;
        this.addr = addr;
        this.lat = lat;
        this.lon = lon;
        this.rage = rage;
        this.rsex = rsex;
        this.rcareer = rcareer;
        this.count = count;
        this.name = name;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStTime() {
        return stTime;
    }

    public void setStTime(String stTime) {
        this.stTime = stTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public String getRage() {
        return rage;
    }

    public void setRage(String rage) {
        this.rage = rage;
    }

    public String getRsex() {
        return rsex;
    }

    public void setRsex(String rsex) {
        this.rsex = rsex;
    }

    public String getRcareer() {
        return rcareer;
    }

    public void setRcareer(String rcareer) {
        this.rcareer = rcareer;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
