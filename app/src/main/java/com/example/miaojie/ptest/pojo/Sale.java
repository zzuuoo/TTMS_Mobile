package com.example.miaojie.ptest.pojo;

import java.util.Date;

public class Sale
{
    private int sale_id;
    private int emp_id;
    private Date sale_time;
    private double sale_payment;//收钱
    private double sale_change;//找零
    private int sale_type;//退款、付款
    private int sale_status;//0待付款 1已付款

    public int getSale_id() {
        return sale_id;
    }

    public void setSale_id(int sale_id) {
        this.sale_id = sale_id;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public Date getSale_time() {
        return sale_time;
    }

    public void setSale_time(Date sale_time) {
        this.sale_time = sale_time;
    }

    public double getSale_payment() {
        return sale_payment;
    }

    public void setSale_payment(double sale_payment) {
        this.sale_payment = sale_payment;
    }

    public double getSale_change() {
        return sale_change;
    }

    public void setSale_change(double sale_change) {
        this.sale_change = sale_change;
    }

    public int getSale_type() {
        return sale_type;
    }

    public void setSale_type(int sale_type) {
        this.sale_type = sale_type;
    }

    public int getSale_status() {
        return sale_status;
    }

    public void setSale_status(int sale_status) {
        this.sale_status = sale_status;
    }
}
