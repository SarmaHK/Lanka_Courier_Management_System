package com.lankacourier.UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.lankacourier.Auth.AuthController;
import com.lankacourier.Auth.Session;
import com.lankacourier.Controller.EmployeeController;
import com.lankacourier.Model.Employee;
import com.lankacourier.Util.FormatUtil;

public class LoginFrame extends JFrame {

    private final AuthController auth = new AuthController();
    private final EmployeeController employeeController = new EmployeeController();
    private final Preferences prefs = Preferences.userNodeForPackage(LoginFrame.class);

    // ✅ FIX: declare checkbox
    private JCheckBox cbRemember;

    // Background image panel
    private static class BackgroundPanel extends JPanel {
        private final Image bg;

        BackgroundPanel(Image bg) {
            this.bg = bg;
            setLayout(new GridBagLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public LoginFrame() {
        setTitle("Lanka Courier - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setLocationRelativeTo(null);
        initUI();
        checkInitialAdmin();
    }

    private void initUI() {

        // ===== Background =====
        Image bgImage = new ImageIcon(
                getClass().getResource("/images/bg.jpg")).getImage();

        BackgroundPanel wrapper = new BackgroundPanel(bgImage);
        setContentPane(wrapper);

        // ===== White glass card =====
        JPanel content = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 20, shadow = 8;

                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(
                        shadow, shadow,
                        getWidth() - shadow * 2,
                        getHeight() - shadow * 2,
                        arc, arc);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(
                        0, 0,
                        getWidth() - shadow * 2,
                        getHeight() - shadow * 2,
                        arc, arc);
                g2.dispose();
            }
        };
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        wrapper.add(content);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        content.setPreferredSize(new Dimension(
                (int) (screen.width * 0.5),
                (int) (screen.height * 0.5)));

        // ===== Form panel =====
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(false);

        // Left image
        ImageIcon imageIcon = new ImageIcon(
                getClass().getResource("/images/loginIMG.jpg"));
        Image img = imageIcon.getImage()
                .getScaledInstance(250, 300, Image.SCALE_SMOOTH);
        JLabel lblImage = new JLabel(new ImageIcon(img));

        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.gridy = 0;

        cgbc.gridx = 0;
        cgbc.insets = new Insets(0, 0, 0, 30);
        content.add(lblImage, cgbc);

        cgbc.gridx = 1;
        content.add(card, cgbc);

        // Hide image on small width
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                lblImage.setVisible(getWidth() >= 850);
            }
        });

        // ===== GridBag base =====
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Title
        JLabel lblTitle = new JLabel("Lanka Courier Login");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 25));
        card.add(lblTitle, gbc);

        // Username
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel lblUser = new JLabel("Username");
        JTextField tfUser = new JTextField(20);
        card.add(lblUser, gbc);

        gbc.gridx = 1;
        card.add(tfUser, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblPass = new JLabel("Password");
        JPasswordField pf = new JPasswordField(20);
        card.add(lblPass, gbc);

        gbc.gridx = 1;
        card.add(pf, gbc);

        // ===== Remember Me =====
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        cbRemember = new JCheckBox("Remember me");
        cbRemember.setOpaque(false);
        cbRemember.setFocusPainted(false);
        cbRemember.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(cbRemember, gbc);

        // ===== Forgot password =====
        Color linkNormal = new Color(30, 136, 229);
        Color linkHover = new Color(21, 101, 192);

        gbc.gridy++;
        JLabel lblForgot = new JLabel("Forgot password?");
        lblForgot.setForeground(linkNormal);
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblForgot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                lblForgot.setText("<html><u>Forgot password?</u></html>");
                lblForgot.setForeground(linkHover);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                lblForgot.setText("Forgot password?");
                lblForgot.setForeground(linkNormal);
            }

            public void mouseClicked(java.awt.event.MouseEvent e) {
                new ForgotPasswordFrame().setVisible(true);
            }
        });
        card.add(lblForgot, gbc);

        // ===== Login button =====
        gbc.gridy++;
        JButton btnLogin = new JButton("Login");
        btnLogin.setFocusPainted(false);
        btnLogin.setBackground(new Color(30, 136, 229));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Border baseBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0,
                        new Color(0, 0, 0, 0)),
                BorderFactory.createEmptyBorder(8, 20, 4, 20));
        Border hoverBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0,
                        new Color(0, 0, 0, 60)),
                BorderFactory.createEmptyBorder(8, 20, 4, 20));
        btnLogin.setBorder(baseBorder);

        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnLogin.setBorder(hoverBorder);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btnLogin.setBorder(baseBorder);
            }
        });
        card.add(btnLogin, gbc);

        // ===== Load remembered username =====
        String savedUser = prefs.get("rememberedUser", "");
        if (!savedUser.isBlank()) {
            tfUser.setText(savedUser);
            cbRemember.setSelected(true);
        }

        // ===== Login action =====
        btnLogin.addActionListener(e -> {
            String user = tfUser.getText().trim();
            String pass = new String(pf.getPassword());

            if (user.isBlank() || pass.isBlank()) {
                JOptionPane.showMessageDialog(this,
                        "Enter username and password");
                return;
            }

            if (!auth.login(user, pass)) {
                shake(btnLogin);
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password");
                return;
            }

            if (cbRemember.isSelected())
                prefs.put("rememberedUser", user);
            else
                prefs.remove("rememberedUser");

            Employee me = Session.getCurrentUser();
            JOptionPane.showMessageDialog(this,
                    "Welcome, " + (me == null ? user
                            : FormatUtil.safe(me.getFirstName()) + " " +
                                    FormatUtil.safe(me.getLastName())));

            SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
            dispose();
        });
    }

    // ===== Admin check =====
    private void checkInitialAdmin() {
        try {
            List<Employee> all = employeeController.fetchAll();
            boolean hasAdmin = false;

            if (all != null)
                for (Employee e : all)
                    if ("Admin".equalsIgnoreCase(e.getRole()))
                        hasAdmin = true;

            if (!hasAdmin)
                SwingUtilities.invokeLater(() -> new SetupFrame(this).setVisible(true));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Admin check failed");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    private void shake(javax.swing.JComponent c) {
        final int originalX = c.getLocation().x;
        final int originalY = c.getLocation().y;
        final int shakeDistance = 5;
        final int shakeSpeed = 5;

        javax.swing.Timer timer = new javax.swing.Timer(shakeSpeed, new java.awt.event.ActionListener() {
            int counter = 0;
            boolean direction = true;

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int x = direction ? originalX + shakeDistance : originalX - shakeDistance;
                c.setLocation(x, originalY);
                counter++;
                direction = !direction;

                if (counter >= 10) {
                    ((javax.swing.Timer) e.getSource()).stop();
                    c.setLocation(originalX, originalY);
                }
            }
        });
        timer.start();
    }
}
