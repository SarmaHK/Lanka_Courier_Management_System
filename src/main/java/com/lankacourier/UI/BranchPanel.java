package com.lankacourier.UI;

import com.lankacourier.Controller.BranchController;
import com.lankacourier.Model.Branch;
import com.lankacourier.Util.SwingUtils;

import com.lankacourier.UI.base.AdminGuard;
import com.lankacourier.UI.base.SearchPopup;
import com.lankacourier.UI.components.ModernComboBox;
import com.lankacourier.UI.components.RoundedButton;
import com.lankacourier.UI.components.SearchChip;
import com.lankacourier.UI.dialogs.BranchDialog;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BranchPanel extends JPanel {

    /* ===== COLORS ===== */
    private static final Color BG = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER = new Color(220, 220, 220);
    private static final Color PRIMARY = new Color(30, 136, 229);
    private static final Color DANGER = new Color(229, 57, 53);

    private final BranchController controller = new BranchController();

    private JTable table;
    private DefaultTableModel model;

    private JCheckBox cbSelectAll;
    private JLabel lblSelected;

    private SearchPopup searchPopup;
    private ModernComboBox<String> cbFilter;
    private ModernComboBox<String> cbSort;

    private List<Branch> cache = new ArrayList<>();

    public BranchPanel() {

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

        JLabel title = new JLabel("Branch Details", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        RoundedButton btnAdd = new RoundedButton("+ Add Branch", Color.WHITE);
        btnAdd.setForeground(PRIMARY);
        btnAdd.setEnabled(AdminGuard.isAdmin());
        btnAdd.addActionListener(e -> {
            if (AdminGuard.requireAdmin(this))
                openAddDialog();
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
        RoundedButton btnRefresh = new RoundedButton("Refresh", PRIMARY);

        btnEdit.setEnabled(AdminGuard.isAdmin());
        btnDelete.setEnabled(AdminGuard.isAdmin());

        left.add(lblSelected);
        left.add(cbSelectAll);
        left.add(btnEdit);
        left.add(btnDelete);
        left.add(btnRefresh);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        cbFilter = new ModernComboBox<>(new String[] { "All Types", "Main", "Sub", "General" });
        cbSort = new ModernComboBox<>(new String[] { "Sort by Name", "Sort by District" });

        right.add(cbFilter);
        right.add(cbSort);

        secondBar.add(left, BorderLayout.WEST);
        secondBar.add(right, BorderLayout.EAST);

        /* ================= TABLE ================= */
        model = new DefaultTableModel(
                new Object[] { "Select", "ID", "Name", "Type", "District", "Province", "Postal Code", "Tel No" }, 0) {
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
        cbSelectAll.addActionListener(e -> toggleSelectAll());
        model.addTableModelListener(this::onSelectionChanged);

        btnEdit.addActionListener(e -> openEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> loadTable());

        cbFilter.addActionListener(e -> applyAll());
        cbSort.addActionListener(e -> applyAll());

        loadTable();
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

        // Zebra rows
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
                    c.setForeground(Color.DARK_GRAY);
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
    private void loadTable() {
        cache = controller.fetchAll();
        if (cache == null)
            cache = new ArrayList<>();
        applyAll();
    }

    private void applyAll() {

        model.setRowCount(0);

        String typeFilter = cbFilter.getSelectedItem().toString();
        String sort = cbSort.getSelectedItem().toString();
        String query = searchPopup == null ? "" : searchPopup.getQuery();

        List<Branch> list = new ArrayList<>();

        for (Branch b : cache) {

            if (!"All Types".equals(typeFilter)
                    && !typeFilter.equalsIgnoreCase(b.getType()))
                continue;

            if (!query.isEmpty()) {
                boolean match = searchPopup.matches("Name", b.getName()) ||
                        searchPopup.matches("District", b.getDistrict()) ||
                        searchPopup.matches("Province", b.getProvince()) ||
                        searchPopup.matches("Tel", b.getTelNo());
                if (!match)
                    continue;
            }

            list.add(b);
        }

        Comparator<Branch> cmp = "Sort by District".equals(sort)
                ? Comparator.comparing(b -> safe(b.getDistrict()))
                : Comparator.comparing(b -> safe(b.getName()));

        list.sort(cmp);

        for (Branch b : list) {
            model.addRow(new Object[] {
                    false,
                    String.format("%03d", b.getBranchId()),
                    b.getName(),
                    b.getType(),
                    b.getDistrict(),
                    b.getProvince(),
                    b.getPostalCode(),
                    b.getTelNo()
            });
        }

        lblSelected.setText("0 Selected");
        cbSelectAll.setSelected(false);
    }

    /* ================= SELECTION ================= */
    private void toggleSelectAll() {
        boolean sel = cbSelectAll.isSelected();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(sel, i, 0);
        }
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
    private void openAddDialog() {
        if (!AdminGuard.requireAdmin(this))
            return;

        BranchDialog d = new BranchDialog(this, null);
        if (d.isSaved()) {
            boolean ok = controller.create(d.getBranchFromForm());
            if (ok) {
                SwingUtils.info(this, "Branch added successfully.");
                loadTable();
            } else {
                SwingUtils.error(this, "Failed to add branch.");
            }
        }
    }

    private void openEditDialog() {
        if (!AdminGuard.requireAdmin(this))
            return;

        int row = getSingleCheckedRow();
        if (row < 0) {
            SwingUtils.error(this, "Select exactly ONE branch.");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 1).toString());
        Branch existing = controller.findById(id);

        BranchDialog d = new BranchDialog(this, existing);
        if (d.isSaved()) {
            Branch updated = d.getBranchFromForm();
            updated.setBranchId(id);

            boolean ok = controller.update(updated);
            if (ok) {
                SwingUtils.info(this, "Branch updated successfully.");
                loadTable();
            } else {
                SwingUtils.error(this, "Failed to update branch.");
            }
        }
    }

    private void deleteSelected() {
        if (!AdminGuard.requireAdmin(this))
            return;

        int row = getSingleCheckedRow();
        if (row < 0) {
            SwingUtils.error(this, "Select exactly ONE branch.");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 1).toString());
        if (!SwingUtils.confirm(this, "Delete selected branch?"))
            return;

        boolean ok = controller.delete(id);
        if (ok) {
            SwingUtils.info(this, "Branch deleted.");
            loadTable();
        } else {
            SwingUtils.error(this, "Delete failed.");
        }
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
                    "Search Branches",
                    new String[] { "Name", "District", "Province", "Tel" });
        }
        searchPopup.show(anchor);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
