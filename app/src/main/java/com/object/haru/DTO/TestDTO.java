package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestDTO {

    @SerializedName("acccesstoken")
    @Expose
    private String acccesstoken;

    public String getAcccesstoken() {
        return acccesstoken;
    }

    public void setAcccesstoken(String acccesstoken) {
        this.acccesstoken = acccesstoken;
    }
}
