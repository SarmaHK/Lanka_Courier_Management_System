package com.lankacourier.UI.dialogs;

import com.lankacourier.Controller.BranchController;
import com.lankacourier.Controller.EmployeeController;
import com.lankacourier.Model.Branch;
import com.lankacourier.Model.Employee;
import com.lankacourier.Auth.Session;
import com.lankacourier.Util.FormatUtil;
import com.lankacourier.Util.SwingUtils;
import com.lankacourier.Util.HashUtil;
import com.lankacourier.Util.ModernUI;
import com.lankacourier.UI.base.BaseDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmployeeDialog extends BaseDialog {

    private final EmployeeController employeeController = new EmployeeController();
    private final BranchController branchController = new BranchController();

    private final Employee existing;
    private final boolean isAdmin;

    // Fields
    private JTextField tfFirst;
    private JTextField tfLast;
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JTextField tfMobile;
    private JComboBox<String> cbRole;
    private JComboBox<Branch> cbBranch;

    public EmployeeDialog(Component parent, Employee existing) {
        super(parent, existing == null ? "Add Employee" : "Edit Employee");
        this.existing = existing;

        isAdmin = Session.getCurrentUser() != null &&
                "Admin".equalsIgnoreCase(Session.getCurrentUser().getRole());

        buildForm();
        prefill();
        applyRoleRules();
        buildButtons();
    }

    /* ================= FORM ================= */
    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfFirst = ModernUI.createTextField();
        tfLast = ModernUI.createTextField();
        tfUsername = ModernUI.createTextField();
        pfPassword = ModernUI.createPasswordField();
        tfMobile = ModernUI.createTextField();

        cbRole = new JComboBox<>(new String[] { "Admin", "Clerk", "Driver" });
        cbBranch = new JComboBox<>();

        // Load branches
        cbBranch.addItem(null);
        List<Branch> branches = branchController.fetchAll();
        if (branches != null)
            for (Branch b : branches)
                cbBranch.addItem(b);

        cbBranch.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value instanceof Branch ? ((Branch) value).getName() : "");
                return this;
            }
        });

        int y = 0;
        addRow("First Name", tfFirst, gbc, y++);
        addRow("Last Name", tfLast, gbc, y++);
        addRow("Username", tfUsername, gbc, y++);
        addRow("Password", pfPassword, gbc, y++);
        addRow("Role", cbRole, gbc, y++);
        addRow("Mobile", tfMobile, gbc, y++);
        addRow("Branch", cbBranch, gbc, y);
    }

    private void addRow(String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        formPanel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        formPanel.add(field, gbc);
    }

    /* ================= PREFILL ================= */
    private void prefill() {
        if (existing == null)
            return;

        tfFirst.setText(existing.getFirstName());
        tfLast.setText(existing.getLastName());
        tfUsername.setText(existing.getUsername());
        tfMobile.setText(FormatUtil.formatMobile(existing.getMobileNo()));
        cbRole.setSelectedItem(existing.getRole());

        for (int i = 0; i < cbBranch.getItemCount(); i++) {
            Branch b = cbBranch.getItemAt(i);
            if (b != null && b.getBranchId() == existing.getBranchId()) {
                cbBranch.setSelectedIndex(i);
                break;
            }
        }
    }

    /* ================= ROLE RULES ================= */
    private void applyRoleRules() {
        if (isAdmin)
            return;

        tfFirst.setEditable(false);
        tfLast.setEditable(false);
        tfUsername.setEditable(false);
        tfMobile.setEditable(false);
        cbRole.setEnabled(false);
        cbBranch.setEnabled(false);

        // Prevent editing Admin by non-admin
        if (existing != null && "Admin".equalsIgnoreCase(existing.getRole())) {
            SwingUtils.error(this, "You are not allowed to edit Admin users.");
            dispose();
        }
    }

    /* ================= BUTTONS ================= */
    private void buildButtons() {
        JButton btnSave = ModernUI.createPrimaryButton("Save");
        JButton btnCancel = ModernUI.createSecondaryButton("Cancel");

        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
    }

    /* ================= SAVE ================= */
    @Override
    protected void onSave() {

        String first = tfFirst.getText().trim();
        String last = tfLast.getText().trim();
        String user = tfUsername.getText().trim();
        String mobile = tfMobile.getText().trim();
        String role = cbRole.getSelectedItem().toString();
        String pass = new String(pfPassword.getPassword()).trim();

        if (first.isBlank() || last.isBlank() || user.isBlank()) {
            SwingUtils.error(this, "First, Last and Username are required");
            return;
        }

        if (!mobile.isBlank() && !mobile.matches("07\\d{8}")) {
            SwingUtils.error(this, "Invalid mobile number (07xxxxxxxx)");
            return;
        }

        Employee emp = existing == null ? new Employee() : existing;

        emp.setFirstName(first);
        emp.setLastName(last);
        emp.setUsername(user);
        emp.setRole(role);
        emp.setMobileNo(FormatUtil.formatMobile(mobile));

        if (!pass.isBlank())
            emp.setPasswordHash(HashUtil.hashPassword(pass));

        Branch sel = (Branch) cbBranch.getSelectedItem();
        emp.setBranchId(sel == null ? 0 : sel.getBranchId());

        boolean ok = existing == null
                ? employeeController.create(emp)
                : employeeController.update(emp);

        if (ok) {
            SwingUtils.info(this, "Employee saved successfully");
            dispose();
        } else {
            SwingUtils.error(this, "Failed to save employee");
        }
    }
}
