package com.lankacourier.Service;

import com.lankacourier.DAO.TrackingHistoryDAO;
import com.lankacourier.Model.TrackingHistory;

import java.util.List;

public class TrackingHistoryService {

    private final TrackingHistoryDAO dao = new TrackingHistoryDAO();

    public boolean addHistory(TrackingHistory h) {
        return dao.addHistory(h);
    }

    public List<TrackingHistory> getHistoryForParcel(int parcelId) {
        return dao.getHistoryByParcelId(parcelId);
    }

    public List<TrackingHistory> getAllHistory() {
        return dao.getAllHistory();
    }
}
