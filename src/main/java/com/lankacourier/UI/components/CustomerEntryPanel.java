package com.lankacourier.UI.components;

import com.lankacourier.Controller.CustomerController;
import com.lankacourier.Model.Customer;
import com.lankacourier.Util.SwingUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CustomerEntryPanel extends JPanel {

    private final CustomerController controller = new CustomerController();
    private Customer loadedCustomer = null;

    private JComboBox<String> cbType;
    private JTextField tfCustomerId, tfMobile, tfNic, tfName, tfAddress, tfEmail;
    private JTextField tfBusName, tfBusReg;
    private JPanel busPanel;

    public CustomerEntryPanel(String title) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230)),
                        title,
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 14),
                        new Color(30, 136, 229) // Primary Blue
                ),
                BorderFactory.createEmptyBorder(10, 15, 15, 15)));
        setBackground(Color.WHITE);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        // Use title from TitledBorder if possible, but we are removing the old border
        // style
        // So we might want to add a header label.
        // For now, let's just make the whole panel cleaner.

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Fields
        tfCustomerId = com.lankacourier.Util.ModernUI.createTextField();
        tfCustomerId.setEditable(true);
        tfCustomerId.setToolTipText("Enter ID and Tab to Search");
        // STRICT DIGITS ONLY FOR CUSTOMER ID (Max 3)
        ((javax.swing.text.AbstractDocument) tfCustomerId.getDocument())
                .setDocumentFilter(new com.lankacourier.Util.NumericDocumentFilter(false, 3));

        tfMobile = com.lankacourier.Util.ModernUI.createTextField();
        // STRICT DIGITS ONLY FOR MOBILE
        ((javax.swing.text.AbstractDocument) tfMobile.getDocument())
                .setDocumentFilter(new com.lankacourier.Util.NumericDocumentFilter(false));

        tfNic = com.lankacourier.Util.ModernUI.createTextField();
        tfName = com.lankacourier.Util.ModernUI.createTextField();
        tfAddress = com.lankacourier.Util.ModernUI.createTextField();
        tfEmail = com.lankacourier.Util.ModernUI.createTextField();

        cbType = new JComboBox<>(new String[] { "Individual", "Business" });
        cbType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbType.setBackground(Color.WHITE);

        // Business Fields
        tfBusName = com.lankacourier.Util.ModernUI.createTextField();
        tfBusReg = com.lankacourier.Util.ModernUI.createTextField();

        // Listeners
        // Listeners for Search (Focus Lost + Enter Key)
        tfMobile.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchByMobile();
            }
        });
        tfMobile.addActionListener(e -> searchByMobile());

        tfCustomerId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchById();
            }
        });
        tfCustomerId.addActionListener(e -> searchById());

        JButton btnClear = new JButton("Clear");
        btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnClear.setMargin(new Insets(2, 8, 2, 8));
        btnClear.setFocusable(false);
        btnClear.addActionListener(e -> loadCustomer(null));

        // ID Row
        JPanel idPanel = new JPanel(new BorderLayout(5, 0));
        idPanel.setOpaque(false);
        idPanel.add(tfCustomerId, BorderLayout.CENTER);
        idPanel.add(btnClear, BorderLayout.EAST);

        // Layout
        int row = 0;

        // Row 1: ID & Mobile
        addField(content, gbc, "Customer ID", idPanel, 0, row);
        addField(content, gbc, "Mobile No *", tfMobile, 1, row++);

        // Row 2: Type & NIC
        addField(content, gbc, "Customer Type", cbType, 0, row);
        addField(content, gbc, "NIC (Individual)", tfNic, 1, row++);

        // Row 3: Name & Email
        addField(content, gbc, "Name *", tfName, 0, row);
        addField(content, gbc, "Email", tfEmail, 1, row++);

        // Row 4: Address (Full width)
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        content.add(com.lankacourier.Util.ModernUI.createFormGroup("Address *", tfAddress), gbc);
        gbc.gridwidth = 1;

        // Business Section
        busPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        busPanel.setOpaque(false);
        busPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel p1 = com.lankacourier.Util.ModernUI.createFormGroup("Business Name", tfBusName);
        JPanel p2 = com.lankacourier.Util.ModernUI.createFormGroup("Business Reg No", tfBusReg);
        busPanel.add(p1);
        busPanel.add(p2);
        busPanel.setVisible(false);

        // Logic for Business Panel
        cbType.addActionListener(e -> {
            boolean isBus = "Business".equals(cbType.getSelectedItem());
            busPanel.setVisible(isBus);
            revalidate();
            repaint();
        });

        add(content, BorderLayout.NORTH);
        add(busPanel, BorderLayout.CENTER);
    }

    private void addField(JPanel p, GridBagConstraints gbc, String label, JComponent cmp, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        p.add(com.lankacourier.Util.ModernUI.createFormGroup(label, cmp), gbc);
    }

    private void searchById() {
        String idStr = tfCustomerId.getText().trim();
        if (idStr.isEmpty())
            return;

        try {
            int id = Integer.parseInt(idStr);
            Customer c = controller.findById(id);
            if (c != null) {
                loadCustomer(c);
                SwingUtils.info(this, "Customer Found: " + c.getContactPersonName());
            } else {
                SwingUtils.error(this, "Customer not found with ID: " + id);
                // Clear ID but keep other fields open for new entry?
                // Or maybe just clear loaded customer
                this.loadedCustomer = null;
            }
        } catch (NumberFormatException e) {
            SwingUtils.error(this, "Invalid Customer ID");
        }
    }

    private void searchByMobile() {
        String m = tfMobile.getText().trim();
        if (m.isEmpty())
            return;

        Customer c = controller.findByMobile(m);
        if (c != null) {
            loadCustomer(c);
            SwingUtils.info(this, "Customer Found: " + c.getContactPersonName());
        }
    }

    public void loadCustomer(Customer c) {
        this.loadedCustomer = c;
        if (c == null) {
            // clear fields
            tfCustomerId.setText("");
            tfMobile.setText("");
            tfNic.setText("");
            tfName.setText("");
            tfAddress.setText("");
            tfEmail.setText("");
            tfBusName.setText("");
            tfBusReg.setText("");
            return;
        }

        tfCustomerId.setText(String.valueOf(c.getCustomerId()));
        tfMobile.setText(c.getMobileNo());
        cbType.setSelectedItem(c.getCustomerType());
        tfNic.setText(c.getContactPersonNic());
        tfName.setText(c.getContactPersonName());
        tfAddress.setText(c.getAddress());
        tfEmail.setText(c.getEmail());
        tfBusName.setText(c.getBusinessName());
        tfBusReg.setText(c.getBusinessRegNo());

        notifyListeners();
    }

    public Customer getCustomerData() {
        // Return a Customer object with current data
        // If loadedCustomer exists, use its ID. Else ID=0 (New)
        Customer c = new Customer();
        if (loadedCustomer != null) {
            c.setCustomerId(loadedCustomer.getCustomerId());
        }

        c.setMobileNo(tfMobile.getText().trim());
        c.setCustomerType((String) cbType.getSelectedItem());
        c.setContactPersonName(tfName.getText().trim());
        c.setContactPersonNic(tfNic.getText().trim());
        c.setAddress(tfAddress.getText().trim());
        c.setEmail(tfEmail.getText().trim());

        if ("Business".equals(c.getCustomerType())) {
            c.setBusinessName(tfBusName.getText().trim());
            c.setBusinessRegNo(tfBusReg.getText().trim());
        }

        return c;
    }

    public JTextField getTfMobile() {
        return tfMobile;
    }

    public JTextField getTfName() {
        return tfName;
    }

    private java.util.List<Runnable> customerChangeListeners = new java.util.ArrayList<>();

    public void addCustomerChangeListener(Runnable r) {
        customerChangeListeners.add(r);
    }

    private void notifyListeners() {
        customerChangeListeners.forEach(Runnable::run);
    }
}
