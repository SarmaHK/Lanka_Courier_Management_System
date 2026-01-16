package com.lankacourier.UI;

import com.lankacourier.Controller.*;
import com.lankacourier.Model.*;
import com.lankacourier.UI.base.SearchPopup;
import com.lankacourier.UI.components.CustomerEntryPanel;
import com.lankacourier.UI.components.ModernComboBox;
import com.lankacourier.UI.components.RoundedButton;
import com.lankacourier.UI.components.SearchChip;
import com.lankacourier.Util.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;

public class ParcelPanel extends JPanel {

    private final ParcelController parcelController = new ParcelController();
    private final CustomerController customerController = new CustomerController();
    private final BranchController branchController = new BranchController();
    private final RatesController ratesController = new RatesController();

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    private SearchPopup searchPopup;
    private ModernComboBox<String> cbStatus, cbCod, cbSort;

    public ParcelPanel() {
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

        SearchChip searchChip = new SearchChip();
        searchChip.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showSearch(searchChip);
            }
        });

        JLabel title = new JLabel("Parcel Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        RoundedButton btnAdd = new RoundedButton("+ Register Parcel", Color.WHITE);
        btnAdd.setForeground(new Color(30, 136, 229));
        btnAdd.addActionListener(e -> openForm(null));

        topBar.add(searchChip, BorderLayout.WEST);
        topBar.add(title, BorderLayout.CENTER);
        topBar.add(btnAdd, BorderLayout.EAST);

        /* ================= SECOND BAR (ACTIONS) ================= */
        JPanel secondBar = new JPanel(new BorderLayout());
        secondBar.setOpaque(false);
        secondBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        RoundedButton btnEdit = new RoundedButton("Edit", new Color(30, 136, 229));
        RoundedButton btnDelete = new RoundedButton("Delete", new Color(229, 57, 53));
        RoundedButton btnRefresh = new RoundedButton("Refresh", new Color(149, 165, 166));

        left.add(btnEdit);
        left.add(btnDelete);
        left.add(btnRefresh);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        cbStatus = new ModernComboBox<>(new String[] { "All Status", "Registered", "In Transit", "Delivered" });
        cbCod = new ModernComboBox<>(new String[] { "All", "COD Only", "Non-COD" });
        cbSort = new ModernComboBox<>(new String[] { "No Sorting", "Price ↑", "Price ↓", "Weight ↑", "Weight ↓" });

        right.add(cbStatus);
        right.add(cbCod);
        right.add(cbSort);

        secondBar.add(left, BorderLayout.WEST);
        secondBar.add(right, BorderLayout.EAST);

        /* ================= TABLE ================= */
        model = new DefaultTableModel(
                new String[] {
                        "ID", "Tracking", "Sender", "Receiver",
                        "Origin", "Destination", "Weight", "Distance", "Price", "Status", "COD"
                }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        styleTable(table);
        setInitialColumnWidths(); // Reusing existing method

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        enableResponsiveResize(table, scroll); // Reusing existing method

        /* ================= CARD LAYOUT ================= */
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                null));

        // Assemble Header + Second Bar
        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setBackground(Color.WHITE);
        headerContainer.add(topBar, BorderLayout.NORTH);
        headerContainer.add(secondBar, BorderLayout.CENTER);

        card.add(headerContainer, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);

        /* ================= EVENTS ================= */
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> loadTable());

        cbStatus.addActionListener(e -> applyFilters());
        cbCod.addActionListener(e -> applyFilters());
        cbSort.addActionListener(e -> applySorting());

        loadTable();
    }

    // logic methods continue...

    private void showSearch(Component anchor) {
        if (searchPopup == null) {
            searchPopup = new SearchPopup("Search Parcels", new String[] {
                    "Tracking ID", "Sender", "Receiver", "Origin", "Destination"
            });
        }
        searchPopup.show(anchor);
    }

    // Overriding/Adding logic to handle SearchPopup filter queries
    // NOTE: The original FilterListener is no longer needed as we use SearchBox
    // We need to update ApplyFilters to check SearchPopup query.

    private void applyFilters() {
        String status = cbStatus.getSelectedItem().toString();
        String cod = cbCod.getSelectedItem().toString();
        String query = searchPopup == null ? "" : searchPopup.getQuery();

        // Need to refactor 'loadTable' or 'applyFilters' to use the new search
        // architecture
        // But the existing code uses TableRowSorter or loadTable re-fetch?
        // Existing code had 'FilterListener' on a JTextField 'tfSearch'.

        // Since I replaced the UI, I should adapt the filter logic here.
        // Let's implement a robust RowFilter.

        RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                // 1. Status
                String sta = entry.getStringValue(9);
                if (!"All Status".equals(status) && !sta.equalsIgnoreCase(status))
                    return false;

                // 2. COD
                String isCod = entry.getStringValue(10); // YES/NO
                if ("COD Only".equals(cod) && !"YES".equals(isCod))
                    return false;
                if ("Non-COD".equals(cod) && !"NO".equals(isCod))
                    return false;

                // 3. Search
                if (query.isEmpty())
                    return true;

                // Check all enabled fields
                // Map columns to fields roughly
                // 1=Tracking, 2=Sender, 3=Receiver, 4=Origin, 5=Dest

                boolean match = false;
                if (searchPopup.matches("Tracking ID", entry.getStringValue(1)))
                    match = true;
                if (searchPopup.matches("Sender", entry.getStringValue(2)))
                    match = true;
                if (searchPopup.matches("Receiver", entry.getStringValue(3)))
                    match = true;
                if (searchPopup.matches("Origin", entry.getStringValue(4)))
                    match = true;
                if (searchPopup.matches("Destination", entry.getStringValue(5)))
                    match = true;

                // If search popup has no fields checked, default to global search
                // But SearchPopup usually defaults to some.

                return match;
            }
        };

        // Set sorter
        if (sorter == null) {
            sorter = new TableRowSorter<>(model);
            table.setRowSorter(sorter);
        }
        sorter.setRowFilter(rf);
    }

    private void applySorting() {
        // reuse existing logic if compatible, otherwise reimplement
        String sort = cbSort.getSelectedItem().toString();
        // Existing logic used comparators on 'model' rows?
        // Let's check original implementation...
        // Original implementation had specific Comparator logic.
        // I will rely on standard TableRowSorter for now or re-implement custom sort if
        // needed.
        // Actually the original code handled sorting manually in 'loadTable' or
        // 'applyAll'?
        // The original code had `cbSort.addActionListener(e -> applySorting())` (line
        // 72),
        // but `applySorting()` method was NOT visible in the snippet (only applyFilters
        // calls logic?).

        // Wait, snippet showed: tfSearch -> FilterListener, cbStatus/cbCod ->
        // applyFilters.
        // cbSort -> applySorting.
        // AND `buildTableSection` sets `sorter`.

        // I will reimplement applySorting using the sorter for simplicity and
        // robustness.

        List<RowSorter.SortKey> keys = new ArrayList<>();
        switch (sort) {
            case "Price ↑":
                keys.add(new RowSorter.SortKey(8, SortOrder.ASCENDING));
                break;
            case "Price ↓":
                keys.add(new RowSorter.SortKey(8, SortOrder.DESCENDING));
                break;
            case "Weight ↑":
                keys.add(new RowSorter.SortKey(6, SortOrder.ASCENDING));
                break;
            case "Weight ↓":
                keys.add(new RowSorter.SortKey(6, SortOrder.DESCENDING));
                break;
            default:
                break;
        }
        sorter.setSortKeys(keys);
    }

    // Helper class for DocumentListener is removed (using SearchPopup)

    // LOAD DATA
    private void loadTable() {

        model.setRowCount(0);
        List<Parcel> list = parcelController.fetchAll();
        if (list == null)
            return;

        for (Parcel p : list) {

            Customer s = customerController.findById(p.getSenderId());
            Customer r = customerController.findById(p.getReceiverId());
            Branch o = branchController.findById(p.getOriginBranchId());
            Branch d = p.getDestinationBranchId() == null
                    ? null
                    : branchController.findById(p.getDestinationBranchId());

            model.addRow(new Object[] {
                    p.getParcelId(),
                    p.getTrackingId(),
                    s == null ? "" : s.getContactPersonName(),
                    r == null ? "" : r.getContactPersonName(),
                    o == null ? "" : o.getName(),
                    d == null ? "" : d.getName(),
                    p.getWeightKg(),
                    p.getDistanceKm() == null ? "0" : p.getDistanceKm(),
                    p.getPriceLkr(),
                    p.getStatus(),
                    p.isCod() ? "YES" : "NO"
            });
        }
    }

    // REGISTER / EDIT DIALOG
    private void openForm(Parcel existing) {

        JDialog d = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                existing == null ? "Register Parcel" : "Edit Parcel",
                Dialog.ModalityType.APPLICATION_MODAL);

        d.setSize(1100, 800); // Slightly larger for better spacing
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        d.setBackground(Color.WHITE);

        // MAIN CONTENT SCROLLABLE
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainContent.setBackground(new Color(245, 247, 250));

        // --- COMPONENTS ---
        JTextField tfTracking = ModernUI.createTextField();
        tfTracking.setToolTipText("Enter simplified 6-digit Tracking ID");
        // STRICT NUMBER ONLY FOR TRACKING ID (12 DIGITS)
        ((javax.swing.text.AbstractDocument) tfTracking.getDocument())
                .setDocumentFilter(new NumericDocumentFilter(false, 12));

        JTextField tfWeight = ModernUI.createTextField();
        tfWeight.setToolTipText("Enter weight in kg (e.g. 1.5)");
        // DECIMAL SUPPORT FOR WEIGHT
        ((javax.swing.text.AbstractDocument) tfWeight.getDocument()).setDocumentFilter(new NumericDocumentFilter(true));

        JTextField tfDistance = ModernUI.createTextField();
        tfDistance.setEditable(false);
        tfDistance.setToolTipText("Auto-calculated based on origin and destination");

        JTextField tfPrice = ModernUI.createTextField();
        tfPrice.setEditable(false);
        tfPrice.setBackground(new Color(240, 240, 240));
        tfPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tfPrice.setForeground(new Color(39, 174, 96));

        CustomerEntryPanel pnlSender = new CustomerEntryPanel("Sender Details");
        CustomerEntryPanel pnlReceiver = new CustomerEntryPanel("Receiver Details");

        // 1. Initialize Combos & Populate FIRST so logic can use them
        JComboBox<Branch> cbOrigin = new JComboBox<>();
        JComboBox<Branch> cbDestination = new JComboBox<>();
        styleCombo(cbOrigin);
        styleCombo(cbDestination);

        branchController.fetchAll().forEach(b -> {
            cbOrigin.addItem(b);
            cbDestination.addItem(b);
        });
        cbOrigin.setRenderer(branchRenderer());
        cbDestination.setRenderer(branchRenderer());

        // 2. AUTO-GENERATE 12-DIGIT TRACKING ID
        // Format: [Sender(3)][Receiver(3)][Branch(3)][Sequence(3)]

        int nextCustId = customerController.getNextId();
        int nextSeq = parcelController.getNextParcelId(); // Sequence part

        Runnable updateTracking = () -> {
            try {
                // 1. SENDER ID
                int sId = nextCustId;
                Customer s = pnlSender.getCustomerData();
                if (s.getCustomerId() > 0) {
                    sId = s.getCustomerId();
                } else if (!s.getMobileNo().isEmpty()) {
                    Customer ex = customerController.findByMobile(s.getMobileNo());
                    if (ex != null)
                        sId = ex.getCustomerId();
                }

                // 2. RECEIVER ID
                int rId = nextCustId; // Default new
                Customer r = pnlReceiver.getCustomerData();
                if (r.getCustomerId() > 0) {
                    rId = r.getCustomerId();
                } else if (!r.getMobileNo().isEmpty()) {
                    Customer ex = customerController.findByMobile(r.getMobileNo());
                    if (ex != null)
                        rId = ex.getCustomerId();
                }

                // Edge Case: If Sender and Receiver are both "New" (nextCustId),
                // and it's the SAME ID, we might want to differentiate?
                // But strictly following logic: tracking ID is composite.
                // If both are new, sId == rId.
                // However, "Receiver" might be the NEXT customer after Sender?
                // If both are 0, sId = next, rId = next.
                // Let's assume rId = next + 1 if sequence implies concurrent creation?
                // For now, keep simple: use nextCustId for both placeholders.

                // 3. BRANCH ID
                int bId = 0;
                if (cbOrigin.getSelectedItem() instanceof Branch b) {
                    bId = b.getBranchId();
                }

                // 4. SEQUENCE
                int seq = nextSeq;

                // FORMAT: 12 Digits
                String newId = String.format("%03d%03d%03d%03d", sId, rId, bId, seq);

                tfTracking.setText(newId);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        // Listeners for ALL components affecting ID
        java.awt.event.FocusAdapter customerListener = new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                // Try load customers if mobile entered
                Customer s = pnlSender.getCustomerData();
                if (s.getCustomerId() == 0 && !s.getMobileNo().isEmpty()) {
                    Customer ex = customerController.findByMobile(s.getMobileNo());
                    if (ex != null)
                        pnlSender.loadCustomer(ex);
                }
                Customer r = pnlReceiver.getCustomerData();
                if (r.getCustomerId() == 0 && !r.getMobileNo().isEmpty()) {
                    Customer ex = customerController.findByMobile(r.getMobileNo());
                    if (ex != null)
                        pnlReceiver.loadCustomer(ex);
                }
                updateTracking.run();
            }
        };

        // Listeners on both mobile fields
        pnlSender.getTfMobile().addFocusListener(customerListener);
        pnlReceiver.getTfMobile().addFocusListener(customerListener);

        // INSTANT UPDATES via Custom Listener
        pnlSender.addCustomerChangeListener(updateTracking);
        pnlReceiver.addCustomerChangeListener(updateTracking);

        cbOrigin.addActionListener(e -> updateTracking.run());

        // Initial
        if (existing == null)
            updateTracking.run();

        JTextField tfStatus = ModernUI.createTextField();
        tfStatus.setText("Registered");
        tfStatus.setEditable(false); // Status usually managed by system flow, initially Registered

        JCheckBox chCod = new JCheckBox("Cash on Delivery (COD)");
        chCod.setFont(new Font("Segoe UI", Font.BOLD, 13));
        chCod.setForeground(new Color(50, 50, 50));
        chCod.setOpaque(false);

        // --- SECTION 1: PARCEL INFO ---
        JPanel pnlInfo = ModernUI.createCard();
        pnlInfo.setLayout(new GridBagLayout());

        // Header with Icon-like visual
        pnlInfo.add(ModernUI.createHeader("Details"),
                new GridBagConstraints(0, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 15, 0), 0, 0));

        addFormGroup(pnlInfo, "Tracking ID (Numbers Only)", tfTracking, 0, 1);
        addFormGroup(pnlInfo, "Weight (kg)", tfWeight, 1, 1);
        addFormGroup(pnlInfo, "Status", tfStatus, 2, 1);

        // --- SECTION 2: ROUTE & PRICING ---
        JPanel pnlRoute = ModernUI.createCard();
        pnlRoute.setLayout(new GridBagLayout());

        pnlRoute.add(ModernUI.createHeader("Route & Price"),
                new GridBagConstraints(0, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 15, 0), 0, 0));

        addFormGroup(pnlRoute, "Origin Branch", cbOrigin, 0, 1);
        addFormGroup(pnlRoute, "Destination Branch", cbDestination, 1, 1);
        addFormGroup(pnlRoute, "Distance", tfDistance, 2, 1);
        addFormGroup(pnlRoute, "Total Price (LKR)", tfPrice, 3, 1);

        GridBagConstraints gbcCod = new GridBagConstraints();
        gbcCod.gridx = 3;
        gbcCod.gridy = 2;
        gbcCod.anchor = GridBagConstraints.EAST;
        pnlRoute.add(chCod, gbcCod);

        // --- SECTION 3: PARTIES ---
        JPanel pnlParties = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlParties.setOpaque(false);
        pnlParties.add(pnlSender);
        pnlParties.add(pnlReceiver);

        // ADD SECTIONS
        mainContent.add(pnlInfo);
        mainContent.add(Box.createVerticalStrut(15));
        mainContent.add(pnlRoute);
        mainContent.add(Box.createVerticalStrut(15));
        mainContent.add(pnlParties);

        // AUTO PRICE CALCULATION LOGIC
        Runnable calculate = () -> {
            try {
                String wStr = tfWeight.getText().trim();
                String dStr = tfDistance.getText().trim().toLowerCase().replace("km", "").replace(" ", "");

                if (wStr.isEmpty()) {
                    tfPrice.setText("0.00");
                    return;
                }

                BigDecimal w = new BigDecimal(wStr);
                BigDecimal dist = dStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(dStr);

                BigDecimal finalPrice = ratesController.calculatePrice(w, dist);
                tfPrice.setText(finalPrice.setScale(2, RoundingMode.HALF_UP).toString());
            } catch (Exception ignored) {
                // If invalid number format momentarily, just ignore
            }
        };

        tfWeight.getDocument().addDocumentListener(new SimpleDocListener(calculate));
        tfDistance.getDocument().addDocumentListener(new SimpleDocListener(calculate));

        // AUTO DISTANCE LOGIC
        java.awt.event.ActionListener branchListener = e -> {
            try {
                Branch origin = (Branch) cbOrigin.getSelectedItem();
                Branch dest = (Branch) cbDestination.getSelectedItem();

                if (origin != null && dest != null) {
                    double distVal = getMatrixDistance(origin.getDistrict(), dest.getDistrict());
                    tfDistance.setText(String.format("%.2f km", distVal));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        cbOrigin.addActionListener(branchListener);
        cbDestination.addActionListener(branchListener);

        // Trigger initial calculation if editing
        if (existing != null) {
            tfTracking.setText(existing.getTrackingId());
            tfWeight.setText(existing.getWeightKg().toString());
            if (existing.getDistanceKm() != null)
                tfDistance.setText(existing.getDistanceKm().toString());
            tfPrice.setText(existing.getPriceLkr().toString());
            tfStatus.setText(existing.getStatus());
            chCod.setSelected(existing.isCod());
            selectBranch(cbOrigin, existing.getOriginBranchId());
            if (existing.getDestinationBranchId() != null)
                selectBranch(cbDestination, existing.getDestinationBranchId());

            pnlSender.loadCustomer(customerController.findById(existing.getSenderId()));
            pnlReceiver.loadCustomer(customerController.findById(existing.getReceiverId()));
        } else {
            // Trigger distance calc for default selection
            branchListener.actionPerformed(null);
        }

        // SAVE ACTION
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        JButton save = ModernUI.createPrimaryButton("Confirm & Register");
        save.setFont(new Font("Segoe UI", Font.BOLD, 14));
        save.setPreferredSize(new Dimension(180, 40));

        JButton cancel = ModernUI.createSecondaryButton("Cancel");
        cancel.setPreferredSize(new Dimension(100, 40));

        actions.add(cancel);
        actions.add(save);

        save.addActionListener(evt -> {
            try {
                // VALIDATE INPUTS
                if (tfTracking.getText().trim().isEmpty()) {
                    SwingUtils.error(d, "Tracking ID is required!");
                    return;
                }
                if (tfWeight.getText().trim().isEmpty()) {
                    SwingUtils.error(d, "Parcel weight is required!");
                    return;
                }

                Customer sender = pnlSender.getCustomerData();
                Customer receiver = pnlReceiver.getCustomerData();

                // Validate Customer Data Basic
                if (sender.getMobileNo().isEmpty() || sender.getContactPersonName().isEmpty()) {
                    SwingUtils.error(d, "Sender Name and Mobile are required!");
                    return;
                }
                if (receiver.getMobileNo().isEmpty() || receiver.getContactPersonName().isEmpty()) {
                    SwingUtils.error(d, "Receiver Name and Mobile are required!");
                    return;
                }

                // SAVE CUSTOMERS
                Customer savedSender = customerController.findOrCreate(sender);
                Customer savedReceiver = customerController.findOrCreate(receiver);

                if (savedSender == null || savedReceiver == null) {
                    SwingUtils.error(d, "Error saving customers. Check details.");
                    return;
                }

                // SAVE PARCEL
                Parcel p = existing == null ? new Parcel() : existing;
                p.setTrackingId(tfTracking.getText().trim());
                p.setSenderId(savedSender.getCustomerId());
                p.setReceiverId(savedReceiver.getCustomerId());
                p.setOriginBranchId(((Branch) cbOrigin.getSelectedItem()).getBranchId());
                p.setDestinationBranchId(((Branch) cbDestination.getSelectedItem()).getBranchId());
                p.setWeightKg(new BigDecimal(tfWeight.getText().trim()));

                String dst = tfDistance.getText().toLowerCase().replace("km", "").trim();
                p.setDistanceKm(dst.isEmpty() ? BigDecimal.ZERO : new BigDecimal(dst));

                p.setPriceLkr(new BigDecimal(tfPrice.getText().trim()));
                p.setStatus(tfStatus.getText());
                p.setCod(chCod.isSelected());

                boolean ok = existing == null ? parcelController.create(p) : parcelController.update(p);

                if (ok) {
                    SwingUtils.info(d, "Parcel Processed Successfully!");
                    d.dispose();
                    loadTable();
                } else {
                    SwingUtils.error(d, "Failed to save parcel in database.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtils.error(d, "System Error: " + ex.getMessage());
            }
        });

        cancel.addActionListener(e -> d.dispose());

        d.add(new JScrollPane(mainContent), BorderLayout.CENTER);
        d.add(actions, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        cb.setFocusable(false);
    }

    private void addFormGroup(JPanel p, String label, JComponent c, int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = x;
        gbc.gridy = y;
        p.add(ModernUI.createFormGroup(label, c), gbc);
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            SwingUtils.error(this, "Select a parcel");
            return;
        }
        int id = (int) model.getValueAt(table.convertRowIndexToModel(row), 0);
        openForm(parcelController.findById(id));
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            SwingUtils.error(this, "Select a parcel");
            return;
        }
        int id = (int) model.getValueAt(table.convertRowIndexToModel(row), 0);
        if (!SwingUtils.confirm(this, "Delete selected parcel?"))
            return;

        parcelController.delete(id);
        loadTable();
    }

    // FILTER & SORT
    // Old Filter/Sort methods replaced by new implementation above.

    // TABLE STYLE
    private void styleTable(JTable table) {
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 247, 250));
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

    private void setInitialColumnWidths() {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] w = { 60, 150, 150, 150, 120, 120, 80, 80, 90, 120, 60 };
        for (int i = 0; i < w.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
    }

    private void enableResponsiveResize(JTable table, JScrollPane scroll) {
        scroll.getViewport().addChangeListener(e -> {
            if (scroll.getViewport().getWidth() > table.getPreferredSize().width)
                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            else
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        });
    }

    private ListCellRenderer<Object> branchRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Branch b)
                    setText(b.getName());
                return this;
            }
        };
    }

    private void selectBranch(JComboBox<Branch> cb, int id) {
        for (int i = 0; i < cb.getItemCount(); i++)
            if (cb.getItemAt(i).getBranchId() == id)
                cb.setSelectedIndex(i);
    }

    private static class SimpleDocListener implements DocumentListener {
        private final Runnable r;

        SimpleDocListener(Runnable r) {
            this.r = r;
        }

        public void insertUpdate(DocumentEvent e) {
            r.run();
        }

        public void removeUpdate(DocumentEvent e) {
            r.run();
        }

        public void changedUpdate(DocumentEvent e) {
            r.run();
        }
    }

    private double getMatrixDistance(String d1, String d2) {
        if (d1 == null || d2 == null)
            return 0.0;
        d1 = d1.trim().toLowerCase();
        d2 = d2.trim().toLowerCase();

        if (d1.equals(d2))
            return 0.0;

        // Matrix
        // Jaffna cases
        if (is(d1, "jaffna") && is(d2, "colombo"))
            return 400.0;
        if (is(d1, "colombo") && is(d2, "jaffna"))
            return 400.0;

        if (is(d1, "jaffna") && is(d2, "kandy"))
            return 375.0;
        if (is(d1, "kandy") && is(d2, "jaffna"))
            return 375.0;

        if (is(d1, "jaffna") && is(d2, "galle"))
            return 585.0;
        if (is(d1, "galle") && is(d2, "jaffna"))
            return 585.0;

        if (is(d1, "jaffna") && is(d2, "matara"))
            return 610.0;
        if (is(d1, "matara") && is(d2, "jaffna"))
            return 610.0;

        // Colombo cases
        if (is(d1, "colombo") && is(d2, "kandy"))
            return 115.0;
        if (is(d1, "kandy") && is(d2, "colombo"))
            return 115.0;

        if (is(d1, "colombo") && is(d2, "galle"))
            return 120.0;
        if (is(d1, "galle") && is(d2, "colombo"))
            return 120.0;

        if (is(d1, "colombo") && is(d2, "matara"))
            return 160.0;
        if (is(d1, "matara") && is(d2, "colombo"))
            return 160.0;

        // Kandy cases
        if (is(d1, "kandy") && is(d2, "galle"))
            return 235.0;
        if (is(d1, "galle") && is(d2, "kandy"))
            return 235.0;

        if (is(d1, "kandy") && is(d2, "matara"))
            return 275.0;
        if (is(d1, "matara") && is(d2, "kandy"))
            return 275.0;

        // Galle cases
        if (is(d1, "galle") && is(d2, "matara"))
            return 45.0;
        if (is(d1, "matara") && is(d2, "galle"))
            return 45.0;

        return 0.0; // Default or unknown
    }

    private boolean is(String s1, String target) {
        return s1 != null && s1.contains(target);
    }
}
