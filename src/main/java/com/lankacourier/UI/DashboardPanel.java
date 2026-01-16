package com.lankacourier.UI;

import javax.swing.*;
import java.awt.*;

/**
 * Modern Dashboard Panel (FIXED & STABLE)
 */
public class DashboardPanel extends JPanel {

    // ===== Card names =====
    public static final String CARD_PROFILE = "profile";
    public static final String CARD_EMP = "employees";
    public static final String CARD_CUST = "customers";
    public static final String CARD_BRANCH = "branches";
    public static final String CARD_PARCEL = "parcels";
    public static final String CARD_RATES = "rates";
    public static final String CARD_TRACK = "tracking";
    public static final String CARD_ADMIN = "admin_reset";

    /* ===== Colors ===== */
    private static final Color PRIMARY = new Color(30, 136, 229);
    private static final Color BG = new Color(245, 247, 250);
    private static final Color SIDEBAR = Color.WHITE;
    private static final Color HOVER = new Color(227, 242, 253);
    private static final Color TEXT = new Color(38, 50, 56);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    private JLabel lblHeaderTitle;

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);

        JPanel mainArea = buildMainArea(); // ✅ HEADER FIRST
        JPanel sidebar = buildSidebar(); // ✅ SAFE NOW

        add(sidebar, BorderLayout.WEST);
        add(mainArea, BorderLayout.CENTER);

        // default view
        cardLayout.show(cards, CARD_EMP);
        lblHeaderTitle.setText("Employees");
    }

    /* ================= MAIN AREA ================= */
    private JPanel buildMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        lblHeaderTitle = new JLabel("Dashboard");
        lblHeaderTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeaderTitle.setForeground(TEXT);

        JLabel lblUser = new JLabel("Logged in");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(Color.GRAY);

        header.add(lblHeaderTitle, BorderLayout.WEST);
        header.add(lblUser, BorderLayout.EAST);

        // Cards
        cards.setBackground(BG);
        cards.add(new ProfilePanel(), CARD_PROFILE);
        cards.add(new EmployeePanel(), CARD_EMP);
        cards.add(new CustomerPanel(), CARD_CUST);
        cards.add(new BranchPanel(), CARD_BRANCH);
        cards.add(new ParcelPanel(), CARD_PARCEL);
        cards.add(new RatesPanel(), CARD_RATES);
        cards.add(new ParcelTrackingPanel(), CARD_TRACK);
        cards.add(new ParcelTrackingPanel(), CARD_TRACK);

        if (isAdmin()) {
            cards.add(new AdminPasswordResetPanel(), CARD_ADMIN);
        }

        main.add(header, BorderLayout.NORTH);
        main.add(cards, BorderLayout.CENTER);

        return main;
    }

    /* ================= SIDEBAR ================= */
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        JLabel title = new JLabel("Lanka Courier");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.BLACK);
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        sidebar.add(title);

        JButton btnProfile = navBtn("My Profile", CARD_PROFILE);
        JButton btnEmp = navBtn("Employees", CARD_EMP);
        JButton btnCust = navBtn("Customers", CARD_CUST);
        JButton btnBranch = navBtn("Branches", CARD_BRANCH);
        JButton btnParcel = navBtn("Parcels", CARD_PARCEL);
        JButton btnRates = navBtn("Rates", CARD_RATES);
        JButton btnTrack = navBtn("Tracking", CARD_TRACK);

        sidebar.add(btnProfile);
        sidebar.add(btnEmp);
        sidebar.add(btnCust);
        sidebar.add(btnBranch);
        sidebar.add(btnParcel);
        sidebar.add(btnRates);
        sidebar.add(btnTrack);

        sidebar.add(Box.createVerticalGlue());

        if (isAdmin()) {
            JButton btnAdmin = navBtn("Admin Reset", CARD_ADMIN);
            sidebar.add(btnAdmin);
        }
        sidebar.add(Box.createVerticalStrut(10));

        setActive(btnEmp);

        return sidebar;
    }

    private boolean isAdmin() {
        com.lankacourier.Model.Employee me = com.lankacourier.Auth.Session.getCurrentUser();
        return me != null && "Admin".equalsIgnoreCase(me.getRole());
    }

    /* ================= NAV BUTTON ================= */
    private JButton navBtn(String text, String card) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.setBackground(SIDEBAR);
        btn.setForeground(TEXT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btn.addActionListener(e -> {
            cardLayout.show(cards, card);
            lblHeaderTitle.setText(text);
            setActive(btn);
        });

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.getBackground() != PRIMARY)
                    btn.setBackground(HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn.getBackground() != PRIMARY)
                    btn.setBackground(SIDEBAR);
            }
        });

        return btn;
    }

    /* ================= ACTIVE STATE ================= */
    private void setActive(JButton active) {
        for (Component c : active.getParent().getComponents()) {
            if (c instanceof JButton b) {
                b.setBackground(SIDEBAR);
                b.setForeground(TEXT);
            }
        }
        active.setBackground(PRIMARY);
        active.setForeground(Color.WHITE);
    }
}
