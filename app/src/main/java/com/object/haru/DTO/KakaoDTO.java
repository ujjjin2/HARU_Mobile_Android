package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KakaoDTO {
    @SerializedName("acccesstoken")
    @Expose
    private String acccesstoken;

    public String getacccesstoken() {
        return acccesstoken;
    }

    public void setacccesstoken(String code) {
        this.acccesstoken = acccesstoken;
    }
}
