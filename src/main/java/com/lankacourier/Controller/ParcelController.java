package com.lankacourier.Controller;

import com.lankacourier.Model.Parcel;
import com.lankacourier.Service.ParcelService;

import java.util.List;

public class ParcelController {
    private final ParcelService service = new ParcelService();

    public List<Parcel> fetchAll() {
        return service.getAllParcels();
    }

    public int getNextParcelId() {
        return service.getNextParcelId();
    }

    public Parcel findById(int id) {
        return service.getById(id);
    }

    public Parcel findByTrackingId(String trackingId) {
        return service.getByTrackingId(trackingId);
    }

    public boolean create(Parcel p) {
        return service.addParcel(p);
    }

    public boolean update(Parcel p) {
        return service.updateParcel(p);
    }

    public boolean delete(int id) {
        return service.deleteParcel(id);
    }
}
