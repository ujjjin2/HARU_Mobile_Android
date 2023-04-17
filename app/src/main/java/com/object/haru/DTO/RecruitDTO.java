package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RecruitDTO {

    @SerializedName("addr")
    @Expose
    private String addr;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pay")
    @Expose
    private Integer pay;
    @SerializedName("person")
    @Expose
    private Long person;
    @SerializedName("rage")
    @Expose
    private String rage;
    @SerializedName("rcareer")
    @Expose
    private String rcareer;
    @SerializedName("rid")
    @Expose
    private Long rid;
    @SerializedName("rsex")
    @Expose
    private String rsex;
    @SerializedName("rtime")
    @Expose
    private String rtime;
    @SerializedName("stTime")
    @Expose
    private String stTime;
    @SerializedName("step")
    @Expose
    private String step;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("kakaoid")
    @Expose
    private long kakaoid;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }

    public String getRage() {
        return rage;
    }

    public void setRage(String rage) {
        this.rage = rage;
    }

    public String getRcareer() {
        return rcareer;
    }

    public void setRcareer(String rcareer) {
        this.rcareer = rcareer;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public Long getPerson() {
        return person;
    }

    public void setPerson(Long person) {
        this.person = person;
    }

    public String getRsex() {
        return rsex;
    }

    public void setRsex(String rsex) {
        this.rsex = rsex;
    }

    public String getRtime() {
        return rtime;
    }

    public void setRtime(String rtime) {
        this.rtime = rtime;
    }

    public String getStTime() {
        return stTime;
    }

    public void setStTime(String stTime) {
        this.stTime = stTime;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(long kakaoid) {
        this.kakaoid = kakaoid;
    }

    public RecruitDTO(String addr, String endTime, double lat, double lon, Integer pay, String rage, String rcareer, String rsex, String stTime, String subject, String title, long kakaoid) {
        this.addr = addr;
        this.endTime = endTime;
        this.lat = lat;
        this.lon = lon;
        this.pay = pay;
        this.rage = rage;
        this.rcareer = rcareer;
        this.rsex = rsex;
        this.stTime = stTime;
        this.subject = subject;
        this.title = title;
        this.kakaoid = kakaoid;
    }
}