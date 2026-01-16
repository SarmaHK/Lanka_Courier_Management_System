package com.lankacourier.Model;

public class CustomerMobileNumber {

    private int mobileNoId;
    private int customerId;
    private String mobileNo;

    public CustomerMobileNumber() {}

    public int getMobileNoId() {
        return mobileNoId;
    }

    public void setMobileNoId(int mobileNoId) {
        this.mobileNoId = mobileNoId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return mobileNo;
    }
}
