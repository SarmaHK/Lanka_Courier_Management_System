package com.lankacourier.Model;

import java.math.BigDecimal;

public class Rates {
    private int rateId;
    private BigDecimal minWeightKg;
    private BigDecimal maxWeightKg;
    private BigDecimal priceLkr;

    public Rates() {}

    public Rates(int rateId, BigDecimal minWeightKg, BigDecimal maxWeightKg, BigDecimal priceLkr) {
        this.rateId = rateId;
        this.minWeightKg = minWeightKg;
        this.maxWeightKg = maxWeightKg;
        this.priceLkr = priceLkr;
    }

    // getters / setters
    public int getRateId() { return rateId; }
    public void setRateId(int rateId) { this.rateId = rateId; }

    public BigDecimal getMinWeightKg() { return minWeightKg; }
    public void setMinWeightKg(BigDecimal minWeightKg) { this.minWeightKg = minWeightKg; }

    public BigDecimal getMaxWeightKg() { return maxWeightKg; }
    public void setMaxWeightKg(BigDecimal maxWeightKg) { this.maxWeightKg = maxWeightKg; }

    public BigDecimal getPriceLkr() { return priceLkr; }
    public void setPriceLkr(BigDecimal priceLkr) { this.priceLkr = priceLkr; }

    @Override
    public String toString() {
        return priceLkr + " LKR (" + minWeightKg + " - " + maxWeightKg + " kg)";
    }
}
