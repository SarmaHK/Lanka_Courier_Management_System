package com.lankacourier.Model;

public class Customer {

    private int customerId;
    private String customerType; // individual | business
    private String businessName;
    private String contactPersonName;
    private String email;
    private String businessRegNo;
    private String contactPersonNic;
    private String address;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String mobileNo;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return ("business".equalsIgnoreCase(customerType) ? businessName : contactPersonName)
                + (mobileNo != null ? " (" + mobileNo + ")" : "");
    }

    public String getBusinessRegNo() {
        return businessRegNo;
    }

    public void setBusinessRegNo(String businessRegNo) {
        this.businessRegNo = businessRegNo;
    }

    public String getContactPersonNic() {
        return contactPersonNic;
    }

    public void setContactPersonNic(String contactPersonNic) {
        this.contactPersonNic = contactPersonNic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
