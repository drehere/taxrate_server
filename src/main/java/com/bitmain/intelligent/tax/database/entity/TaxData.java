package com.bitmain.intelligent.tax.database.entity;

public class TaxData {
    private long id;
    private long userID;
    private int grossPay;
    private int fee;
    private int threshold;
    private int tax;
    private int realSalary;
    private String resultDesc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public int getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(int grossPay) {
        this.grossPay = grossPay;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public int getRealSalary() {
        return realSalary;
    }

    public void setRealSalary(int realSalary) {
        this.realSalary = realSalary;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
}
