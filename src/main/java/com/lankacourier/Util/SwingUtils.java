package com.lankacourier.Util;

import java.awt.Component;
import javax.swing.*;

public class SwingUtils {

    // Confirm dialog (YES / NO)
    public static boolean confirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(
                parent,
                message,
                "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    // Info dialog
    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Error dialog
    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void styleTextField(JTextField tf) {
        tf.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        tf.putClientProperty("JTextField.placeholderText", "Search...");
    }
}
