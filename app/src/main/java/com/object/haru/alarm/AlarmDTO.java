package com.object.haru.alarm;

import java.time.LocalDateTime;

public class AlarmDTO {
    private Long kakaoid;
    private Long alarmid;
    private String body;
    private String title;
    private int confirm;
    private long aid;
    private long rid;
    private long rrid;
    private String alTime;

    public Long getAlarmid() {
        return alarmid;
    }

    public void setAlarmid(Long alarmid) {
        this.alarmid = alarmid;
    }
    public long getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(long kakaoid) {
        this.kakaoid = kakaoid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public long getRrid() {
        return rrid;
    }

    public void setRrid(long rrid) {
        this.rrid = rrid;
    }

    public String getAlTime() {
        return alTime;
    }

    public void setAlTime(String alTime) {
        this.alTime = alTime;
    }



    // Constructors, getters, and setters
}
