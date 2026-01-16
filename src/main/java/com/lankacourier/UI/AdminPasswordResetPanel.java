package com.lankacourier.UI;

import com.lankacourier.Controller.PasswordResetController;
import com.lankacourier.Model.PasswordResetRequest;
import com.lankacourier.Util.ModernUI;
import com.lankacourier.Util.SwingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminPasswordResetPanel extends JPanel {

    private final PasswordResetController controller = new PasswordResetController();
    private final DefaultTableModel model;
    private final JTable table;

    public AdminPasswordResetPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250)); // Modern BG
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        add(ModernUI.createHeader("Password Reset Requests"), BorderLayout.NORTH);

        // Table
        String[] cols = { "Request ID", "Username", "Status", "Date Requested" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);

        JPanel tableCard = ModernUI.createCard();
        tableCard.add(sp, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        // Actions
        JPanel points = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        points.setOpaque(false);

        JButton btnApprove = ModernUI.createPrimaryButton("Approve Request");
        JButton btnRefresh = ModernUI.createSecondaryButton("Refresh");

        points.add(btnRefresh);
        points.add(btnApprove);
        add(points, BorderLayout.SOUTH);

        // Listeners
        btnRefresh.addActionListener(e -> loadRequests());
        btnApprove.addActionListener(e -> approveSelected());

        loadRequests();
    }

    private void loadRequests() {
        model.setRowCount(0);
        List<PasswordResetRequest> list = controller.getPendingRequests();
        for (PasswordResetRequest r : list) {
            model.addRow(new Object[] {
                    r.getRequestId(),
                    r.getUsername(),
                    r.getStatus(),
                    r.getCreatedAt()
            });
        }
    }

    private void approveSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            SwingUtils.error(this, "Please select a request to approve.");
            return;
        }

        int reqId = (int) model.getValueAt(row, 0);
        String username = (String) model.getValueAt(row, 1);

        if (SwingUtils.confirm(this, "Approve password reset for user: " + username + "?")) {
            if (controller.approveRequest(reqId)) {
                SwingUtils.info(this, "Request Approved. User can now reset their password.");
                loadRequests();
            } else {
                SwingUtils.error(this, "Failed to approve request.");
            }
        }
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(30, 136, 229));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
    }
}
