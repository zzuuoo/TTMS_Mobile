package com.example.miaojie.ptest.pojo;

public class Sale_item {
    private int sale_item_id;
    private int ticket_id;
    private int sale_id;
    private double sale_item_price;

    public int getSale_item_id() {
        return sale_item_id;
    }

    public void setSale_item_id(int sale_item_id) {
        this.sale_item_id = sale_item_id;
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getSale_id() {
        return sale_id;
    }

    public void setSale_id(int sale_id) {
        this.sale_id = sale_id;
    }

    public double getSale_item_price() {
        return sale_item_price;
    }

    public void setSale_item_price(double sale_item_price) {
        this.sale_item_price = sale_item_price;
    }
}
