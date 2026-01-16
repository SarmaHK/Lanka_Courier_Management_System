package com.lankacourier.Controller;

import com.lankacourier.Model.Rates;
import com.lankacourier.Service.RatesService;

import java.math.BigDecimal;
import java.util.List;

public class RatesController {
    private final RatesService service = new RatesService();

    public void init() {
        service.init();
    }

    public List<Rates> fetchAll() {
        return service.getAllRates();
    }

    public Rates findById(int id) {
        return service.getById(id);
    }

    public boolean create(Rates r) {
        return service.addRate(r);
    }

    public boolean update(Rates r) {
        return service.updateRate(r);
    }

    public boolean delete(int id) {
        return service.deleteRate(id);
    }

    public Rates findRateForWeight(BigDecimal weight) {
        return service.findRateForWeight(weight);
    }

    // New: Distance based pricing
    // Rate per km = 2.00 LKR
    private static final BigDecimal RATE_PER_KM = new BigDecimal("2.00");

    public BigDecimal calculatePrice(BigDecimal weight, BigDecimal distanceKm) {
        Rates r = findRateForWeight(weight);
        BigDecimal basePrice = (r != null) ? r.getPriceLkr() : BigDecimal.ZERO;

        if (distanceKm == null || distanceKm.compareTo(BigDecimal.ZERO) <= 0) {
            return basePrice;
        }

        BigDecimal distanceCost = distanceKm.multiply(RATE_PER_KM);
        return basePrice.add(distanceCost);
    }
}
