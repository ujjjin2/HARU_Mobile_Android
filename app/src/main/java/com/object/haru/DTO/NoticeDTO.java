package com.object.haru.DTO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoticeDTO {

    @SerializedName("nid")
    @Expose
    private Long nid; // 공지 번호

    @SerializedName("ncontents")
    @Expose
    private String ncontents; // 공지 내용

    public Long getNid() {
        return nid;
    }

    public void setNid(Long nid) {
        this.nid = nid;
    }

    public String getNcontents() {
        return ncontents;
    }

    public void setNcontents(String ncontents) {
        this.ncontents = ncontents;
    }
}
