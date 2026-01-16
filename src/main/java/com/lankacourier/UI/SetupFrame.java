package com.lankacourier.UI;

import com.lankacourier.Controller.BranchController;
import com.lankacourier.Controller.EmployeeController;
import com.lankacourier.Model.Branch;
import com.lankacourier.Model.Employee;
import com.lankacourier.Util.SwingUtils;
import com.lankacourier.Util.HashUtil;
import com.lankacourier.Util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class SetupFrame extends JDialog {

    private final EmployeeController employeeController = new EmployeeController();
    private final BranchController branchController = new BranchController();

    public SetupFrame(Frame owner) {
        super(owner, "Initial Setup - Create Admin", true);
        initUI();
    }

    private void initUI() {
        setSize(480, 360);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(8,8));

        JPanel fields = new JPanel(new GridLayout(7,2,8,8));
        fields.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JTextField tfFirst = new JTextField();
        JTextField tfLast = new JTextField();
        JTextField tfUsername = new JTextField("admin");
        JPasswordField pfPassword = new JPasswordField();
        JPasswordField pfConfirm = new JPasswordField();
        JComboBox<Branch> cbBranch = new JComboBox<>();

        fields.add(new JLabel("First name:")); fields.add(tfFirst);
        fields.add(new JLabel("Last name:")); fields.add(tfLast);
        fields.add(new JLabel("Username:")); fields.add(tfUsername);
        fields.add(new JLabel("Password:")); fields.add(pfPassword);
        fields.add(new JLabel("Confirm password:")); fields.add(pfConfirm);
        fields.add(new JLabel("Assign Branch:")); fields.add(cbBranch);
        add(fields, BorderLayout.CENTER);

        // load branches (optional) — if none exist user can still create admin with branch_id = 1 or 0
        try {
            List<Branch> branches = branchController.fetchAll();
            cbBranch.addItem(null);
            if (branches != null) for (Branch b : branches) cbBranch.addItem(b);
            cbBranch.setRenderer(new DefaultListCellRenderer() {
                @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Branch) setText(((Branch)value).getName());
                    else setText("<no branch>");
                    return this;
                }
            });
        } catch (Exception ignored) {}

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCreate = new JButton("Create Admin");
        JButton btnCancel = new JButton("Exit");
        actions.add(btnCancel); actions.add(btnCreate);
        add(actions, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> {
            // close the whole app if user cancels initial setup
            dispose();
            System.exit(0);
        });

        btnCreate.addActionListener(e -> {
            String first = tfFirst.getText().trim();
            String last = tfLast.getText().trim();
            String username = tfUsername.getText().trim();
            String pw = new String(pfPassword.getPassword());
            String pw2 = new String(pfConfirm.getPassword());
            Branch selBranch = (Branch) cbBranch.getSelectedItem();

            if (first.isBlank() || last.isBlank() || username.isBlank()) {
                SwingUtils.error(this, "First name, last name and username are required.");
                return;
            }
            if (pw.isBlank()) { SwingUtils.error(this, "Password required."); return; }
            if (!pw.equals(pw2)) { SwingUtils.error(this, "Passwords do not match."); return; }
            if (!ValidationUtil.isStrongPassword(pw, 6, true, true)) {
                int res = JOptionPane.showConfirmDialog(this, "Password looks weak. Continue anyway?", "Weak password", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (res != JOptionPane.YES_OPTION) return;
            }

            try {
                // Check if username exists
                List<Employee> all = employeeController.fetchAll();
                if (all != null) {
                    for (Employee emp : all) {                          // renamed variable to 'emp' to avoid colliding with lambda param 'e'
                        if (emp != null && emp.getUsername() != null && emp.getUsername().trim().equalsIgnoreCase(username)) {
                            SwingUtils.error(this, "Username already exists. Choose another.");
                            return;
                        }
                    }
                }

                Employee admin = new Employee();
                admin.setFirstName(first);
                admin.setLastName(last);
                admin.setUsername(username);
                admin.setRole("Admin");
                admin.setMobileNo(""); // optional
                // assign branch id safely (if none selected, set to null or 0 depending on your model)
                if (selBranch == null) {
                    admin.setBranchId(1);
                } else {
                    admin.setBranchId(selBranch.getBranchId());
                }
                admin.setPasswordHash(HashUtil.hashPassword(pw));

                boolean ok = employeeController.create(admin);
                if (ok) {
                    SwingUtils.info(this, "Admin account created. You can now log in.");
                    dispose(); // close setup
                } else {
                    SwingUtils.error(this, "Failed to create admin. Check logs/DB.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtils.error(this, "Error while creating admin: " + ex.getMessage());
            }
        });
    }
}
