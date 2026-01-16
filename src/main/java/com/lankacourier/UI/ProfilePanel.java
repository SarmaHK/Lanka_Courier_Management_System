package com.lankacourier.UI;

import com.lankacourier.Auth.Session;
import com.lankacourier.Controller.EmployeeController;
import com.lankacourier.Model.Employee;
import com.lankacourier.Util.FormatUtil;
import com.lankacourier.Util.HashUtil;
import com.lankacourier.Util.ModernUI;
import com.lankacourier.Util.SwingUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ProfilePanel extends JPanel {

    private final EmployeeController controller = new EmployeeController();
    private final Employee me = Session.getCurrentUser();

    private CircleAvatar avatar;
    private JLabel lblName;
    private JLabel lblRole;

    // Fields
    private JTextField tfFirst, tfLast, tfUser, tfMobile;
    private JPasswordField pfCurrent, pfNew, pfConfirm;

    public ProfilePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Split Layout: Left (Identity) | Right (Form)
        add(buildLeftPane(), BorderLayout.WEST);
        add(buildRightPane(), BorderLayout.CENTER);
    }

    /* ================= LEFT PANE (IDENTITY) ================= */
    private JPanel buildLeftPane() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setPreferredSize(new Dimension(300, 0));
        p.setBackground(new Color(248, 250, 252)); // Very light blue/gray
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));

        // Avatar Section
        avatar = new CircleAvatar(140);

        JButton btnChange = new JButton("Change Photo");
        btnChange.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnChange.setForeground(new Color(30, 136, 229));
        btnChange.setContentAreaFilled(false);
        btnChange.setBorderPainted(false);
        btnChange.setFocusPainted(false);
        btnChange.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChange.addActionListener(e -> choosePhoto());

        // Name & Role
        lblName = new JLabel(me.getFirstName() + " " + me.getLastName());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblName.setForeground(new Color(33, 33, 33));

        lblRole = new JLabel(me.getRole().toUpperCase());
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblRole.setForeground(new Color(120, 120, 120));
        lblRole.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                        BorderFactory.createEmptyBorder(4, 12, 4, 12)));

        // Layout Components
        p.add(Box.createVerticalStrut(60));

        JPanel avatarBox = new JPanel();
        avatarBox.setOpaque(false);
        avatarBox.add(avatar);
        p.add(avatarBox);
        p.add(Box.createVerticalStrut(10));

        JPanel btnBox = new JPanel();
        btnBox.setOpaque(false);
        btnBox.add(btnChange);
        p.add(btnBox);

        p.add(Box.createVerticalStrut(20));

        JPanel nameBox = new JPanel();
        nameBox.setOpaque(false);
        nameBox.add(lblName);
        p.add(nameBox);

        p.add(Box.createVerticalStrut(5));

        JPanel roleBox = new JPanel();
        roleBox.setOpaque(false);
        roleBox.add(lblRole);
        p.add(roleBox);

        p.add(Box.createVerticalGlue()); // Push content to top

        loadPhotoIfExists();
        return p;
    }

    /* ================= RIGHT PANE (SETTINGS) ================= */
    private JPanel buildRightPane() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);

        // Internal padding container
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.insets = new Insets(0, 0, 20, 0);
        gc.gridx = 0;

        // === HEADER ===
        JLabel title = new JLabel("Account Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        content.add(title, gc);

        // === SECTION 1: PERSONAL ===
        gc.insets = new Insets(20, 0, 15, 0);
        content.add(createSectionHeader("Personal Information"), gc);

        // Fields Layout
        JPanel form1 = new JPanel(new GridLayout(2, 2, 20, 15));
        form1.setOpaque(false);

        tfFirst = ModernUI.createTextField();
        tfFirst.setText(me.getFirstName());
        tfLast = ModernUI.createTextField();
        tfLast.setText(me.getLastName());
        tfUser = ModernUI.createTextField();
        tfUser.setText(me.getUsername());
        tfMobile = ModernUI.createTextField();
        tfMobile.setText(FormatUtil.formatMobile(me.getMobileNo()));
        tfUser.setEditable(false);

        form1.add(ModernUI.createFormGroup("First Name", tfFirst));
        form1.add(ModernUI.createFormGroup("Last Name", tfLast));
        form1.add(ModernUI.createFormGroup("Username", tfUser));
        form1.add(ModernUI.createFormGroup("Mobile Number", tfMobile));

        gc.insets = new Insets(0, 0, 30, 0);
        content.add(form1, gc);

        // === SECTION 2: SECURITY ===
        content.add(createSectionHeader("Security & Password"), gc);

        JPanel form2 = new JPanel(new GridLayout(1, 3, 20, 15));
        form2.setOpaque(false);

        pfCurrent = ModernUI.createPasswordField();
        pfNew = ModernUI.createPasswordField();
        pfConfirm = ModernUI.createPasswordField();

        form2.add(ModernUI.createFormGroup("Current Password", pfCurrent));
        form2.add(ModernUI.createFormGroup("New Password", pfNew));
        form2.add(ModernUI.createFormGroup("Confirm Password", pfConfirm));

        content.add(form2, gc);

        // === FOOTER ACTIONS ===
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actions.setOpaque(false);
        actions.setBorder(new EmptyBorder(30, 0, 0, 0));

        JButton btnSave = ModernUI.createPrimaryButton("Save Changes");
        btnSave.setPreferredSize(new Dimension(180, 45));
        btnSave.addActionListener(e -> saveProfile());

        actions.add(btnSave);

        gc.weighty = 1.0; // Push everything up
        gc.anchor = GridBagConstraints.NORTH;
        content.add(actions, gc);

        // ScrollPane for smaller screens
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JLabel createSectionHeader(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Smaller, caps-like
        l.setForeground(new Color(30, 136, 229));
        l.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(240, 240, 240)));
        return l;
    }

    /* ================= LOGIC ================= */
    private void saveProfile() {
        me.setFirstName(tfFirst.getText().trim());
        me.setLastName(tfLast.getText().trim());
        me.setMobileNo(FormatUtil.formatMobile(tfMobile.getText().trim()));

        String cur = new String(pfCurrent.getPassword()).trim();
        String np = new String(pfNew.getPassword()).trim();
        String cp = new String(pfConfirm.getPassword()).trim();

        if (!np.isEmpty() || !cp.isEmpty()) {
            if (cur.isEmpty()) {
                SwingUtils.error(this, "Enter current password to change it.");
                return;
            }
            if (!HashUtil.verifyPassword(cur, me.getPasswordHash())) {
                SwingUtils.error(this, "Incorrect current password.");
                return;
            }
            if (!np.equals(cp)) {
                SwingUtils.error(this, "New passwords do not match.");
                return;
            }
            me.setPasswordHash(HashUtil.hashPassword(np));
        }

        if (controller.update(me)) {
            SwingUtils.info(this, "Account updated successfully.");
            lblName.setText(me.getFirstName() + " " + me.getLastName());
            pfCurrent.setText("");
            pfNew.setText("");
            pfConfirm.setText("");
        } else {
            SwingUtils.error(this, "Failed to update profile.");
        }
    }

    /* ================= PHOTO & AVATAR ================= */
    private void choosePhoto() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File src = fc.getSelectedFile();
            String path = savePhoto(src);
            if (path == null)
                return;

            me.setPhotoPath(path);
            avatar.setImage(loadScaled(path));
            SwingUtils.info(this, "Profile photo updated. Click Save to persist.");
        }
    }

    private String savePhoto(File src) {
        try {
            File dir = new File("user-photos");
            if (!dir.exists())
                dir.mkdirs();
            String ext = src.getName().substring(src.getName().lastIndexOf('.'));
            File dst = new File(dir, "emp_" + me.getEmployeeId() + ext);
            Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return dst.getAbsolutePath();
        } catch (Exception ex) {
            SwingUtils.error(this, "Failed to save photo.");
            return null;
        }
    }

    private void loadPhotoIfExists() {
        if (me.getPhotoPath() == null)
            return;
        File f = new File(me.getPhotoPath());
        if (f.exists())
            avatar.setImage(loadScaled(f.getAbsolutePath()));
    }

    private Image loadScaled(String path) {
        return new ImageIcon(path).getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
    }
}

/* ================= CIRCULAR AVATAR ================= */
class CircleAvatar extends JComponent {

    private final int size;
    private Image image;

    public CircleAvatar(int size) {
        this.size = size;
        setPreferredSize(new Dimension(size, size));
    }

    public void setImage(Image img) {
        this.image = img;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(Color.WHITE);
        g2.fillOval(0, 0, size, size);

        // Border
        g2.setColor(new Color(230, 230, 230));
        g2.setStroke(new BasicStroke(3f));
        g2.drawOval(1, 1, size - 2, size - 2);

        // Clip
        g2.setClip(new Ellipse2D.Double(2, 2, size - 4, size - 4));

        if (image != null) {
            g2.drawImage(image, 0, 0, size, size, null);
        } else {
            drawDefaultAvatar(g2, size);
        }
        g2.dispose();
    }

    private void drawDefaultAvatar(Graphics2D g2, int size) {
        g2.setColor(new Color(220, 220, 220));
        g2.fillRect(0, 0, size, size);

        g2.setColor(new Color(150, 150, 150));

        int headSize = (int) (size * 0.4);
        int headX = (size - headSize) / 2;
        int headY = (int) (size * 0.2);
        g2.fillOval(headX, headY, headSize, headSize);

        GeneralPath body = new GeneralPath();
        int bodyW = (int) (size * 0.8);
        int bodyH = (int) (size * 0.5);
        int bodyX = (size - bodyW) / 2;
        int bodyY = headY + headSize + 5;

        body.moveTo(bodyX, bodyY + bodyH);
        body.quadTo(size / 2.0, bodyY - 20, bodyX + bodyW, bodyY + bodyH);
        g2.fill(body);
    }
}
