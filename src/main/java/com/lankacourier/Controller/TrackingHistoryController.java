package com.lankacourier.Controller;

import com.lankacourier.Model.TrackingHistory;
import com.lankacourier.Service.TrackingHistoryService;

import java.util.List;

public class TrackingHistoryController {

    private final TrackingHistoryService service = new TrackingHistoryService();

    
    public boolean create(TrackingHistory h) {
        return service.addHistory(h);
    }

    public List<TrackingHistory> fetchByParcelId(int parcelId) {
        return service.getHistoryForParcel(parcelId);
    }

    public List<TrackingHistory> fetchAll() {
        return service.getAllHistory();
    }
}
