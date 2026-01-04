import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class NewAccountForm1 extends JFrame {
    private JFrame parent;

    public NewAccountForm1(JFrame parent) {
        this.parent = parent;
        setTitle("Open New Account - Dhaka University Bank");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main Panel with Black Background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 144, 255));
        header.setPreferredSize(new Dimension(800, 60));
        JLabel titleLabel = new JLabel("NEW ACCOUNT APPLICATION FORM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.BLACK);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 10, 8, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Personal Information Section
        int y = 0;
        c.gridx = 0; c.gridy = y; c.gridwidth = 2;
        JLabel personalSection = new JLabel("━━━━━ PERSONAL INFORMATION ━━━━━");
        personalSection.setFont(new Font("Segoe UI", Font.BOLD, 15));
        personalSection.setForeground(new Color(50, 205, 50));
        formPanel.add(personalSection, c);
        c.gridwidth = 1;

        y++; c.gridx = 0; c.gridy = y; 
        JLabel nameLabel = new JLabel("Full Name:*");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(nameLabel, c);
        c.gridx = 1; JTextField name = new JTextField(25); 
        name.setBackground(new Color(50, 50, 50));
        name.setForeground(Color.WHITE);
        name.setCaretColor(Color.WHITE);
        name.setPreferredSize(new Dimension(300, 30));
        formPanel.add(name, c);

        y++; c.gridx = 0; c.gridy = y; 
        JLabel fatherLabel = new JLabel("Father's Name:*");
        fatherLabel.setForeground(Color.WHITE);
        fatherLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(fatherLabel, c);
        c.gridx = 1; JTextField father = new JTextField(25); 
        father.setBackground(new Color(50, 50, 50));
        father.setForeground(Color.WHITE);
        father.setCaretColor(Color.WHITE);
        father.setPreferredSize(new Dimension(300, 30));
        formPanel.add(father, c);

        y++; c.gridx = 0; c.gridy = y; 
        JLabel motherLabel = new JLabel("Mother's Name:*");
        motherLabel.setForeground(Color.WHITE);
        motherLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(motherLabel, c);
        c.gridx = 1; JTextField mother = new JTextField(25); 
        mother.setBackground(new Color(50, 50, 50));
        mother.setForeground(Color.WHITE);
        mother.setCaretColor(Color.WHITE);
        mother.setPreferredSize(new Dimension(300, 30));
        formPanel.add(mother, c);

        y++; c.gridx = 0; c.gridy = y; 
        JLabel dobLabel = new JLabel("Date of Birth:*");
        dobLabel.setForeground(Color.WHITE);
        dobLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(dobLabel, c);
        c.gridx = 1; JTextField dob = new JTextField(15); 
        dob.setBackground(new Color(50, 50, 50));
        dob.setForeground(Color.WHITE);
        dob.setCaretColor(Color.WHITE);
        dob.setToolTipText("Format: YYYY-MM-DD");
        dob.setPreferredSize(new Dimension(300, 30));
        formPanel.add(dob, c);

        y++; c.gridx = 0; c.gridy = y; 
        JLabel genderLabel = new JLabel("Gender:*");
        genderLabel.setForeground(Color.WHITE);
        genderLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(genderLabel, c);
        c.gridx = 1; JComboBox<String> gender = new JComboBox<>(new String[]{"Male","Female","Other"}); 
        gender.setBackground(Color.WHITE);
        gender.setPreferredSize(new Dimension(300, 30));
        formPanel.add(gender, c);

        y++; c.gridx = 0; c.gridy = y; 
        JLabel nidLabel = new JLabel("NID Number:*");
        nidLabel.setForeground(Color.WHITE);
        nidLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(nidLabel, c);
        c.gridx = 1; JTextField nid = new JTextField(20); 
        nid.setBackground(new Color(50, 50, 50));
        nid.setForeground(Color.WHITE);
        nid.setCaretColor(Color.WHITE);
        nid.setPreferredSize(new Dimension(300, 30));
        formPanel.add(nid, c);

        // Contact Information
        y++; c.gridx = 0; c.gridy = y; c.gridwidth = 2;
        JLabel contactSection = new JLabel("━━━━━ CONTACT INFORMATION ━━━━━");
        contactSection.setFont(new Font("Segoe UI", Font.BOLD, 15));
        contactSection.setForeground(new Color(255, 165, 0));
        formPanel.add(contactSection, c);
        c.gridwidth = 1;

        y++; c.gridx = 0; c.gridy = y; 
        JLabel mobileLabel = new JLabel("Mobile Number:*");
        mobileLabel.setForeground(Color.WHITE);
        mobileLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(mobileLabel, c);
        c.gridx = 1; JTextField mobile = new JTextField(15); 
        mobile.setBackground(new Color(50, 50, 50));
        mobile.setForeground(Color.WHITE);
        mobile.setCaretColor(Color.WHITE);
        mobile.setPreferredSize(new Dimension(300, 30));
        formPanel.add(mobile, c);

        y++; c.gridx = 0; c.gridy = y; 
        JLabel addressLabel = new JLabel("Address:*");
        addressLabel.setForeground(Color.WHITE);
        addressLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(addressLabel, c);
        c.gridx = 1; JTextField address = new JTextField(25); 
        address.setBackground(new Color(50, 50, 50));
        address.setForeground(Color.WHITE);
        address.setCaretColor(Color.WHITE);
        address.setPreferredSize(new Dimension(300, 30));
        formPanel.add(address, c);

        // Account Details
        y++; c.gridx = 0; c.gridy = y; c.gridwidth = 2;
        JLabel accountSection = new JLabel("━━━━━ ACCOUNT DETAILS ━━━━━");
        accountSection.setFont(new Font("Segoe UI", Font.BOLD, 15));
        accountSection.setForeground(new Color(138, 43, 226));
        formPanel.add(accountSection, c);
        c.gridwidth = 1;

        y++; c.gridx = 0; c.gridy = y; 
        JLabel accTypeLabel = new JLabel("Account Type:*");
        accTypeLabel.setForeground(Color.WHITE);
        accTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(accTypeLabel, c);
        c.gridx = 1; JComboBox<String> accType = new JComboBox<>(new String[]{"Savings","Current","Fixed"}); 
        accType.setBackground(Color.WHITE);
        accType.setPreferredSize(new Dimension(300, 30));
        formPanel.add(accType, c);

        // Document Upload
        y++; c.gridx = 0; c.gridy = y; c.gridwidth = 2;
        JLabel docSection = new JLabel("━━━━━ DOCUMENT UPLOAD ━━━━━");
        docSection.setFont(new Font("Segoe UI", Font.BOLD, 15));
        docSection.setForeground(new Color(255, 69, 0));
        formPanel.add(docSection, c);
        c.gridwidth = 1;

        // Profile Image upload
        y++; c.gridx = 0; c.gridy = y; 
        JLabel profileLabel = new JLabel("Profile Photo:");
        profileLabel.setForeground(Color.WHITE);
        profileLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(profileLabel, c);
        c.gridx = 1; 
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        profilePanel.setOpaque(false);
        JTextField profileImgField = new JTextField(15); 
        profileImgField.setEditable(false);
        profileImgField.setBackground(new Color(50, 50, 50)); 
        profileImgField.setForeground(Color.WHITE);
        JButton browseProfileBtn = new JButton("Browse");
        browseProfileBtn.setForeground(Color.WHITE);
        browseProfileBtn.setBackground(new Color(30, 144, 255));
        browseProfileBtn.setOpaque(true);
        browseProfileBtn.setFocusPainted(false);
        browseProfileBtn.setPreferredSize(new Dimension(90, 28));
        browseProfileBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        profilePanel.add(profileImgField);
        profilePanel.add(browseProfileBtn);
        formPanel.add(profilePanel, c);

        // Signature Image upload
        y++; c.gridx = 0; c.gridy = y; 
        JLabel sigLabel = new JLabel("Signature:");
        sigLabel.setForeground(Color.WHITE);
        sigLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(sigLabel, c);
        c.gridx = 1; 
        JPanel sigPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        sigPanel.setOpaque(false);
        JTextField sigPathField = new JTextField(15); 
        sigPathField.setEditable(false);
        sigPathField.setBackground(new Color(50, 50, 50)); 
        sigPathField.setForeground(Color.WHITE);
        JButton browseSigBtn = new JButton("Browse");
        browseSigBtn.setForeground(Color.WHITE);
        browseSigBtn.setBackground(new Color(30, 144, 255));
        browseSigBtn.setOpaque(true);
        browseSigBtn.setFocusPainted(false);
        browseSigBtn.setPreferredSize(new Dimension(90, 28));
        browseSigBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        sigPanel.add(sigPathField);
        sigPanel.add(browseSigBtn);
        formPanel.add(sigPanel, c);

        // Action Buttons
        y++; c.gridx = 0; c.gridy = y; c.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);
        
        JButton submitBtn = new JButton("CREATE ACCOUNT");
        submitBtn.setForeground(Color.WHITE); 
        submitBtn.setOpaque(true); 
        submitBtn.setBackground(new Color(50, 205, 50));
        submitBtn.setFocusPainted(false);
        submitBtn.setPreferredSize(new Dimension(180, 40));
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        
        JButton backBtn = new JButton("CANCEL");
        backBtn.setForeground(Color.WHITE); 
        backBtn.setOpaque(true); 
        backBtn.setBackground(new Color(205, 92, 0));
        backBtn.setFocusPainted(false);
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        
        buttonPanel.add(submitBtn);
        buttonPanel.add(backBtn);
        formPanel.add(buttonPanel, c);

        // Scroll pane for form
        JScrollPane sp = new JScrollPane(formPanel);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getViewport().setBackground(Color.BLACK);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(sp, BorderLayout.CENTER);
        add(mainPanel);

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
                // Validation
                if (name.getText().trim().isEmpty() || father.getText().trim().isEmpty() || 
                    mother.getText().trim().isEmpty() || dob.getText().trim().isEmpty() ||
                    mobile.getText().trim().isEmpty() || address.getText().trim().isEmpty() ||
                    nid.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all required fields (*)", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

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
                
                if (profileImgField.getText() != null && !profileImgField.getText().trim().isEmpty()) {
                    a.setProfileImagePath(profileImgField.getText().trim());
                }
                a.setBalance(0.0);

                String accNum = DBHelper.createAccount(a);
                
                // Success message with account number
                JOptionPane.showMessageDialog(this, 
                    "Account created successfully!\n\nAccount Number: " + accNum + 
                    "\n\nWelcome to Dhaka University Bank!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                parent.setVisible(true);
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Failed to create account:\n" + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });
    }
}
