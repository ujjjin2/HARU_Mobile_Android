package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FCMDTO {
    @SerializedName("fcmToken")
    @Expose
    private String fcmToken;
    @SerializedName("kakaoid")
    @Expose
    private Long kakaoid;

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Long getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(Long kakaoid) {
        this.kakaoid = kakaoid;
    }

    public FCMDTO(String fcmToken, Long kakaoid) {
        this.fcmToken = fcmToken;
        this.kakaoid = kakaoid;
    }
}
