package com.lankacourier.UI.base;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDialog extends JDialog {

    protected JPanel formPanel = new JPanel(new GridBagLayout());
    protected JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    protected JButton btnSave;
    protected JButton btnCancel;

    public BaseDialog(Component parent, String title) {
        super(SwingUtilities.getWindowAncestor(parent), title, ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout(10, 10));
        setSize(520, 460);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        formPanel.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,16,16,16));

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        initButtons();
    }

    private void initButtons() {
        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
    }

    protected GridBagConstraints gbc(int x, int y) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = x;
        g.gridy = y;
        g.insets = new Insets(6,6,6,6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        return g;
    }

    protected abstract void onSave();
}
