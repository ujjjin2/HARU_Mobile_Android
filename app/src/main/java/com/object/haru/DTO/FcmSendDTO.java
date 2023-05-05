package com.object.haru.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FcmSendDTO {

    public FcmSendDTO(Long kakaoid, String title, String body, String topic) {
        this.kakaoid = kakaoid;
        this.title = title;
        this.body = body;
        this.topic = topic;
    }

    @SerializedName("acccesstoken")
    @Expose
    private Long kakaoid;

    private String title;

    private String body;

    private String topic;


    public Long getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(Long kakaoid) {
        this.kakaoid = kakaoid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


}
