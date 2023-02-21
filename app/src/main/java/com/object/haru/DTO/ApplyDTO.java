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
    @SerializedName("rid")
    @Expose
    private Integer rid;
    @SerializedName("uid")
    @Expose
    private Integer uid;

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

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public ApplyDTO(String aage, String acareer, String asex, String myself, Integer rid, Integer uid) {
        this.aage = aage;
        this.acareer = acareer;
        this.asex = asex;
        this.myself = myself;
        this.rid = rid;
        this.uid = uid;
    }
}
