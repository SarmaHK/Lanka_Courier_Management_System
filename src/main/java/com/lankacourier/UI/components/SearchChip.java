package com.lankacourier.UI.components;

import javax.swing.*;
import java.awt.*;

public class SearchChip extends JPanel {

    public SearchChip() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 4));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(4, 14, 4, 14));
        add(new JLabel("🔍"));
        add(new JLabel("Search"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(235, 238, 242));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
        super.paintComponent(g);
    }
}
