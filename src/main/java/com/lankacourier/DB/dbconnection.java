package com.lankacourier.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbconnection {

    private static final String URL = "jdbc:mysql://localhost:3306/Lanka_Courier_Management?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";          
    private static final String PASS = "H@bi2307";      

    // Use this method everywhere
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Database connection FAILED!");
            e.printStackTrace();
            return null;
        }
    }

    // Only for testing
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Connection SUCCESS!");
            } else {
                System.out.println("Connection FAILED!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
