package com.lankacourier.UI;

import com.lankacourier.Auth.Session;
import com.lankacourier.Model.Employee;

import javax.swing.*;
import java.awt.*;

/**
 * Main application frame with background image (NO extra class)
 */
public class MainApp extends JFrame {

    private static final Color PRIMARY = new Color(30,136,229);

    private final DashboardPanel dashboardPanel = new DashboardPanel();

    public MainApp() {
        setTitle("Lanka Courier");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {

        // Load background image from resources
        Image bgImage = new ImageIcon(
                getClass().getResource("/images/bg.jpg")
        ).getImage();

        // 🔹 Background panel (INLINE, no new class)
        JPanel bgPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);

                // Optional dark overlay for readability
                g.setColor(new Color(0, 0, 0, 60));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        setContentPane(bgPanel);

        // Important for glass effect
        dashboardPanel.setOpaque(false);

        bgPanel.add(buildTopBar(), BorderLayout.NORTH);
        bgPanel.add(dashboardPanel, BorderLayout.CENTER);
    }

    /* ================= TOP BAR ================= */
    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(185,185,185,120));
        topBar.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(220,220,220)));
        topBar.setPreferredSize(new Dimension(0, 56));

        Employee user = Session.getCurrentUser();
        JLabel lblUser = new JLabel("Not signed in");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(new Color(40,40,40));

        if (user != null) {
            lblUser.setText(
                user.getFirstName() + " " + user.getLastName()
                + " • " + user.getRole()
            );
        }

        JButton btnLogout = new JButton("Logout");
        styleLogoutButton(btnLogout);

        btnLogout.addActionListener(e -> {
            Session.clear();
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        right.setOpaque(false);
        right.add(btnLogout);

        topBar.add(lblUser, BorderLayout.WEST);
        topBar.add(right, BorderLayout.EAST);

        return topBar;
    }

    /* ================= LOGOUT BUTTON ================= */
    private void styleLogoutButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(25,118,210));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(PRIMARY);
            }
        });
    }
}
