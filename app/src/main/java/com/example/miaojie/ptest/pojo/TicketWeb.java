package com.example.miaojie.ptest.pojo;

public class TicketWeb {
    private int ticketId;
    private int seatId;
    private int schedId;
    private double ticketPrice;
    private int ticketStatus;//0Œ¥ €°¢1“— €
    private long ticketLockedTime;

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getSchedId() {
        return schedId;
    }

    public void setSchedId(int schedId) {
        this.schedId = schedId;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(int ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public long getTicketLockedTime() {
        return ticketLockedTime;
    }

    public void setTicketLockedTime(long ticketLockedTime) {
        this.ticketLockedTime = ticketLockedTime;
    }
}
