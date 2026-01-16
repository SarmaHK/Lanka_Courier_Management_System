package com.lankacourier.UI.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class ModernComboBox<T> extends JComboBox<T> {

    public ModernComboBox(T[] items) {
        super(items);
        setUI(new BasicComboBoxUI());
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
    }
}
