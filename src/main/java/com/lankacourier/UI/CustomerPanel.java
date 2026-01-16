package com.lankacourier.UI;

import com.lankacourier.Controller.CustomerController;
import com.lankacourier.Controller.CustomerMobileNumberController;
import com.lankacourier.Model.Customer;
import com.lankacourier.Model.CustomerMobileNumber;
import com.lankacourier.Util.SwingUtils;

import com.lankacourier.UI.base.AdminGuard;
import com.lankacourier.UI.base.SearchPopup;
import com.lankacourier.UI.components.ModernComboBox;
import com.lankacourier.UI.components.RoundedButton;
import com.lankacourier.UI.components.SearchChip;
import com.lankacourier.UI.dialogs.CustomerDialog;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CustomerPanel extends JPanel {

    /* ===== COLORS ===== */
    private static final Color BG = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER = new Color(220, 220, 220);
    private static final Color PRIMARY = new Color(30, 136, 229);
    private static final Color DANGER = new Color(229, 57, 53);

    private final CustomerController customerController = new CustomerController();
    private final CustomerMobileNumberController mobileController = new CustomerMobileNumberController();

    private JTable table;
    private DefaultTableModel model;

    private JCheckBox cbSelectAll;
    private JLabel lblSelected;

    private SearchPopup searchPopup;
    private ModernComboBox<String> cbFilter;
    private ModernComboBox<String> cbSort;

    private List<Customer> cache = new ArrayList<>();

    public CustomerPanel() {

        setLayout(new BorderLayout(12, 12));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        /* ================= TOP BAR (GRADIENT) ================= */
        JPanel topBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(30, 136, 229), getWidth(), getHeight(),
                        new Color(21, 101, 192)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topBar.setPreferredSize(new Dimension(0, 80));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        SearchChip searchChip = new SearchChip();
        searchChip.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showSearch(searchChip);
            }
        });

        JLabel title = new JLabel("Customer Details", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        // Make buttons white/transparent to fit on gradient
        RoundedButton btnAdd = new RoundedButton("+ Add Customer", Color.WHITE);
        btnAdd.setForeground(PRIMARY); // Blue text on white button
        btnAdd.addActionListener(e -> openDialog(null));

        topBar.add(searchChip, BorderLayout.WEST);
        topBar.add(title, BorderLayout.CENTER);
        topBar.add(btnAdd, BorderLayout.EAST);

        /* ================= SECOND BAR (ACTIONS) ================= */
        JPanel secondBar = new JPanel(new BorderLayout());
        secondBar.setOpaque(false);
        secondBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        cbSelectAll = new JCheckBox("Select All");
        lblSelected = new JLabel("0 Selected");
        lblSelected.setFont(new Font("Segoe UI", Font.BOLD, 13));

        RoundedButton btnEdit = new RoundedButton("Edit", PRIMARY);
        RoundedButton btnDelete = new RoundedButton("Delete", DANGER);
        RoundedButton btnRefresh = new RoundedButton("Refresh", PRIMARY);

        btnDelete.setEnabled(AdminGuard.isAdmin());

        left.add(lblSelected);
        left.add(cbSelectAll);
        left.add(btnEdit);
        left.add(btnDelete);
        left.add(btnRefresh);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        cbFilter = new ModernComboBox<>(new String[] { "All Types", "individual", "business" });
        cbSort = new ModernComboBox<>(
                new String[] { "Sort by Business Name", "Sort by Contact Name", "Sort by Email" });

        right.add(cbFilter);
        right.add(cbSort);

        secondBar.add(left, BorderLayout.WEST);
        secondBar.add(right, BorderLayout.EAST);

        /* ================= TABLE ================= */
        model = new DefaultTableModel(
                new Object[] {
                        "Select", "ID", "Type", "Business Name", "Contact Name", "Business ID", "Address", "Email",
                        "Mobile"
                }, 0) {
            @Override
            public Class<?> getColumnClass(int c) {
                return c == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 0;
            }
        };

        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        enableResponsiveResize(table, scroll);

        /* ================= CARD LAYOUT ================= */
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                null // Modern clean border
        ));

        // Assemble
        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setBackground(Color.WHITE);
        headerContainer.add(topBar, BorderLayout.NORTH);
        headerContainer.add(secondBar, BorderLayout.CENTER);

        card.add(headerContainer, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);

        /* ================= EVENTS ================= */
        cbSelectAll.addActionListener(e -> toggleSelectAll());
        model.addTableModelListener(this::onSelectionChanged);

        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> loadTable());

        cbFilter.addActionListener(e -> applyAll());
        cbSort.addActionListener(e -> applyAll());

        loadTable();
    }

    /* ================= DATA ================= */
    private void loadTable() {
        cache = customerController.fetchAll();
        applyAll();
    }

    private void applyAll() {

        model.setRowCount(0);

        String filter = cbFilter.getSelectedItem().toString();
        String sort = cbSort.getSelectedItem().toString();
        String query = searchPopup == null ? "" : searchPopup.getQuery().toLowerCase();

        List<Customer> list = new ArrayList<>();

        for (Customer c : cache) {

            if (!"All Types".equals(filter)
                    && !filter.equalsIgnoreCase(c.getCustomerType()))
                continue;

            if (!query.isEmpty() &&
                    !(safe(c.getBusinessName()).toLowerCase().contains(query)
                            || safe(c.getContactPersonName()).toLowerCase().contains(query) // ✅
                            || safe(c.getBusinessRegNo()).toLowerCase().contains(query)
                            || safe(c.getEmail()).toLowerCase().contains(query)
                            || safe(c.getAddress()).toLowerCase().contains(query)))
                continue;

            list.add(c);
        }

        Comparator<Customer> cmp;
        switch (sort) {
            case "Sort by Contact Name":
                cmp = Comparator.comparing(c -> safe(c.getContactPersonName()));
                break;
            case "Sort by Email":
                cmp = Comparator.comparing(c -> safe(c.getEmail()));
                break;
            default:
                cmp = Comparator.comparing(c -> safe(c.getBusinessName()));
        }

        list.sort(cmp);

        for (Customer c : list) {

            String mobile = "-";
            List<CustomerMobileNumber> ms = mobileController.fetchForCustomer(c.getCustomerId());
            if (!ms.isEmpty())
                mobile = ms.get(0).getMobileNo();

            model.addRow(new Object[] {
                    false,
                    String.format("%03d", c.getCustomerId()),
                    c.getCustomerType(),
                    safe(c.getBusinessName()),
                    safe(c.getContactPersonName()), // ✅
                    safe(c.getBusinessRegNo()),
                    safe(c.getAddress()),
                    safe(c.getEmail()),
                    mobile
            });
        }

        lblSelected.setText("0 Selected");
        cbSelectAll.setSelected(false);
    }

    /* ================= SELECTION ================= */
    private void toggleSelectAll() {
        boolean sel = cbSelectAll.isSelected();
        for (int i = 0; i < model.getRowCount(); i++)
            model.setValueAt(sel, i, 0);
    }

    private void onSelectionChanged(TableModelEvent e) {
        if (e.getColumn() != 0)
            return;

        int count = 0;
        boolean all = true;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (Boolean.TRUE.equals(model.getValueAt(i, 0)))
                count++;
            else
                all = false;
        }

        lblSelected.setText(count + " Selected");
        cbSelectAll.setSelected(all);
    }

    /* ================= ACTIONS ================= */
    private void openDialog(Customer existing) {
        new CustomerDialog(this, existing).setVisible(true);
        loadTable();
    }

    private void editSelected() {
        int row = getSingleCheckedRow();
        if (row < 0) {
            SwingUtils.error(this, "Select exactly ONE customer.");
            return;
        }
        int id = Integer.parseInt(model.getValueAt(row, 1).toString());
        openDialog(customerController.findById(id));
    }

    private void deleteSelected() {
        if (!AdminGuard.requireAdmin(this))
            return;

        int row = getSingleCheckedRow();
        if (row < 0) {
            SwingUtils.error(this, "Select exactly ONE customer.");
            return;
        }

        if (!SwingUtils.confirm(this, "Delete selected customer?"))
            return;

        int id = Integer.parseInt(model.getValueAt(row, 1).toString());
        customerController.delete(id);
        loadTable();
    }

    private int getSingleCheckedRow() {
        int row = -1, count = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (Boolean.TRUE.equals(model.getValueAt(i, 0))) {
                row = i;
                count++;
            }
        }
        return count == 1 ? row : -1;
    }

    private void showSearch(Component anchor) {
        if (searchPopup == null) {
            searchPopup = new SearchPopup(
                    "Search Customers",
                    new String[] {
                            "Business Name",
                            "Contact Name",
                            "Business ID",
                            "Email",
                            "Address"
                    });
        }
        searchPopup.show(anchor);
    }

    /* ================= UI HELPERS ================= */
    private void styleTable(JTable table) {

        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.getColumnModel().getColumn(0).setMaxWidth(70);
    }

    private void enableResponsiveResize(JTable table, JScrollPane scroll) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scroll.getViewport().addChangeListener(e -> {
            table.setAutoResizeMode(
                    scroll.getViewport().getWidth() > table.getPreferredSize().width
                            ? JTable.AUTO_RESIZE_ALL_COLUMNS
                            : JTable.AUTO_RESIZE_OFF);
        });
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
