package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class zzimRequestDTO {
    @SerializedName("rid")
    @Expose
    private Integer rid;
    @SerializedName("kakaoid")
    @Expose
    private Integer kakaoid;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(Integer kakaoid) {
        this.kakaoid = kakaoid;
    }

    public zzimRequestDTO(Integer rid, Integer kakaoid) {
        this.rid = rid;
        this.kakaoid = kakaoid;
    }
}
