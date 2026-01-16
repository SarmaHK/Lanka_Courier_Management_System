package com.lankacourier;

import com.lankacourier.UI.LoginFrame;

public class Main {
    public static void main(String[] args) {
        // Initialize Database Tables (e.g., District Distances)
        try {
            new com.lankacourier.Controller.DistanceController().init();
            new com.lankacourier.Controller.RatesController().init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                LoginFrame lf = new LoginFrame();
                lf.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "Failed to start Login UI: " + ex.getMessage(),
                        "Startup error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}