package com.lankacourier.UI.dialogs;

import com.lankacourier.Model.Branch;
import com.lankacourier.UI.base.BaseDialog;
import com.lankacourier.Util.ModernUI;
import com.lankacourier.Util.SwingUtils;

import javax.swing.*;
import java.awt.*;

public class BranchDialog extends BaseDialog {

    private final Branch existing;

    private JTextField tfName;
    private JTextField tfType;
    private JTextField tfDistrict;
    private JTextField tfProvince;
    private JTextField tfPostal;
    private JTextField tfTel;
    private JTextField tfLatitude;
    private JTextField tfLongitude;
    private JTextField tfBranchDistance;

    private boolean saved = false;

    public BranchDialog(Component parent, Branch existing) {
        super(parent, existing == null ? "Add Branch" : "Edit Branch");
        this.existing = existing;
        setSize(500, 600); // Slightly taller

        buildForm();

        if (existing != null) {
            fillData();
        }

        setVisible(true);
    }

    /* ================= FORM ================= */
    private void buildForm() {

        tfName = ModernUI.createTextField();
        tfType = ModernUI.createTextField();
        tfDistrict = ModernUI.createTextField();
        tfProvince = ModernUI.createTextField();
        tfPostal = ModernUI.createTextField();
        tfTel = ModernUI.createTextField();

        tfLatitude = ModernUI.createTextField();
        tfLatitude.setToolTipText("e.g. 6.9271");
        tfLongitude = ModernUI.createTextField();
        tfLongitude.setToolTipText("e.g. 79.8612");
        tfBranchDistance = ModernUI.createTextField();
        tfBranchDistance.setToolTipText("Distance from Head Office (km)");

        int y = 0;
        formPanel.add(new JLabel("Name *"), gbc(0, y));
        formPanel.add(tfName, gbc(1, y++));

        formPanel.add(new JLabel("Type"), gbc(0, y));
        formPanel.add(tfType, gbc(1, y++));

        formPanel.add(new JLabel("District"), gbc(0, y));
        formPanel.add(tfDistrict, gbc(1, y++));

        formPanel.add(new JLabel("Province"), gbc(0, y));
        formPanel.add(tfProvince, gbc(1, y++));

        formPanel.add(new JLabel("Postal Code"), gbc(0, y));
        formPanel.add(tfPostal, gbc(1, y++));

        formPanel.add(new JLabel("Tel No"), gbc(0, y));
        formPanel.add(tfTel, gbc(1, y++));

        // Location Section
        JLabel lblLoc = new JLabel("Location (for auto-distance)");
        lblLoc.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLoc.setForeground(new Color(100, 100, 100));

        formPanel.add(Box.createVerticalStrut(10), gbc(0, y));
        formPanel.add(lblLoc, gbc(1, y++));

        formPanel.add(new JLabel("Latitude"), gbc(0, y));
        formPanel.add(tfLatitude, gbc(1, y++));

        formPanel.add(new JLabel("Longitude"), gbc(0, y));
        formPanel.add(tfLongitude, gbc(1, y++));

        formPanel.add(new JLabel("Branch Distance (km)"), gbc(0, y));
        formPanel.add(tfBranchDistance, gbc(1, y++));
    }

    /* ================= PREFILL ================= */
    private void fillData() {
        tfName.setText(existing.getName());
        tfType.setText(existing.getType());
        tfDistrict.setText(existing.getDistrict());
        tfProvince.setText(existing.getProvince());
        tfPostal.setText(existing.getPostalCode());
        tfTel.setText(existing.getTelNo());

        if (existing.getLatitude() != null)
            tfLatitude.setText(existing.getLatitude().toString());

        if (existing.getLongitude() != null)
            tfLongitude.setText(existing.getLongitude().toString());

        if (existing.getBranchDistance() != null)
            tfBranchDistance.setText(existing.getBranchDistance().toString());
    }

    /* ================= SAVE ================= */
    @Override
    protected void onSave() {

        if (tfName.getText().isBlank()) {
            SwingUtils.error(this, "Branch name is required");
            return;
        }

        saved = true;
        dispose();
    }

    /* ================= GET DATA ================= */
    public boolean isSaved() {
        return saved;
    }

    public Branch getBranchFromForm() {
        Branch b = existing == null ? new Branch() : existing;

        b.setName(tfName.getText().trim());
        b.setType(tfType.getText().trim());
        b.setDistrict(tfDistrict.getText().trim());
        b.setProvince(tfProvince.getText().trim());
        b.setPostalCode(tfPostal.getText().trim());
        b.setTelNo(tfTel.getText().trim());

        try {
            String lat = tfLatitude.getText().trim();
            if (!lat.isEmpty())
                b.setLatitude(Double.parseDouble(lat));

            String lon = tfLongitude.getText().trim();
            if (!lon.isEmpty())
                b.setLongitude(Double.parseDouble(lon));
        } catch (NumberFormatException e) {
            // Ignore invalid input, leave as default/null
        }

        try {
            String dist = tfBranchDistance.getText().trim();
            if (!dist.isEmpty())
                b.setBranchDistance(Double.parseDouble(dist));
        } catch (NumberFormatException e) {
        }

        return b;
    }
}
