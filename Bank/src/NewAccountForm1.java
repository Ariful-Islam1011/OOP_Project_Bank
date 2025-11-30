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

        // Profile Image upload moved here (replaces Initial Deposit)
        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Profile Image:"), c);
        c.gridx = 1; JTextField profileImg = new JTextField(18); profileImg.setEditable(false); p.add(profileImg, c);
        JButton browseProfile = new JButton("Choose"); c.gridx = 2; p.add(browseProfile, c);

        // removed PIN field from signup form per request

        y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Signature Image:"), c);
        c.gridx = 1; JTextField sigPath = new JTextField(18); sigPath.setEditable(false); p.add(sigPath, c);
        JButton browse = new JButton("Choose"); c.gridx = 2; p.add(browse, c);

        y++; c.gridx = 0; c.gridy = y; JButton submit = new JButton("Submit"); p.add(submit, c);
        c.gridx = 1; JButton back = new JButton("Back"); p.add(back, c);

        add(new JScrollPane(p));

        browseProfile.addActionListener((ActionEvent e) -> {
            JFileChooser fc = new JFileChooser();
            int r = fc.showOpenDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                profileImg.setText(f.getAbsolutePath());
            }
        });

        browse.addActionListener((ActionEvent e) -> {
            JFileChooser fc = new JFileChooser();
            int r = fc.showOpenDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                sigPath.setText(f.getAbsolutePath());
            }
        });

        submit.addActionListener((ActionEvent e) -> {
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
                a.setSignaturePath(sigPath.getText().trim());
                // store profile image path if provided (optional field)
                if (profileImg.getText() != null && !profileImg.getText().trim().isEmpty()) {
                    a.setSignaturePath(profileImg.getText().trim());
                }
                // initial balance set to 0 by default
                a.setBalance(0.0);

                String accNum = DBHelper.createAccount(a);
                JOptionPane.showMessageDialog(this, "Account created: " + accNum, "Success", JOptionPane.INFORMATION_MESSAGE);
                // go back to employee
                parent.setVisible(true);
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to create account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        back.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });
    }
}
