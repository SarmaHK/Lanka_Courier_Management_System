package com.lankacourier.Util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ModernUI {

    public static JPanel createCard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 15, 15)));
        return p;
    }

    public static JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(new Color(100, 100, 100));
        return l;
    }

    public static JLabel createHeader(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(new Color(50, 50, 50));
        return l;
    }

    public static JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        Border defaultBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 8, 5, 8));
        Border focusedBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 136, 229), 2),
                new EmptyBorder(4, 7, 4, 7)); // Adjust padding for border width difference

        tf.setBorder(defaultBorder);

        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                tf.setBorder(focusedBorder);
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                tf.setBorder(defaultBorder);
            }
        });
        return tf;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        Border defaultBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 8, 5, 8));
        Border focusedBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 136, 229), 2),
                new EmptyBorder(4, 7, 4, 7));

        pf.setBorder(defaultBorder);
        pf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                pf.setBorder(focusedBorder);
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                pf.setBorder(defaultBorder);
            }
        });
        return pf;
    }

    public static JPanel createFormGroup(String labelText, JComponent input) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        p.add(createLabel(labelText), BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);
        return p;
    }

    public static JButton createPrimaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        Color normal = new Color(30, 136, 229);
        Color hover = new Color(21, 101, 192);
        b.setBackground(normal); // Primary
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(hover);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(normal);
            }
        });
        return b;
    }

    public static JButton createSecondaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(new Color(50, 50, 50));
        Color normal = new Color(230, 230, 230);
        Color hover = new Color(210, 210, 210);
        b.setBackground(normal); // Grey
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(hover);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(normal);
            }
        });
        return b;
    }
}
