package com.example.miaojie.ptest.pojo;

public class ScheduleWeb {
    private Integer schedId;

    private Integer studioId;

    private Integer playId;

    private long schedTime;
    private double schedTicketPrice;

    public Integer getSchedId() {
        return schedId;
    }

    public void setSchedId(Integer schedId) {
        this.schedId = schedId;
    }

    public Integer getStudioId() {
        return studioId;
    }

    public void setStudioId(Integer studioId) {
        this.studioId = studioId;
    }

    public Integer getPlayId() {
        return playId;
    }

    public void setPlayId(Integer playId) {
        this.playId = playId;
    }

    public long getSchedTime() {
        return schedTime;
    }

    public void setSchedTime(long schedTime) {
        this.schedTime = schedTime;
    }

    public double getSchedTicketPrice() {
        return schedTicketPrice;
    }

    public void setSchedTicketPrice(double schedTicketPrice) {
        this.schedTicketPrice = schedTicketPrice;
    }
}
