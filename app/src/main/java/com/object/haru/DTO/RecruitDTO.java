package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RecruitDTO {

    @SerializedName("rid")
    @Expose
    private Integer rid;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("stTime")
    @Expose
    private String stTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("pay")
    @Expose
    private Integer pay;
    @SerializedName("addr")
    @Expose
    private String addr;
    @SerializedName("lat")
    @Expose
    private Object lat;
    @SerializedName("lon")
    @Expose
    private Object lon;
    @SerializedName("rage")
    @Expose
    private String rage;
    @SerializedName("rsex")
    @Expose
    private Object rsex;
    @SerializedName("rcareer")
    @Expose
    private String rcareer;
    @SerializedName("person")
    @Expose
    private Object person;
    @SerializedName("step")
    @Expose
    private String step;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
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

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Object getLat() {
        return lat;
    }

    public void setLat(Object lat) {
        this.lat = lat;
    }

    public Object getLon() {
        return lon;
    }

    public void setLon(Object lon) {
        this.lon = lon;
    }

    public String getRage() {
        return rage;
    }

    public void setRage(String rage) {
        this.rage = rage;
    }

    public Object getRsex() {
        return rsex;
    }

    public void setRsex(Object rsex) {
        this.rsex = rsex;
    }

    public String getRcareer() {
        return rcareer;
    }

    public void setRcareer(String rcareer) {
        this.rcareer = rcareer;
    }

    public Object getPerson() {
        return person;
    }

    public void setPerson(Object person) {
        this.person = person;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}