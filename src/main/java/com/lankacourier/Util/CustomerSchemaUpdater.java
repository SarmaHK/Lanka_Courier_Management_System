package com.lankacourier.Util;

import com.lankacourier.DB.dbconnection;
import java.sql.Connection;
import java.sql.Statement;

public class CustomerSchemaUpdater {
    public static void main(String[] args) {
        String sql = "ALTER TABLE customers ADD COLUMN mobile_no VARCHAR(15) UNIQUE AFTER contact_person_nic";

        try (Connection conn = dbconnection.getConnection();
                Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("Schema updated: Added mobile_no to customers table.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
