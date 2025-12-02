import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class NewAccountForm1 extends JFrame {
    private JFrame parent;

    public NewAccountForm1(JFrame parent) {
        this.parent = parent;
        setTitle("New Account - Signup Form (v1)");
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridBagLayout());
        // use translucent white background so text is readable over page backgrounds
        p.setOpaque(true);
        p.setBackground(new Color(255,255,255,220));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Name:"), c);
        c.gridx = 1; JTextField name = new JTextField(20); p.add(name, c);

        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Father's Name:"), c);
        c.gridx = 1; JTextField father = new JTextField(20); p.add(father, c);

        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Mother's Name:"), c);
        c.gridx = 1; JTextField mother = new JTextField(20); p.add(mother, c);

        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("DOB (YYYY-MM-DD):"), c);
        c.gridx = 1; JTextField dob = new JTextField(12); p.add(dob, c);

        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Gender:"), c);
        c.gridx = 1; JComboBox<String> gender = new JComboBox<>(new String[]{"Male","Female","Other"}); p.add(gender, c);

        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Mobile:"), c);
        c.gridx = 1; JTextField mobile = new JTextField(12); p.add(mobile, c);

        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Address:"), c);
        c.gridx = 1; JTextField address = new JTextField(20); p.add(address, c);

        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("NID:"), c);
        c.gridx = 1; JTextField nid = new JTextField(20); p.add(nid, c);

        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Account Type:"), c);
        c.gridx = 1; JComboBox<String> accType = new JComboBox<>(new String[]{"Savings","Current","Fixed"}); p.add(accType, c);

        // Profile Image upload
        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Profile Image:"), c);
        c.gridx = 1; JTextField profileImgField = new JTextField(18); profileImgField.setEditable(false);
        profileImgField.setBackground(new Color(255,255,255,220)); profileImgField.setForeground(Color.BLACK); p.add(profileImgField, c);
        JButton browseProfileBtn = new JButton("Choose");
        browseProfileBtn.setForeground(Color.WHITE); browseProfileBtn.setOpaque(false); browseProfileBtn.setContentAreaFilled(false); browseProfileBtn.setBorderPainted(false);
        c.gridx = 2; p.add(browseProfileBtn, c);

        // Signature Image upload
        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Signature Image:"), c);
        c.gridx = 1; JTextField sigPathField = new JTextField(18); sigPathField.setEditable(false);
        sigPathField.setBackground(new Color(255,255,255,220)); sigPathField.setForeground(Color.BLACK); p.add(sigPathField, c);
        JButton browseSigBtn = new JButton("Choose");
        browseSigBtn.setForeground(Color.WHITE); browseSigBtn.setOpaque(false); browseSigBtn.setContentAreaFilled(false); browseSigBtn.setBorderPainted(false);
        c.gridx = 2; p.add(browseSigBtn, c);

        // Buttons (solid, visible on translucent panel)
        y++; c.gridx = 0; c.gridy = y; JButton submitBtn = new JButton("Submit");
        submitBtn.setForeground(Color.WHITE); submitBtn.setOpaque(true); submitBtn.setContentAreaFilled(true); submitBtn.setBorderPainted(false);
        submitBtn.setBackground(new Color(0, 120, 215));
        p.add(submitBtn, c);
        c.gridx = 1; JButton backBtn = new JButton("Back");
        backBtn.setForeground(Color.WHITE); backBtn.setOpaque(true); backBtn.setContentAreaFilled(true); backBtn.setBorderPainted(false);
        backBtn.setBackground(new Color(120, 120, 120));
        p.add(backBtn, c);

        JScrollPane sp = new JScrollPane(p);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        add(sp);

        // Action listeners
        browseProfileBtn.addActionListener((ActionEvent e) -> {
            JFileChooser fc = new JFileChooser();
            int r = fc.showOpenDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                profileImgField.setText(f.getAbsolutePath());
            }
        });

        browseSigBtn.addActionListener((ActionEvent e) -> {
            JFileChooser fc = new JFileChooser();
            int r = fc.showOpenDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                sigPathField.setText(f.getAbsolutePath());
            }
        });

        submitBtn.addActionListener((ActionEvent e) -> {
            try {
                Account a = new Account();
                a.setName(name.getText().trim());
                a.setFather(father.getText().trim());
                a.setMother(mother.getText().trim());
                a.setDob(dob.getText().trim());
                a.setGender((String)gender.getSelectedItem());
                a.setMobile(mobile.getText().trim());
                a.setAddress(address.getText().trim());
                a.setNid(nid.getText().trim());
                a.setAccountType((String)accType.getSelectedItem());
                a.setPin("");
                a.setSignaturePath(sigPathField.getText().trim());
                // store profile image path separately (if provided)
                if (profileImgField.getText() != null && !profileImgField.getText().trim().isEmpty()) {
                    a.setProfileImagePath(profileImgField.getText().trim());
                }
                a.setBalance(0.0);

                String accNum = DBHelper.createAccount(a);
                JOptionPane.showMessageDialog(this, "Account created: " + accNum, "Success", JOptionPane.INFORMATION_MESSAGE);
                parent.setVisible(true);
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to create account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });

        SwingUtilities.invokeLater(() -> {
            for (Component comp : p.getComponents()) {
                if (comp instanceof JLabel) ((JLabel)comp).setForeground(Color.BLACK);
            }
        });
    }
}
