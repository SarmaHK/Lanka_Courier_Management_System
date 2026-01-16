package com.lankacourier.Service;

import com.lankacourier.DAO.ParcelDAO;
import com.lankacourier.Model.Parcel;

import java.math.BigDecimal;
import java.util.List;

public class ParcelService {
    private final ParcelDAO dao = new ParcelDAO();

    public List<Parcel> getAllParcels() {
        return dao.getAllParcels();
    }

    public int getNextParcelId() {
        return dao.getNextParcelId();
    }

    public Parcel getById(int id) {
        if (id <= 0)
            return null;
        return dao.getParcelById(id);
    }

    public Parcel getByTrackingId(String trackingId) {
        if (trackingId == null || trackingId.isBlank())
            return null;
        return dao.getByTrackingId(trackingId);
    }

    public boolean addParcel(Parcel p) {
        if (p == null)
            return false;
        if (p.getTrackingId() == null || p.getTrackingId().isBlank())
            return false;
        if (p.getSenderId() <= 0 || p.getReceiverId() <= 0)
            return false;
        if (p.getWeightKg() == null || p.getWeightKg().compareTo(BigDecimal.ZERO) <= 0)
            return false;
        if (p.getPriceLkr() == null || p.getPriceLkr().compareTo(BigDecimal.ZERO) < 0)
            return false;
        return dao.addParcel(p);
    }

    public boolean updateParcel(Parcel p) {
        if (p == null || p.getParcelId() <= 0)
            return false;
        return dao.updateParcel(p);
    }

    public boolean deleteParcel(int id) {
        if (id <= 0)
            return false;
        return dao.deleteParcel(id);
    }
}
