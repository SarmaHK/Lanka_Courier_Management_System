package com.lankacourier.UI.base;

import com.lankacourier.Util.ModernUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SearchPopup {

    private final JPopupMenu popup = new JPopupMenu();
    private final JTextField tf;
    private final Set<String> enabledFields = new HashSet<>();

    public SearchPopup(String title, String[] fields) {
        popup.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        popup.setBackground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 220)); // Fixed reasonable size

        // Title
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(new Color(30, 30, 30));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Input
        tf = ModernUI.createTextField();
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Checkboxes Container
        JPanel checks = new JPanel(new GridLayout(0, 2, 8, 8));
        checks.setOpaque(false);
        checks.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String f : fields) {
            JCheckBox cb = new JCheckBox(f, true);
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            cb.setBackground(Color.WHITE);
            cb.setFocusPainted(false);

            enabledFields.add(f);

            cb.addActionListener(e -> {
                if (cb.isSelected())
                    enabledFields.add(f);
                else
                    enabledFields.remove(f);
            });
            checks.add(cb);
        }

        // Layout
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(10));
        panel.add(tf);
        panel.add(Box.createVerticalStrut(15));

        JLabel lblFilter = new JLabel("Search in:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFilter.setForeground(new Color(120, 120, 120));
        lblFilter.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblFilter);
        panel.add(Box.createVerticalStrut(5));
        panel.add(checks);

        popup.add(panel);
    }

    public void show(Component anchor) {
        popup.show(anchor, 0, anchor.getHeight() + 6);
        tf.requestFocusInWindow();
    }

    public String getQuery() {
        return tf.getText().toLowerCase().trim();
    }

    public boolean matches(String field, String value) {
        return enabledFields.contains(field) &&
                value != null &&
                value.toLowerCase().contains(getQuery());
    }
}
