package com.example.miaojie.ptest.pojo;

public class SaleWeb {
    private int saleId;
    private int empId;
    private long saleTime;
    private double salePayment;//��Ǯ
    private double saleChange;//����
    private int saleType;// 1�����۵�\r\n            -1���˿
    private int saleStatus;//0������ 1�Ѹ���

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public long getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(long saleTime) {
        this.saleTime = saleTime;
    }

    public double getSalePayment() {
        return salePayment;
    }

    public void setSalePayment(double salePayment) {
        this.salePayment = salePayment;
    }

    public double getSaleChange() {
        return saleChange;
    }

    public void setSaleChange(double saleChange) {
        this.saleChange = saleChange;
    }

    public int getSaleType() {
        return saleType;
    }

    public void setSaleType(int saleType) {
        this.saleType = saleType;
    }

    public int getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(int saleStatus) {
        this.saleStatus = saleStatus;
    }
}
