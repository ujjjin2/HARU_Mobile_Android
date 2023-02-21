package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class zzimRequestDTO {
    @SerializedName("rid")
    @Expose
    private Integer rid;
    @SerializedName("uid")
    @Expose
    private Integer uid;

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

    public zzimRequestDTO(Integer rid, Integer uid) {
        this.rid = rid;
        this.uid = uid;
    }
}
