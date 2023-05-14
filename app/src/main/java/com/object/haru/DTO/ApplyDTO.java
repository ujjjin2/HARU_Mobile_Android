package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplyDTO {
    @SerializedName("aage")
    @Expose
    private String aage;
    @SerializedName("acareer")
    @Expose
    private String acareer;
    @SerializedName("asex")
    @Expose
    private String asex;
    @SerializedName("myself")
    @Expose
    private String myself;

    public String getName() {
        return name;
    }

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("rid")
    @Expose
    private Long rid;



    @SerializedName("kakaoid")
    @Expose
    private Long kakaoid;
    @SerializedName("aid")
    @Expose
    private Integer aid;
    @SerializedName("avgRating")
    @Expose
    private Double avgRating;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("step")
    @Expose
    private String step;
    @SerializedName("title")
    @Expose
    private String title;

    public Long getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(Long kakaoid) {
        this.kakaoid = kakaoid;
    }

    public String getAage() {
        return aage;
    }

    public void setAage(String aage) {
        this.aage = aage;
    }

    public String getAcareer() {
        return acareer;
    }

    public void setAcareer(String acareer) {
        this.acareer = acareer;
    }

    public String getAsex() {
        return asex;
    }

    public void setAsex(String asex) {
        this.asex = asex;
    }

    public String getMyself() {
        return myself;
    }

    public void setMyself(String myself) {
        this.myself = myself;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Long getUid() {
        return kakaoid;
    }

    public void setUid(Integer uid) {
        this.kakaoid = kakaoid;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public ApplyDTO(String aage, String acareer, String asex, String myself, Long rid, Long kakaoid) {
        this.aage = aage;
        this.acareer = acareer;
        this.asex = asex;
        this.myself = myself;
        this.rid = rid;
        this.kakaoid = kakaoid;
    }
}
