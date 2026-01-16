package com.lankacourier.UI.base;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class BaseSearchPanel extends JPopupMenu {

    private final JTextField tfSearch = new JTextField(22);
    private final Set<String> enabledFields = new HashSet<>();

    public BaseSearchPanel(String title, String[] fields, Runnable onChange) {

        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(false);

        JPanel wrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(220, 220, 220));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        tfSearch.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JPanel checks = new JPanel(new GridLayout(0, 2, 8, 6));
        checks.setOpaque(false);

        for (String f : fields) {
            JCheckBox cb = new JCheckBox(f, true);
            enabledFields.add(f);
            cb.addActionListener(e -> {
                if (cb.isSelected()) enabledFields.add(f);
                else enabledFields.remove(f);
                onChange.run();
            });
            checks.add(cb);
        }

        tfSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { onChange.run(); }
            public void removeUpdate(DocumentEvent e) { onChange.run(); }
            public void changedUpdate(DocumentEvent e) { onChange.run(); }
        });

        wrapper.add(lblTitle);
        wrapper.add(Box.createVerticalStrut(8));
        wrapper.add(tfSearch);
        wrapper.add(Box.createVerticalStrut(10));
        wrapper.add(checks);

        add(wrapper);
    }

    /* ================= API ================= */

    public String query() {
        return tfSearch.getText().trim().toLowerCase();
    }

    public boolean matchesAny(String field, String value) {
        if (!enabledFields.contains(field)) return false;
        if (value == null) return false;
        return value.toLowerCase().contains(query());
    }
}
