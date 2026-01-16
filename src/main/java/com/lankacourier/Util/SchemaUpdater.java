package com.lankacourier.Util;

import com.lankacourier.DB.dbconnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class SchemaUpdater {
    public static void main(String[] args) {
        try (Connection conn = dbconnection.getConnection();
                Statement stmt = conn.createStatement()) {

            String sql = "ALTER TABLE parcels ADD COLUMN distance_km DECIMAL(10,2) DEFAULT 0.00";
            try {
                stmt.executeUpdate(sql);
                System.out.println("Schema updated successfully: Added distance_km column.");
            } catch (SQLException e) {
                if (!e.getMessage().contains("Duplicate column name"))
                    e.printStackTrace();
            }

            String sql2 = "ALTER TABLE branches ADD COLUMN branch_distance DOUBLE DEFAULT 0.0";
            try {
                stmt.executeUpdate(sql2);
                System.out.println("Schema updated successfully: Added branch_distance column.");
            } catch (SQLException e) {
                if (!e.getMessage().contains("Duplicate column name"))
                    e.printStackTrace();
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate column name")) {
                System.out.println("Column distance_km already exists.");
            } else {
                e.printStackTrace();
            }
        }
    }
}
