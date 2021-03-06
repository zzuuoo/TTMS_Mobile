package com.example.miaojie.ptest.pojo;

import java.io.Serializable;

/**
 * Create by zuo
 */
public class Play implements Serializable
{
    private int play_id;
    private String play_type;
    private String play_lang;
    private String play_name;
    private String play_introdution;
    private String play_image;
    private int play_length;
    private double play_ticket_price;
    private int play_status;//<=0���ۣ�>0 ����

    public String toString()
    {
        return play_name;
    }

    public int getPlay_id() {
        return play_id;
    }

    public void setPlay_id(int play_id) {
        this.play_id = play_id;
    }

    public String getPlay_type() {
        return play_type;
    }

    public void setPlay_type(String play_type) {
        this.play_type = play_type;
    }

    public String getPlay_lang() {
        return play_lang;
    }

    public void setPlay_lang(String play_lang) {
        this.play_lang = play_lang;
    }

    public String getPlay_name() {
        return play_name;
    }

    public void setPlay_name(String play_name) {
        this.play_name = play_name;
    }

    public String getPlay_introdution() {
        return play_introdution;
    }

    public void setPlay_introdution(String play_introdution) {
        this.play_introdution = play_introdution;
    }

    public String getPlay_image() {
        return play_image;
    }

    public void setPlay_image(String play_image) {
        this.play_image = play_image;
    }

    public int getPlay_length() {
        return play_length;
    }

    public void setPlay_length(int play_length) {
        this.play_length = play_length;
    }

    public double getPlay_ticket_price() {
        return play_ticket_price;
    }

    public void setPlay_ticket_price(double play_ticket_price) {
        this.play_ticket_price = play_ticket_price;
    }

    public int getPlay_status() {
        return play_status;
    }

    public void setPlay_status(int play_status) {
        this.play_status = play_status;
    }
}
