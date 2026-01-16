package com.lankacourier.UI;

import com.lankacourier.Controller.RatesController;
import com.lankacourier.Model.Rates;
import com.lankacourier.Util.SwingUtils;
import com.lankacourier.Util.FormatUtil;
import com.lankacourier.Auth.Session;
import com.lankacourier.Model.Employee;
import com.lankacourier.UI.components.RoundedButton;
import com.lankacourier.Util.ModernUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RatesPanel extends JPanel {

    private final RatesController ratesController = new RatesController();
    private final DefaultTableModel tableModel;
    private final JTable table;

    private List<Rates> ratesCache = new ArrayList<>();

    private JTextField tfSearch;
    private boolean weightAsc = true;
    private boolean priceAsc = true;

    public RatesPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
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

        JLabel title = new JLabel("Rates Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        // Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);
        tfSearch = new JTextField(15);
        tfSearch.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tfSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        RoundedButton btnSearch = new RoundedButton("Filter", Color.WHITE);
        btnSearch.setForeground(new Color(30, 136, 229));
        btnSearch.setPreferredSize(new Dimension(80, 30));

        searchPanel.add(new JLabel("<html><font color='white'>Min/Max Weight:</font></html>"));
        searchPanel.add(tfSearch);
        searchPanel.add(btnSearch);

        topBar.add(title, BorderLayout.WEST);
        topBar.add(searchPanel, BorderLayout.EAST);

        tfSearch.addActionListener(e -> applyFilter());
        btnSearch.addActionListener(e -> applyFilter());

        /* ================= MAIN CONTENT (GRID or BORDER) ================= */

        // 1. RATES TABLE
        String[] cols = { "ID", "Min Weight (kg)", "Max Weight (kg)", "Price (LKR)" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane spRates = new JScrollPane(table);
        spRates.setBorder(BorderFactory.createEmptyBorder());

        JPanel pnlRatesCard = new JPanel(new BorderLayout());
        pnlRatesCard.setBackground(Color.WHITE);
        pnlRatesCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                null));
        pnlRatesCard.add(topBar, BorderLayout.NORTH); // Header inside card? Or separate?
        // Let's put Header separate at top, card contains table + actions.

        // Actually, let's follow ParcelPanel pattern: White Card containing Header +
        // Content.
        // It looks cleaner.

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                null));

        card.add(topBar, BorderLayout.NORTH);

        JPanel contentArea = new JPanel(new BorderLayout(15, 0));
        contentArea.setOpaque(false);
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contentArea.add(spRates, BorderLayout.CENTER);

        // 2. DISTANCE TABLE (Static Info)
        JPanel pnlDist = buildDistancePanel();
        contentArea.add(pnlDist, BorderLayout.EAST);

        card.add(contentArea, BorderLayout.CENTER);

        // 3. ACTIONS BAR
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actions.setOpaque(false);
        actions.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        RoundedButton btnAdd = new RoundedButton("Add Rate", new Color(46, 204, 113));
        RoundedButton btnEdit = new RoundedButton("Edit", new Color(52, 152, 219));
        RoundedButton btnDelete = new RoundedButton("Delete", new Color(231, 76, 60));
        RoundedButton btnRef = new RoundedButton("Refresh", new Color(149, 165, 166));

        RoundedButton btnSortW = new RoundedButton("Sort Weight", new Color(245, 245, 245));
        btnSortW.setForeground(Color.DARK_GRAY);
        RoundedButton btnSortP = new RoundedButton("Sort Price", new Color(245, 245, 245));
        btnSortP.setForeground(Color.DARK_GRAY);

        boolean admin = isAdmin();
        btnAdd.setEnabled(admin);
        btnEdit.setEnabled(admin);
        btnDelete.setEnabled(admin);

        actions.add(btnAdd);
        actions.add(btnEdit);
        actions.add(btnDelete);
        actions.add(btnRef);
        actions.add(Box.createHorizontalStrut(20));
        actions.add(btnSortW);
        actions.add(btnSortP);

        card.add(actions, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);

        /* ================= EVENTS ================= */
        btnRef.addActionListener(e -> loadTableSafe());

        btnSortW.addActionListener(e -> {
            ratesCache.sort(Comparator.comparing(Rates::getMinWeightKg));
            if (!weightAsc)
                ratesCache.sort(Comparator.comparing(Rates::getMinWeightKg).reversed());
            weightAsc = !weightAsc;
            refreshTable(ratesCache);
        });

        btnSortP.addActionListener(e -> {
            ratesCache.sort(Comparator.comparing(Rates::getPriceLkr));
            if (!priceAsc)
                ratesCache.sort(Comparator.comparing(Rates::getPriceLkr).reversed());
            priceAsc = !priceAsc;
            refreshTable(ratesCache);
        });

        btnAdd.addActionListener(e -> openRateDialog(null));

        btnEdit.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel < 0) {
                SwingUtils.error(this, "Select a rate.");
                return;
            }
            int id = (int) tableModel.getValueAt(sel, 0);
            Rates r = ratesController.findById(id);
            if (r != null)
                openRateDialog(r);
        });

        btnDelete.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel < 0) {
                SwingUtils.error(this, "Select a rate.");
                return;
            }
            int id = (int) tableModel.getValueAt(sel, 0);
            if (SwingUtils.confirm(this, "Delete rate ID = " + id + "?")) {
                if (ratesController.delete(id)) {
                    SwingUtils.info(this, "Deleted.");
                    loadTableSafe();
                }
            }
        });

        loadTableSafe();
    }

    private boolean isAdmin() {
        try {
            Employee u = Session.getCurrentUser();
            return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
        } catch (Exception e) {
            return false;
        }
    }

    private JPanel buildDistancePanel() {
        JPanel distPanel = new JPanel(new BorderLayout());
        distPanel.setBackground(new Color(250, 252, 255));
        distPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 230)));
        distPanel.setPreferredSize(new Dimension(250, 0));

        JLabel title = new JLabel("Distance Rates", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(30, 136, 229));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        distPanel.add(title, BorderLayout.NORTH);

        String[] cols = { "Distance", "Price (Approx)" };
        DefaultTableModel distModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        // Add examples
        distModel.addRow(new Object[] { "1 km", "50 LKR" });
        distModel.addRow(new Object[] { "5 km", "250 LKR" });
        distModel.addRow(new Object[] { "10 km", "500 LKR" });
        distModel.addRow(new Object[] { "50 km", "2,500 LKR" });
        distModel.addRow(new Object[] { "100 km", "5,000 LKR" });

        JTable distTable = new JTable(distModel);
        styleTable(distTable);
        distTable.setShowHorizontalLines(true);
        distTable.setGridColor(new Color(230, 230, 230));

        JScrollPane sp = new JScrollPane(distTable);
        sp.setBorder(BorderFactory.createEmptyBorder());
        distPanel.add(sp, BorderLayout.CENTER);

        JLabel info = new JLabel("<html><center>Based on 50 LKR/km<br>Standard Rate</center></html>",
                SwingConstants.CENTER);
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(Color.GRAY);
        info.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        distPanel.add(info, BorderLayout.SOUTH);

        return distPanel;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                Component cmp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                if (!s)
                    cmp.setBackground(r % 2 == 0 ? Color.WHITE : new Color(245, 247, 250));
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return cmp;
            }
        });

        JTableHeader h = table.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        h.setBackground(new Color(236, 240, 241));
        h.setForeground(new Color(44, 62, 80));
    }

    public void loadTableSafe() {
        try {
            ratesCache = ratesController.fetchAll();
            refreshTable(ratesCache);
        } catch (Exception ex) {
            SwingUtils.error(this, "Failed to load rates.");
        }
    }

    private void refreshTable(List<Rates> list) {
        tableModel.setRowCount(0);
        if (list == null)
            return;

        for (Rates r : list) {
            tableModel.addRow(new Object[] {
                    r.getRateId(),
                    r.getMinWeightKg(),
                    r.getMaxWeightKg(),
                    FormatUtil.formatPrice(r.getPriceLkr())
            });
        }
    }

    private void applyFilter() {
        String q = tfSearch.getText().trim();
        if (q.isBlank()) {
            refreshTable(ratesCache);
            return;
        }

        List<Rates> filtered = new ArrayList<>();
        try {
            BigDecimal v = new BigDecimal(q);
            for (Rates r : ratesCache) {
                // Filter logic: if Value is within range, OR Price matches
                if (r.getMinWeightKg().compareTo(v) <= 0 && r.getMaxWeightKg().compareTo(v) >= 0) {
                    filtered.add(r);
                } else if (r.getPriceLkr().compareTo(v) == 0) {
                    filtered.add(r);
                }
            }
            refreshTable(filtered);
        } catch (NumberFormatException e) {
            SwingUtils.error(this, "Enter a valid number (Price or Weight).");
        }
    }

    private void openRateDialog(Rates existing) {
        JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this), existing == null ? "Add Rate" : "Edit Rate",
                Dialog.ModalityType.APPLICATION_MODAL);
        d.setSize(400, 350);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField tfMin = ModernUI.createTextField();
        JTextField tfMax = ModernUI.createTextField();
        JTextField tfPrice = ModernUI.createTextField();

        if (existing != null) {
            tfMin.setText(existing.getMinWeightKg().toString());
            tfMax.setText(existing.getMaxWeightKg().toString());
            tfPrice.setText(existing.getPriceLkr().toString());
        }

        addFormGroup(p, "Min Weight (kg)", tfMin, 0);
        addFormGroup(p, "Max Weight (kg)", tfMax, 1);
        addFormGroup(p, "Price (LKR)", tfPrice, 2);

        JButton btnSave = new JButton("Save Rate");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSave.addActionListener(e -> {
            try {
                Rates r = existing == null ? new Rates() : existing;
                r.setMinWeightKg(new BigDecimal(tfMin.getText().trim()));
                r.setMaxWeightKg(new BigDecimal(tfMax.getText().trim()));
                r.setPriceLkr(new BigDecimal(tfPrice.getText().trim()));

                boolean ok = existing == null ? ratesController.create(r) : ratesController.update(r);
                if (ok) {
                    SwingUtils.info(d, "Saved!");
                    d.dispose();
                    loadTableSafe();
                } else {
                    SwingUtils.error(d, "Save failed.");
                }
            } catch (Exception ex) {
                SwingUtils.error(d, "Invalid inputs.");
            }
        });

        d.add(p, BorderLayout.CENTER);
        d.add(btnSave, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void addFormGroup(JPanel p, String label, JComponent c, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = y;
        p.add(ModernUI.createFormGroup(label, c), gbc);
    }
}
