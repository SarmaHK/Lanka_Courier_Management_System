package com.lankacourier.UI;

import com.lankacourier.Controller.EmployeeController;
import com.lankacourier.Controller.PasswordResetController;
import com.lankacourier.Model.Employee;
import com.lankacourier.Model.PasswordResetRequest;
import com.lankacourier.Util.HashUtil;
import com.lankacourier.Util.ModernUI;
import com.lankacourier.Util.SwingUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ForgotPasswordFrame extends JFrame {

    private final PasswordResetController resetController = new PasswordResetController();
    private final EmployeeController employeeController = new EmployeeController();

    private JTextField tfUser;
    private JPasswordField pfNew, pfConfirm;
    private JButton btnAction;
    private JLabel lblStatus;

    public ForgotPasswordFrame() {
        setTitle("Forgot Password");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(Color.WHITE);

        initUI();
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);
        main.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JLabel title = new JLabel("Reset Password", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(50, 50, 50));
        main.add(title, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(20, 0, 20, 0));

        tfUser = ModernUI.createTextField();
        pfNew = new JPasswordField(); // Stylize manually or create helper
        stylePass(pfNew);
        pfConfirm = new JPasswordField();
        stylePass(pfConfirm);

        // Initially disable password fields
        pfNew.setEnabled(false);
        pfConfirm.setEnabled(false);

        form.add(ModernUI.createFormGroup("Username", tfUser));
        form.add(Box.createVerticalStrut(15));
        form.add(ModernUI.createFormGroup("New Password", pfNew));
        form.add(Box.createVerticalStrut(15));
        form.add(ModernUI.createFormGroup("Confirm Password", pfConfirm));
        form.add(Box.createVerticalStrut(20));

        lblStatus = new JLabel("Enter username to check status", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(Color.GRAY);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(lblStatus);

        main.add(form, BorderLayout.CENTER);

        // Button
        btnAction = ModernUI.createPrimaryButton("Check / Request");
        btnAction.setPreferredSize(new Dimension(200, 45));

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(btnAction);
        main.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(main);

        // Logic
        btnAction.addActionListener(e -> handleAction());
    }

    private void stylePass(JPasswordField pf) {
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 8, 5, 8)));
    }

    private void handleAction() {
        String username = tfUser.getText().trim();
        if (username.isEmpty()) {
            SwingUtils.error(this, "Please enter your username.");
            return;
        }

        // If password fields are enabled, we are in RESET mode
        if (pfNew.isEnabled()) {
            performReset(username);
        } else {
            checkStatus(username);
        }
    }

    private void checkStatus(String username) {
        // First check if user exists
        Employee emp = findEmployee(username);
        if (emp == null) {
            SwingUtils.error(this, "User not found.");
            return;
        }

        PasswordResetRequest req = resetController.getStatus(username);
        if (req == null) {
            // No request exists -> Create one
            if (resetController.requestReset(username)) {
                lblStatus.setText("Request Pending Admin Approval");
                lblStatus.setForeground(new Color(230, 126, 34)); // Orange
                SwingUtils.info(this, "Reset request sent! Please wait for admin approval.");
            } else {
                SwingUtils.error(this, "Failed to send request.");
            }
        } else {
            // Request exists
            if ("PENDING".equalsIgnoreCase(req.getStatus())) {
                lblStatus.setText("Status: PENDING Approval");
                lblStatus.setForeground(new Color(230, 126, 34));
                SwingUtils.info(this, "Your request is still pending admin approval.");
            } else if ("APPROVED".equalsIgnoreCase(req.getStatus())) {
                lblStatus.setText("Status: APPROVED. Enter new password.");
                lblStatus.setForeground(new Color(39, 174, 96)); // Green

                // Enable Reset Mode
                pfNew.setEnabled(true);
                pfConfirm.setEnabled(true);
                tfUser.setEditable(false); // Lock username
                btnAction.setText("Reset Password");
            }
        }
    }

    private void performReset(String username) {
        String p1 = new String(pfNew.getPassword());
        String p2 = new String(pfConfirm.getPassword());

        if (p1.isEmpty() || p2.isEmpty()) {
            SwingUtils.error(this, "Password fields cannot be empty.");
            return;
        }

        if (!p1.equals(p2)) {
            SwingUtils.error(this, "Passwords do not match.");
            return;
        }

        // Update Password
        try {
            Employee emp = findEmployee(username);
            if (emp != null) {
                emp.setPasswordHash(HashUtil.hashPassword(p1));
                if (employeeController.update(emp)) {
                    // Mark request as completed
                    resetController.completeRequest(username);
                    SwingUtils.info(this, "Password successfully reset! You can now login.");
                    dispose();
                } else {
                    SwingUtils.error(this, "Failed to update password.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            SwingUtils.error(this, "Error: " + e.getMessage());
        }
    }

    // Helper to find employee by username (naive iter)
    private Employee findEmployee(String username) {
        List<Employee> list = employeeController.fetchAll();
        for (Employee e : list) {
            if (e.getUsername().equalsIgnoreCase(username))
                return e;
        }
        return null;
    }
}
