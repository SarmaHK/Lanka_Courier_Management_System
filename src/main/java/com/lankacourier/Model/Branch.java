package com.lankacourier.Model;

public class Branch {
    private int BranchID;
    private String B_name;
    private String B_Type;
    private String District;
    private String Province;
    private String PostalCode;
    private String TelNo;
    private Double Latitude;
    private Double Longitude;
    private Double branchDistance;

    public Branch() {
    }

    public Branch(String B_name, String B_Type, String District, String Province, String PostalCode, String TelNo) {
        this.B_name = B_name;
        this.B_Type = B_Type;
        this.District = District;
        this.Province = Province;
        this.PostalCode = PostalCode;
        this.TelNo = TelNo;
    }

    public Branch(int BranchID, String B_name, String B_Type, String District, String Province, String PostalCode,
            String TelNo) {
        this.BranchID = BranchID;
        this.B_name = B_name;
        this.B_Type = B_Type;
        this.District = District;
        this.Province = Province;
        this.PostalCode = PostalCode;
        this.TelNo = TelNo;
    }

    public int getBranchId() {
        return BranchID;
    }

    public void setBranchId(int BranchID) {
        this.BranchID = BranchID;
    }

    public String getName() {
        return B_name;
    }

    public void setName(String B_name) {
        this.B_name = B_name;
    }

    public String getType() {
        return B_Type;
    }

    public void setType(String B_Type) {
        this.B_Type = B_Type;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String District) {
        this.District = District;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String Province) {
        this.Province = Province;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String PostalCode) {
        this.PostalCode = PostalCode;
    }

    public String getTelNo() {
        return TelNo;
    }

    public void setTelNo(String TelNo) {
        this.TelNo = TelNo;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getBranchDistance() {
        return branchDistance;
    }

    public void setBranchDistance(Double branchDistance) {
        this.branchDistance = branchDistance;
    }

    @Override
    public String toString() {
        return B_name + " (" + District + ")";
    }
}
