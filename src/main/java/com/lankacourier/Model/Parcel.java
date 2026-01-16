package com.lankacourier.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Parcel {
    private int parcelId;
    private String trackingId;
    private int senderId;
    private int receiverId;
    private int originBranchId;
    private Integer destinationBranchId; // nullable
    private String description;
    private BigDecimal weightKg;         // DECIMAL(10,3)
    private BigDecimal distanceKm;       // DECIMAL(10,2)
    private BigDecimal priceLkr;         // DECIMAL(10,2)
    private String status;
    private LocalDateTime createdAt;
    private boolean isCod;
    private BigDecimal codAmountLkr;
    private BigDecimal serviceChargeLkr;
    private BigDecimal moCommissionLkr;
    private String codPaymentStatus;

    public Parcel() {}

    public Parcel(int parcelId, String trackingId, int senderId, int receiverId,
                  int originBranchId, Integer destinationBranchId, String description,
                  BigDecimal weightKg, BigDecimal distanceKm, BigDecimal priceLkr, String status,
                  LocalDateTime createdAt, boolean isCod, BigDecimal codAmountLkr,
                  BigDecimal serviceChargeLkr, BigDecimal moCommissionLkr, String codPaymentStatus) {
        this.parcelId = parcelId;
        this.trackingId = trackingId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.originBranchId = originBranchId;
        this.destinationBranchId = destinationBranchId;
        this.description = description;
        this.weightKg = weightKg;
        this.distanceKm = distanceKm;
        this.priceLkr = priceLkr;
        this.status = status;
        this.createdAt = createdAt;
        this.isCod = isCod;
        this.codAmountLkr = codAmountLkr;
        this.serviceChargeLkr = serviceChargeLkr;
        this.moCommissionLkr = moCommissionLkr;
        this.codPaymentStatus = codPaymentStatus;
    }

    // getters / setters
    public int getParcelId() { return parcelId; }
    public void setParcelId(int parcelId) { this.parcelId = parcelId; }

    public String getTrackingId() { return trackingId; }
    public void setTrackingId(String trackingId) { this.trackingId = trackingId; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public int getOriginBranchId() { return originBranchId; }
    public void setOriginBranchId(int originBranchId) { this.originBranchId = originBranchId; }

    public Integer getDestinationBranchId() { return destinationBranchId; }
    public void setDestinationBranchId(Integer destinationBranchId) { this.destinationBranchId = destinationBranchId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }

    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }

    public BigDecimal getPriceLkr() { return priceLkr; }
    public void setPriceLkr(BigDecimal priceLkr) { this.priceLkr = priceLkr; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isCod() { return isCod; }
    public void setCod(boolean cod) { isCod = cod; }

    public BigDecimal getCodAmountLkr() { return codAmountLkr; }
    public void setCodAmountLkr(BigDecimal codAmountLkr) { this.codAmountLkr = codAmountLkr; }

    public BigDecimal getServiceChargeLkr() { return serviceChargeLkr; }
    public void setServiceChargeLkr(BigDecimal serviceChargeLkr) { this.serviceChargeLkr = serviceChargeLkr; }

    public BigDecimal getMoCommissionLkr() { return moCommissionLkr; }
    public void setMoCommissionLkr(BigDecimal moCommissionLkr) { this.moCommissionLkr = moCommissionLkr; }

    public String getCodPaymentStatus() { return codPaymentStatus; }
    public void setCodPaymentStatus(String codPaymentStatus) { this.codPaymentStatus = codPaymentStatus; }

    @Override
    public String toString() {
        return trackingId + " (" + status + ")";
    }
}
