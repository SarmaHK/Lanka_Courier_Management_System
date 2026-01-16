package com.lankacourier.Controller;

import com.lankacourier.DAO.DistanceDAO;

public class DistanceController {

    private final DistanceDAO dao = new DistanceDAO();

    public void init() {
        dao.initTable();
    }

    public double getDistance(String district1, String district2) {
        return dao.getDistance(district1, district2);
    }
}
