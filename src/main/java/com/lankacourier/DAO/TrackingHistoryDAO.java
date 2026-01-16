package com.lankacourier.DAO;

import com.lankacourier.DB.dbconnection;
import com.lankacourier.Model.TrackingHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackingHistoryDAO {

    public boolean addHistory(TrackingHistory h) {
        String sql = "INSERT INTO tracking_history (parcel_id, branch_id, employee_id, status, timestamp) VALUES (?,?,?,?,?)";

        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, h.getParcelId());
            ps.setInt(2, h.getBranchId());

            // handle nullable employeeId
            if (h.getEmployeeId() == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, h.getEmployeeId());
            }

            ps.setString(4, h.getStatus());
            ps.setTimestamp(5, Timestamp.valueOf(h.getTimestamp()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TrackingHistory> getHistoryByParcelId(int parcelId) {
        List<TrackingHistory> list = new ArrayList<>();
        String sql = "SELECT history_id, parcel_id, branch_id, employee_id, status, timestamp FROM tracking_history WHERE parcel_id=? ORDER BY timestamp ASC";

        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, parcelId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TrackingHistory h = new TrackingHistory();
                    h.setHistoryId(rs.getInt("history_id"));
                    h.setParcelId(rs.getInt("parcel_id"));
                    h.setBranchId(rs.getInt("branch_id"));

                    // read nullable employee_id
                    int emp = rs.getInt("employee_id");
                    if (rs.wasNull()) h.setEmployeeId(null);
                    else h.setEmployeeId(emp);

                    h.setStatus(rs.getString("status"));
                    Timestamp ts = rs.getTimestamp("timestamp");
                    if (ts != null) h.setTimestamp(ts.toLocalDateTime());
                    list.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TrackingHistory> getAllHistory() {
        List<TrackingHistory> list = new ArrayList<>();
        String sql = "SELECT history_id, parcel_id, branch_id, employee_id, status, timestamp FROM tracking_history ORDER BY timestamp DESC";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TrackingHistory h = new TrackingHistory();
                h.setHistoryId(rs.getInt("history_id"));
                h.setParcelId(rs.getInt("parcel_id"));
                h.setBranchId(rs.getInt("branch_id"));
                int emp = rs.getInt("employee_id");
                if (rs.wasNull()) h.setEmployeeId(null);
                else h.setEmployeeId(emp);
                h.setStatus(rs.getString("status"));
                Timestamp ts = rs.getTimestamp("timestamp");
                if (ts != null) h.setTimestamp(ts.toLocalDateTime());
                list.add(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
