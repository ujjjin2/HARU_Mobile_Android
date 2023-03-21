package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class zzimRequestDTO {
    @SerializedName("rid")
    @Expose
    private Integer rid;
    @SerializedName("kakaoid")
    @Expose
    private Long kakaoid;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Long getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(Long kakaoid) {
        this.kakaoid = kakaoid;
    }

    public zzimRequestDTO(Integer rid, Long kakaoid) {
        this.rid = rid;
        this.kakaoid = kakaoid;
    }
}
