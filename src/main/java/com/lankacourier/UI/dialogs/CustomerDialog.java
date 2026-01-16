package com.lankacourier.UI.dialogs;

import com.lankacourier.Controller.CustomerController;
import com.lankacourier.Controller.CustomerMobileNumberController;
import com.lankacourier.Model.Customer;
import com.lankacourier.Model.CustomerMobileNumber;
import com.lankacourier.UI.base.BaseDialog;
import com.lankacourier.Util.ModernUI;
import com.lankacourier.Util.SwingUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CustomerDialog extends BaseDialog {

    private final CustomerController customerController = new CustomerController();
    private final CustomerMobileNumberController mobileController = new CustomerMobileNumberController();
    private final Customer existing;

    private JComboBox<String> cbType;
    private JTextField tfBusinessName, tfBusinessRegNo;
    private JTextField tfContactName, tfContactNic, tfAddress, tfEmail, tfMobile;

    public CustomerDialog(Component parent, Customer existing) {
        super(parent, existing == null ? "Add Customer" : "Edit Customer");
        this.existing = existing;

        setSize(780, 580); // Wider for 2 columns
        setLocationRelativeTo(parent);

        // Clear default BaseDialog layout basics to take full control
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildHeader(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildForm());
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        add(buildActions(), BorderLayout.SOUTH);

        if (existing != null)
            fillData();
        toggleBusinessFields(); // Init state

        setVisible(true);
    }

    /* ================= HEADER ================= */
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(30, 136, 229), getWidth(), getHeight(),
                        new Color(21, 101, 192)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 24, 0, 24));

        JLabel title = new JLabel(existing == null ? "Add New Customer" : "Edit Customer", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Enter customer details below", SwingConstants.LEFT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(200, 220, 255));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(Box.createVerticalGlue());
        text.add(title);
        text.add(subtitle);
        text.add(Box.createVerticalGlue());

        header.add(text, BorderLayout.CENTER);
        return header;
    }

    /* ================= BODY ================= */
    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(24, 24, 24, 24));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 12, 20, 12);
        gc.weightx = 0.5;
        gc.anchor = GridBagConstraints.NORTH;

        // Init Fields
        cbType = new JComboBox<>(new String[] { "individual", "business" });
        styleCombo(cbType);

        tfBusinessName = ModernUI.createTextField();
        tfBusinessRegNo = ModernUI.createTextField();
        tfContactName = ModernUI.createTextField(); // Contact Person
        tfContactNic = ModernUI.createTextField();
        tfAddress = ModernUI.createTextField();
        tfEmail = ModernUI.createTextField();
        tfMobile = ModernUI.createTextField();

        // Row 1: Type Selection (Full Width)
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        p.add(ModernUI.createFormGroup("Customer Type", cbType), gc);

        // Row 2: Headers
        gc.gridy++;
        gc.gridwidth = 1;
        gc.insets = new Insets(10, 12, 10, 12);
        p.add(createSectionHeader("Business Details"), gc);

        gc.gridx = 1;
        p.add(createSectionHeader("Contact Information"), gc);

        // Row 3: Forms
        gc.gridy++;
        gc.gridx = 0; // Col 1
        gc.weighty = 1.0; // Fill height
        p.add(buildBusinessPanel(), gc);

        gc.gridx = 1; // Col 2
        p.add(buildContactPanel(), gc);

        cbType.addActionListener(e -> toggleBusinessFields());

        return p;
    }

    private JPanel buildBusinessPanel() {
        JPanel p = new JPanel(new GridLayout(0, 1, 0, 15));
        p.setOpaque(false);
        p.add(ModernUI.createFormGroup("Business Name", tfBusinessName));
        p.add(ModernUI.createFormGroup("Registration No", tfBusinessRegNo));
        return p;
    }

    private JPanel buildContactPanel() {
        JPanel p = new JPanel(new GridLayout(0, 1, 0, 15));
        p.setOpaque(false);
        p.add(ModernUI.createFormGroup("Contact Person", tfContactName));
        p.add(ModernUI.createFormGroup("NIC", tfContactNic));
        p.add(ModernUI.createFormGroup("Address", tfAddress));
        p.add(ModernUI.createFormGroup("Email", tfEmail));
        p.add(ModernUI.createFormGroup("Mobile", tfMobile));
        return p;
    }

    private JPanel buildActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.setBackground(new Color(245, 247, 250));
        p.setBorder(new EmptyBorder(12, 24, 12, 24));

        JButton btnCancel = new JButton("Cancel");
        styleBtn(btnCancel, false);
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = new JButton("Save Customer");
        styleBtn(btnSave, true);
        btnSave.addActionListener(e -> onSave());

        p.add(btnCancel);
        p.add(Box.createHorizontalStrut(10));
        p.add(btnSave);
        return p;
    }

    private JLabel createSectionHeader(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(new Color(30, 136, 229));
        l.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(240, 240, 240)));
        return l;
    }

    /* ================= LOGIC ================= */
    private void toggleBusinessFields() {
        boolean business = "business".equals(cbType.getSelectedItem());
        tfBusinessName.setEnabled(business);
        tfBusinessRegNo.setEnabled(business);

        if (business) {
            tfBusinessName.setBackground(Color.WHITE);
            tfBusinessRegNo.setBackground(Color.WHITE);
        } else {
            tfBusinessName.setBackground(new Color(245, 245, 245));
            tfBusinessRegNo.setBackground(new Color(245, 245, 245));
            tfBusinessName.setText("");
            tfBusinessRegNo.setText("");
        }
    }

    private void fillData() {
        cbType.setSelectedItem(existing.getCustomerType());
        tfBusinessName.setText(existing.getBusinessName());
        tfBusinessRegNo.setText(existing.getBusinessRegNo());
        tfContactName.setText(existing.getContactPersonName());
        tfContactNic.setText(existing.getContactPersonNic());
        tfAddress.setText(existing.getAddress());
        tfEmail.setText(existing.getEmail());

        List<CustomerMobileNumber> mobiles = mobileController.fetchForCustomer(existing.getCustomerId());
        if (!mobiles.isEmpty())
            tfMobile.setText(mobiles.get(0).getMobileNo());
    }

    @Override
    protected void onSave() {
        String type = cbType.getSelectedItem().toString();

        if (tfContactName.getText().isBlank()) {
            SwingUtils.error(this, "Contact Name is required");
            return;
        }
        if (tfAddress.getText().isBlank()) {
            SwingUtils.error(this, "Address is required");
            return;
        }
        if (tfEmail.getText().isBlank()) {
            SwingUtils.error(this, "Email is required");
            return;
        }
        if (tfMobile.getText().isBlank()) {
            SwingUtils.error(this, "Mobile is required");
            return;
        }

        if ("business".equals(type) && tfBusinessName.getText().isBlank()) {
            SwingUtils.error(this, "Business Name is required");
            return;
        }

        Customer c = existing == null ? new Customer() : existing;
        c.setCustomerType(type);
        c.setBusinessName("business".equals(type) ? tfBusinessName.getText().trim() : null);
        c.setBusinessRegNo("business".equals(type) ? tfBusinessRegNo.getText().trim() : null);
        c.setContactPersonName(tfContactName.getText().trim());
        c.setContactPersonNic(tfContactNic.getText().trim());
        c.setAddress(tfAddress.getText().trim());
        c.setEmail(tfEmail.getText().trim());

        boolean ok = existing == null ? customerController.create(c) : customerController.update(c);

        if (!ok) {
            SwingUtils.error(this, "Failed to save.");
            return;
        }

        // Mobile
        List<CustomerMobileNumber> mobiles = mobileController.fetchForCustomer(c.getCustomerId());
        if (mobiles.isEmpty()) {
            CustomerMobileNumber m = new CustomerMobileNumber();
            m.setCustomerId(c.getCustomerId());
            m.setMobileNo(tfMobile.getText().trim());
            mobileController.create(m);
        } else {
            CustomerMobileNumber m = mobiles.get(0);
            m.setMobileNo(tfMobile.getText().trim());
            mobileController.update(m);
        }

        SwingUtils.info(this, "Saved successfully.");
        dispose();
    }

    /* ================= STYLES ================= */
    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBackground(Color.WHITE);
        cb.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private void styleBtn(JButton b, boolean primary) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        if (primary) {
            b.setBackground(new Color(30, 136, 229));
            b.setForeground(Color.WHITE);
        } else {
            b.setBackground(Color.WHITE);
            b.setForeground(new Color(50, 50, 50));
        }
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primary ? new Color(30, 136, 229) : new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)));
    }
}
