package com.lankacourier.DAO;

import com.lankacourier.DB.dbconnection;
import com.lankacourier.Model.PasswordResetRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordResetDAO {

    public PasswordResetDAO() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS password_resets (" +
                "request_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL, " +
                "status VARCHAR(20) DEFAULT 'PENDING', " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";
        try (Connection conn = dbconnection.getConnection();
                Statement stmt = conn.createStatement()) {
            if (conn != null)
                stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean createRequest(String username) {
        // First check if pending or approved exists
        PasswordResetRequest existing = findPendingOrApproved(username);
        if (existing != null)
            return true; // Already exists, consider it done

        String sql = "INSERT INTO password_resets (username, status) VALUES (?, 'PENDING')";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatus(int requestId, String status) {
        String sql = "UPDATE password_resets SET status=? WHERE request_id=?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean completeRequest(String username) {
        String sql = "UPDATE password_resets SET status='COMPLETED' WHERE username=? AND status='APPROVED'";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public PasswordResetRequest findPendingOrApproved(String username) {
        String sql = "SELECT * FROM password_resets WHERE username=? AND status IN ('PENDING', 'APPROVED') ORDER BY request_id DESC LIMIT 1";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PasswordResetRequest> fetchAllPending() {
        List<PasswordResetRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM password_resets WHERE status='PENDING' ORDER BY created_at ASC";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private PasswordResetRequest mapRow(ResultSet rs) throws SQLException {
        PasswordResetRequest r = new PasswordResetRequest();
        r.setRequestId(rs.getInt("request_id"));
        r.setUsername(rs.getString("username"));
        r.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null)
            r.setCreatedAt(ts.toLocalDateTime());
        return r;
    }
}
