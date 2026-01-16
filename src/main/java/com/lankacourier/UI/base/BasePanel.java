package com.lankacourier.UI.base;

import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {

    protected JPanel createTopBar(JComponent left, String title, JComponent right) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));

        bar.add(left, BorderLayout.WEST);
        bar.add(lbl, BorderLayout.CENTER);
        if (right != null) bar.add(right, BorderLayout.EAST);

        return bar;
    }

    protected JScrollPane wrapTable(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        return sp;
    }

    protected JPanel createCard(JComponent top, JComponent center) {
        JPanel card = new JPanel(new BorderLayout(8,8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));
        card.add(top, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        return card;
    }
}
