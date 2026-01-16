package com.lankacourier.DAO;

import com.lankacourier.DB.dbconnection;

import java.sql.*;

public class DistanceDAO {

    public void initTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS district_distances (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "district_from VARCHAR(50), " +
                "district_to VARCHAR(50), " +
                "distance_km DOUBLE, " +
                "UNIQUE KEY unique_dist (district_from, district_to)" +
                ")";

        try (Connection conn = dbconnection.getConnection();
                Statement stmt = conn.createStatement()) {

            stmt.execute(createTable);

            // Seed Data if empty
            if (getCount() == 0) {
                seedData();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getCount() {
        try (Connection conn = dbconnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM district_distances")) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void seedData() {
        // District Distances (User Provided Matrix)
        // Colombo
        insert("Colombo", "Kandy", 115);
        insert("Colombo", "Galle", 120);
        insert("Colombo", "Matara", 160);
        insert("Colombo", "Jaffna", 400);

        // Kandy
        insert("Kandy", "Galle", 235);
        insert("Kandy", "Matara", 275);
        insert("Kandy", "Jaffna", 375);

        // Galle
        insert("Galle", "Matara", 45);
        insert("Galle", "Jaffna", 585);

        // Matara
        insert("Matara", "Jaffna", 610);
    }

    // Insert bidirectional (or just handle retrieval logic)
    // Here we insert A->B. Retrieval should check A->B OR B->A
    private void insert(String from, String to, double dist) {
        String sql = "INSERT IGNORE INTO district_distances (district_from, district_to, distance_km) VALUES (?, ?, ?)";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, from);
            ps.setString(2, to);
            ps.setDouble(3, dist);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getDistance(String d1, String d2) {
        if (d1 == null || d2 == null || d1.equalsIgnoreCase(d2))
            return 0;

        String sql = "SELECT distance_km FROM district_distances WHERE " +
                "(district_from = ? AND district_to = ?) OR " +
                "(district_from = ? AND district_to = ?)";

        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d1);
            ps.setString(2, d2);
            ps.setString(3, d2);
            ps.setString(4, d1);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getDouble("distance_km");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Default if not found
    }
}
