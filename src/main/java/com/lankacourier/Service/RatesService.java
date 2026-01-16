package com.lankacourier.Service;

import com.lankacourier.DAO.RatesDAO;
import com.lankacourier.Model.Rates;

import java.math.BigDecimal;
import java.util.List;

public class RatesService {
    private final RatesDAO dao = new RatesDAO();

    public void init() {
        dao.initTable();
    }

    public List<Rates> getAllRates() {
        return dao.getAllRates();
    }

    public Rates getById(int id) {
        return dao.getRateById(id);
    }

    public boolean addRate(Rates r) {
        return dao.addRate(r);
    }

    public boolean updateRate(Rates r) {
        return dao.updateRate(r);
    }

    public boolean deleteRate(int id) {
        return dao.deleteRate(id);
    }

    public Rates findRateForWeight(BigDecimal weightKg) {
        return dao.getRateForWeight(weightKg);
    }
}
