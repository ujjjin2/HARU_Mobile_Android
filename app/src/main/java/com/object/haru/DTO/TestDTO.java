package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestDTO {
    @Expose
    @SerializedName("id")
    private String id;

    public TestDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
