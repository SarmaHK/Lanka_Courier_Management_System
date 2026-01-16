package com.lankacourier.UI;

import com.lankacourier.Controller.*;
import com.lankacourier.Model.*;
import com.lankacourier.UI.components.RoundedButton;
import com.lankacourier.Util.FormatUtil;
import com.lankacourier.Util.SwingUtils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ParcelTrackingPanel extends JPanel {

    /* ===================== STATUS OPTIONS ===================== */
    private static final String[] TRACKING_STATUSES = {
            "IN TRANSIT", "OUT FOR DELIVERY", "DELIVERED", "CANCELLED", "RETURNED"
    };

    /* ===================== CONTROLLERS ===================== */
    private final ParcelController parcelController = new ParcelController();
    private final TrackingHistoryController historyController = new TrackingHistoryController();
    private final BranchController branchController = new BranchController();
    private final EmployeeController employeeController = new EmployeeController();

    /* ===================== COMPONENTS ===================== */
    private final JTextField tfTracking;
    private final JLabel lblParcelInfo = new JLabel("Enter a Tracking ID to view history");

    private final DefaultTableModel historyModel = new DefaultTableModel(
            new String[] { "Time", "Branch", "Employee", "Status" }, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable historyTable = new JTable(historyModel);

    private RoundedButton btnAdd;
    private RoundedButton btnEdit;
    private RoundedButton btnDelete;

    private Parcel currentParcel;

    public ParcelTrackingPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        /* ================= HEADER ================= */
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
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Parcel Tracking", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        // Search Box in Header
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);

        tfTracking = new JTextField(15);
        tfTracking.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tfTracking.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        RoundedButton btnSearch = new RoundedButton("Track", Color.WHITE);
        btnSearch.setForeground(new Color(30, 136, 229));
        btnSearch.setPreferredSize(new Dimension(80, 32));

        searchPanel.add(new JLabel("<html><font color='white'>Tracking ID:</font></html>"));
        searchPanel.add(tfTracking);
        searchPanel.add(btnSearch);

        header.add(title, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        /* ================= BODY (CARD) ================= */
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Info Section
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(240, 248, 255));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblParcelInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblParcelInfo.setForeground(new Color(50, 50, 50));
        infoPanel.add(lblParcelInfo, BorderLayout.CENTER);

        // Table Section
        styleTable();
        JScrollPane scroll = new JScrollPane(historyTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scroll.getViewport().setBackground(Color.WHITE);

        // Actions Section
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        btnAdd = new RoundedButton("Add Event", new Color(46, 204, 113));
        btnEdit = new RoundedButton("Correct Event", new Color(52, 152, 219));
        btnDelete = new RoundedButton("Cancel Event", new Color(231, 76, 60));

        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);

        actions.add(btnAdd);
        actions.add(btnEdit);
        actions.add(btnDelete);

        // Assemble Card
        JPanel body = new JPanel(new BorderLayout(0, 20));
        body.setOpaque(false);
        body.add(infoPanel, BorderLayout.NORTH);
        body.add(scroll, BorderLayout.CENTER);
        body.add(actions, BorderLayout.SOUTH);

        card.add(body, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        /* ================= EVENTS ================= */
        btnSearch.addActionListener(e -> {
            searchTracking();
            btnAdd.setEnabled(currentParcel != null);
        });

        tfTracking.addActionListener(e -> btnSearch.doClick());

        historyTable.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = historyTable.getSelectedRow() >= 0;
            btnEdit.setEnabled(selected);
            btnDelete.setEnabled(selected);
        });

        btnAdd.addActionListener(e -> openEventDialog("Add Tracking Event"));
        btnEdit.addActionListener(e -> openEventDialog("Correct Tracking Event"));
        btnDelete.addActionListener(e -> softCancel());
    }

    /* ===================== LOGIC ===================== */

    private void searchTracking() {
        String tid = tfTracking.getText().trim();
        if (tid.isBlank()) {
            SwingUtils.error(this, "Enter tracking ID");
            return;
        }

        Parcel p = parcelController.findByTrackingId(tid);
        if (p == null) {
            SwingUtils.error(this, "Parcel not found");
            lblParcelInfo.setText("No parcel found with ID: " + tid);
            historyModel.setRowCount(0);
            currentParcel = null;
            return;
        }

        currentParcel = p;

        lblParcelInfo.setText(
                "<html><b style='font-size:16px; color:#2c3e50'>Parcel found</b><br>" +
                        "Current Status: <b style='color:#1e88e5'>" + FormatUtil.safe(p.getStatus()) + "</b>" +
                        " &nbsp; | &nbsp; Price: <b>" + FormatUtil.formatPrice(p.getPriceLkr()) + "</b>" +
                        " &nbsp; | &nbsp; Sender: " + p.getSenderId() +
                        "</html>");

        historyModel.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<TrackingHistory> list = historyController.fetchByParcelId(p.getParcelId());

        if (list != null) {
            for (TrackingHistory h : list) {
                Branch b = branchController.findById(h.getBranchId());
                Employee e = h.getEmployeeId() == null ? null : employeeController.findById(h.getEmployeeId());

                historyModel.addRow(new Object[] {
                        h.getTimestamp() == null ? "" : h.getTimestamp().format(fmt),
                        b == null ? "" : b.getName(),
                        e == null ? "" : e.getFirstName(),
                        FormatUtil.safe(h.getStatus())
                });
            }
        }
    }

    private void openEventDialog(String title) {
        if (currentParcel == null)
            return;

        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        d.setSize(450, 300);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<Branch> cbBranch = new JComboBox<>();
        JComboBox<Employee> cbEmployee = new JComboBox<>();
        JComboBox<String> cbStatus = new JComboBox<>(TRACKING_STATUSES);

        branchController.fetchAll().forEach(cbBranch::addItem);
        employeeController.fetchAll().forEach(cbEmployee::addItem);

        // Styling
        styleCombo(cbBranch);
        styleCombo(cbEmployee);
        styleCombo(cbStatus);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10, 10, 10, 10);
        gc.weightx = 1.0;

        gc.gridx = 0;
        gc.gridy = 0;
        p.add(new JLabel("Branch:"), gc);
        gc.gridx = 1;
        p.add(cbBranch, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        p.add(new JLabel("Employee:"), gc);
        gc.gridx = 1;
        p.add(cbEmployee, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        p.add(new JLabel("Status:"), gc);
        gc.gridx = 1;
        p.add(cbStatus, gc);

        JButton btnSave = new JButton("Save");
        btnSave.setBackground(new Color(30, 136, 229));
        btnSave.setForeground(Color.WHITE);

        btnSave.addActionListener(e -> {
            TrackingHistory h = new TrackingHistory();
            h.setParcelId(currentParcel.getParcelId());
            h.setBranchId(
                    cbBranch.getSelectedItem() == null ? null : ((Branch) cbBranch.getSelectedItem()).getBranchId());
            h.setEmployeeId(cbEmployee.getSelectedItem() == null ? null
                    : ((Employee) cbEmployee.getSelectedItem()).getEmployeeId());
            h.setStatus((String) cbStatus.getSelectedItem());
            h.setTimestamp(java.time.LocalDateTime.now());

            if (historyController.create(h)) {
                SwingUtils.info(d, "Tracking event saved");
                d.dispose();
                searchTracking();
            } else {
                SwingUtils.error(d, "Save failed");
            }
        });

        d.add(p, BorderLayout.CENTER);
        d.add(btnSave, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void softCancel() {
        if (currentParcel == null)
            return;
        if (JOptionPane.showConfirmDialog(this, "Mark tracking as CANCELLED?", "Confirm",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;

        TrackingHistory h = new TrackingHistory();
        h.setParcelId(currentParcel.getParcelId());
        h.setStatus("CANCELLED");
        h.setTimestamp(java.time.LocalDateTime.now());

        if (historyController.create(h)) {
            SwingUtils.info(this, "Tracking cancelled");
            searchTracking();
        } else {
            SwingUtils.error(this, "Operation failed");
        }
    }

    /* ===================== STYLES ===================== */

    private void styleTable() {
        historyTable.setRowHeight(36);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        historyTable.setSelectionBackground(new Color(230, 240, 255));
        historyTable.setSelectionForeground(Color.BLACK);
        historyTable.setFillsViewportHeight(true);
        historyTable.setShowVerticalLines(false);
        historyTable.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = historyTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        historyTable.getColumnModel().getColumn(3).setCellRenderer(new StatusBadgeRenderer());
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setBackground(Color.WHITE);
    }
}

class StatusBadgeRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean sel, boolean foc, int row,
            int col) {
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, sel, foc, row, col);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setOpaque(true);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        Color bg = Color.LIGHT_GRAY, fg = Color.DARK_GRAY;

        if (value != null) {
            switch (value.toString()) {
                case "IN TRANSIT":
                    bg = new Color(210, 225, 245);
                    fg = new Color(30, 90, 180);
                    break;
                case "OUT FOR DELIVERY":
                    bg = new Color(255, 230, 200);
                    fg = new Color(180, 100, 20);
                    break;
                case "DELIVERED":
                    bg = new Color(210, 240, 220);
                    fg = new Color(20, 140, 70);
                    break;
                case "CANCELLED":
                case "RETURNED":
                    bg = new Color(255, 210, 210);
                    fg = new Color(160, 40, 40);
                    break;
            }
        }
        l.setBackground(sel ? table.getSelectionBackground() : bg);
        l.setForeground(fg);
        l.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        return l;
    }
}
