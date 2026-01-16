package com.lankacourier.UI;

import com.lankacourier.Controller.BranchController;
import com.lankacourier.Controller.EmployeeController;
import com.lankacourier.Model.Branch;
import com.lankacourier.Model.Employee;
import com.lankacourier.Util.FormatUtil;
import com.lankacourier.Util.SwingUtils;

import com.lankacourier.UI.base.AdminGuard;
import com.lankacourier.UI.base.SearchPopup;
import com.lankacourier.UI.components.ModernComboBox;
import com.lankacourier.UI.components.RoundedButton;
import com.lankacourier.UI.components.SearchChip;
import com.lankacourier.UI.dialogs.EmployeeDialog;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class EmployeePanel extends JPanel {

    /* ===== COLORS ===== */
    private static final Color BG = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER = new Color(220, 220, 220);
    private static final Color PRIMARY = new Color(30, 136, 229);
    private static final Color DANGER = new Color(229, 57, 53);

    private final EmployeeController employeeController = new EmployeeController();
    private final BranchController branchController = new BranchController();

    private JTable table;
    private DefaultTableModel model;

    private JCheckBox cbSelectAll;
    private JLabel lblSelected;

    private ModernComboBox<String> cbFilter;
    private ModernComboBox<String> cbSort;

    private SearchPopup searchPopup;
    private List<Employee> cache = new ArrayList<>();

    public EmployeePanel() {

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

        JLabel title = new JLabel("Employee Details", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        RoundedButton btnAdd = new RoundedButton("+ Add Employee", Color.WHITE);
        btnAdd.setForeground(PRIMARY);
        btnAdd.setEnabled(AdminGuard.isAdmin());
        btnAdd.addActionListener(e -> {
            if (AdminGuard.requireAdmin(this))
                openForm(null);
        });

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

        btnEdit.setEnabled(AdminGuard.isAdmin());
        btnDelete.setEnabled(AdminGuard.isAdmin());

        left.add(lblSelected);
        left.add(cbSelectAll);
        left.add(btnEdit);
        left.add(btnDelete);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        cbFilter = new ModernComboBox<>(new String[] { "All Roles", "Admin", "Clerk", "Driver" });
        cbSort = new ModernComboBox<>(new String[] { "Sort by Name", "Sort by Role", "Sort by Branch" });

        right.add(cbFilter);
        right.add(cbSort);

        secondBar.add(left, BorderLayout.WEST);
        secondBar.add(right, BorderLayout.EAST);

        /* ================= TABLE ================= */
        model = new DefaultTableModel(
                new Object[] { "Select", "ID", "First Name", "Last Name", "Username", "Role", "Mobile", "Branch" }, 0) {
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
                null // Clean style
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
        cbSelectAll.addActionListener(e -> {
            boolean sel = cbSelectAll.isSelected();
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(sel, i, 0);
            }
        });

        model.addTableModelListener(this::onSelectionChanged);

        btnEdit.addActionListener(e -> editEmployee());
        btnDelete.addActionListener(e -> deleteEmployees());

        cbFilter.addActionListener(e -> applyAll());
        cbSort.addActionListener(e -> applyAll());

        loadData();
    }

    /* ================= TABLE STYLE ================= */
    private void styleTable(JTable table) {

        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setMaxWidth(70);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0
                            ? Color.WHITE
                            : new Color(245, 247, 250));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });

        JTableHeader h = table.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        h.setBackground(new Color(236, 240, 241));
        h.setForeground(new Color(44, 62, 80));
        h.setReorderingAllowed(false);
    }

    /* ================= RESPONSIVE ================= */
    private void enableResponsiveResize(JTable table, JScrollPane scroll) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        scroll.getViewport().addChangeListener(e -> {
            if (scroll.getViewport().getWidth() > table.getPreferredSize().width) {
                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            } else {
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            }
        });
    }

    /* ================= DATA ================= */
    private void loadData() {
        cache = employeeController.fetchAll();
        if (cache == null)
            cache = new ArrayList<>();
        applyAll();
    }

    private void applyAll() {
        model.setRowCount(0);

        String role = cbFilter.getSelectedItem().toString();
        String sort = cbSort.getSelectedItem().toString();
        String q = searchPopup == null ? "" : searchPopup.getQuery();

        List<Employee> list = new ArrayList<>();

        for (Employee e : cache) {

            if (!"All Roles".equals(role) &&
                    !role.equalsIgnoreCase(e.getRole()))
                continue;

            if (!q.isEmpty()) {
                boolean match = searchPopup.matches("First Name", e.getFirstName()) ||
                        searchPopup.matches("Last Name", e.getLastName()) ||
                        searchPopup.matches("Username", e.getUsername()) ||
                        searchPopup.matches("Mobile", e.getMobileNo());
                if (!match)
                    continue;
            }

            list.add(e);
        }

        Comparator<Employee> cmp;
        switch (sort) {
            case "Sort by Role":
                cmp = Comparator.comparing(e -> safe(e.getRole()));
                break;
            case "Sort by Branch":
                cmp = Comparator.comparing(e -> getBranchName(e.getBranchId()));
                break;
            default:
                cmp = Comparator.comparing(e -> safe(e.getFirstName()) + safe(e.getLastName()));
        }

        list.sort(cmp);

        for (Employee e : list) {
            model.addRow(new Object[] {
                    false,
                    e.getEmployeeId(),
                    FormatUtil.safe(e.getFirstName()),
                    FormatUtil.safe(e.getLastName()),
                    FormatUtil.safe(e.getUsername()),
                    FormatUtil.safe(e.getRole()),
                    FormatUtil.formatMobile(e.getMobileNo()),
                    getBranchName(e.getBranchId())
            });
        }

        lblSelected.setText("0 Selected");
        cbSelectAll.setSelected(false);
    }

    /* ================= SELECTION ================= */
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
        if (cbSelectAll.isSelected() != all)
            cbSelectAll.setSelected(all);
    }

    /* ================= ACTIONS ================= */
    private void editEmployee() {
        if (!AdminGuard.requireAdmin(this))
            return;

        int row = getSingleCheckedRow();
        if (row < 0) {
            SwingUtils.error(this, "Select exactly ONE employee.");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 1).toString());
        openForm(employeeController.findById(id));
    }

    private void deleteEmployees() {
        if (!AdminGuard.requireAdmin(this))
            return;

        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (Boolean.TRUE.equals(model.getValueAt(i, 0))) {
                ids.add(Integer.parseInt(model.getValueAt(i, 1).toString()));
            }
        }

        if (ids.isEmpty()) {
            SwingUtils.error(this, "Select employee(s).");
            return;
        }

        if (!SwingUtils.confirm(this, "Delete selected employee(s)?"))
            return;

        for (int id : ids)
            employeeController.delete(id);

        loadData();
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

    /* ================= HELPERS ================= */
    private void openForm(Employee existing) {
        new EmployeeDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                existing).setVisible(true);
        loadData();
    }

    private void showSearch(Component anchor) {
        if (searchPopup == null) {
            searchPopup = new SearchPopup(
                    "Search Employees",
                    new String[] { "First Name", "Last Name", "Username", "Mobile" });
        }
        searchPopup.show(anchor);
    }

    private String getBranchName(int id) {
        Branch b = branchController.findById(id);
        return b != null ? b.getName() : "";
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
