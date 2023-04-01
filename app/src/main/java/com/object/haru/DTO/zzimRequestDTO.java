package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class zzimRequestDTO {
    @SerializedName("rid")
    @Expose
    private Long rid;
    @SerializedName("kakaoid")
    @Expose
    private Long kakaoid;

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Long getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(Long kakaoid) {
        this.kakaoid = kakaoid;
    }

    public zzimRequestDTO(Long rid, Long kakaoid) {
        this.rid = rid;
        this.kakaoid = kakaoid;
    }
}
