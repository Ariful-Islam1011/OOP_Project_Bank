import javax.swing.*;
import java.awt.*;

public class NewATMCardIssueFrame extends JFrame {
    private JFrame parentFrame;
    private JTextField accountNumberField;
    private JTextField accountNameField;
    private JTextField mobileField;
    private JLabel accountStatusLabel;

    public NewATMCardIssueFrame(JFrame parent) {
        this.parentFrame = parent;
        setTitle("New ATM Card Issue");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        String projectDir = System.getProperty("user.dir");
        java.awt.Image bg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/pukur.jpg", projectDir + "/Icon/Hall.jpg");
        if (bg != null) setContentPane(new BackgroundPanel(bg));

        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.setPreferredSize(new Dimension(100, 80));
        ImageIcon logoIcon = UIUtils.loadScaledIcon(projectDir + "/Icon/DU_Logo.png", 48, 48);
        if (logoIcon != null) north.add(new JLabel(logoIcon), BorderLayout.WEST);
        JLabel title = new JLabel("Issue New ATM Card", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setForeground(new Color(255, 215, 0));
        north.add(title, BorderLayout.CENTER);

        // Content
        TranslucentPanel content = new TranslucentPanel(new Color(0, 0, 0, 120), 18, 18);
        content.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(12, 15, 12, 15);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        int row = 0;

        // Title
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        JLabel formTitle = new JLabel("Enter Account Information");
        formTitle.setFont(formTitle.getFont().deriveFont(Font.BOLD, 16f));
        formTitle.setForeground(new Color(76, 175, 80));
        content.add(formTitle, gc);

        // Separator
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(76, 175, 80));
        content.add(sep, gc);

        gc.gridwidth = 1;

        // Account Number
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        JLabel accLabel = new JLabel("Account Number:");
        accLabel.setForeground(Color.WHITE);
        accLabel.setFont(accLabel.getFont().deriveFont(Font.BOLD, 13f));
        content.add(accLabel, gc);

        gc.gridx = 1;
        accountNumberField = new JTextField(25);
        accountNumberField.setFont(accountNumberField.getFont().deriveFont(13f));
        accountNumberField.setBackground(new Color(50, 50, 50));
        accountNumberField.setForeground(Color.WHITE);
        accountNumberField.setCaretColor(Color.WHITE);
        content.add(accountNumberField, gc);

        // Account Name
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        JLabel nameLabel = new JLabel("Account Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 13f));
        content.add(nameLabel, gc);

        gc.gridx = 1;
        accountNameField = new JTextField(25);
        accountNameField.setFont(accountNameField.getFont().deriveFont(13f));
        accountNameField.setBackground(new Color(50, 50, 50));
        accountNameField.setForeground(Color.WHITE);
        accountNameField.setCaretColor(Color.WHITE);
        content.add(accountNameField, gc);

        // Mobile Number
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        JLabel mobileLabel = new JLabel("Mobile Number:");
        mobileLabel.setForeground(Color.WHITE);
        mobileLabel.setFont(mobileLabel.getFont().deriveFont(Font.BOLD, 13f));
        content.add(mobileLabel, gc);

        gc.gridx = 1;
        mobileField = new JTextField(25);
        mobileField.setFont(mobileField.getFont().deriveFont(13f));
        mobileField.setBackground(new Color(50, 50, 50));
        mobileField.setForeground(Color.WHITE);
        mobileField.setCaretColor(Color.WHITE);
        content.add(mobileField, gc);

        // Account Status Display
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        JLabel statusLabelText = new JLabel("Account Status:");
        statusLabelText.setForeground(Color.WHITE);
        statusLabelText.setFont(statusLabelText.getFont().deriveFont(Font.BOLD, 13f));
        content.add(statusLabelText, gc);

        gc.gridx = 1;
        accountStatusLabel = new JLabel("(Check by entering account number)");
        accountStatusLabel.setForeground(new Color(200, 200, 200));
        content.add(accountStatusLabel, gc);

        // Info panel
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><b>Instructions:</b> Enter the account number to verify account details. The cardholder name and mobile must match the account information.</html>");
        infoLabel.setForeground(new Color(200, 200, 200));
        infoLabel.setFont(infoLabel.getFont().deriveFont(11f));
        content.add(infoLabel, gc);

        // Buttons
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        gc.fill = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);

        JButton verifyBtn = createRoundedButton("Verify Account", new Color(30, 144, 255), 180, 50);
        JButton issueBtn = createRoundedButton("Issue Card", new Color(50, 205, 50), 180, 50);
        JButton backBtn = createRoundedButton("Back", new Color(205, 92, 0), 150, 50);

        buttonPanel.add(verifyBtn);
        buttonPanel.add(issueBtn);
        buttonPanel.add(backBtn);
        content.add(buttonPanel, gc);

        // Button Actions
        verifyBtn.addActionListener(e -> verifyAccount());
        issueBtn.addActionListener(e -> issueNewCard());
        backBtn.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        });

        // Scroll pane
        JScrollPane sp = new JScrollPane(content);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);

        add(north, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
    }

    private void verifyAccount() {
        String accountNumber = accountNumberField.getText().trim();
        if (accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter account number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Account account = DBHelper.getAccountByNumber(accountNumber);
            if (account == null) {
                accountStatusLabel.setText("Account NOT FOUND");
                accountStatusLabel.setForeground(new Color(255, 100, 100));
                accountNameField.setText("");
                mobileField.setText("");
                return;
            }

            accountStatusLabel.setText("Account FOUND - " + account.getName());
            accountStatusLabel.setForeground(new Color(0, 255, 0));
            accountNameField.setText(account.getName());
            mobileField.setText(account.getMobile());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error verifying account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void issueNewCard() {
        String accountNumber = accountNumberField.getText().trim();
        String accountName = accountNameField.getText().trim();
        String mobile = mobileField.getText().trim();

        if (accountNumber.isEmpty() || accountName.isEmpty() || mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and verify account", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Account account = DBHelper.getAccountByNumber(accountNumber);
            if (account == null) {
                JOptionPane.showMessageDialog(this, "Account not found. Please verify first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verify name and mobile match
            if (!account.getName().equalsIgnoreCase(accountName) || !account.getMobile().equals(mobile)) {
                JOptionPane.showMessageDialog(this, "Account name or mobile does not match the verified account", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Issue the card
            String cardDetails = DBHelper.issueATMCard(accountNumber, accountName);
            String[] details = cardDetails.split("\\|");
            
            JOptionPane.showMessageDialog(this,
                "ATM Card Issued Successfully!\n\n" +
                "Card Number: " + details[0] + "\n" +
                "Card PIN: " + details[1] + "\n" +
                "Account: " + accountNumber + "\n" +
                "Cardholder: " + accountName + "\n\n" +
                "Please keep this information secure!",
                "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear fields
            accountNumberField.setText("");
            accountNameField.setText("");
            mobileField.setText("");
            accountStatusLabel.setText("(Check by entering account number)");
            accountStatusLabel.setForeground(new Color(200, 200, 200));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error issuing card: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JButton createRoundedButton(String text, Color bgColor, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(); int h = getHeight();
                Color fill = bgColor;
                if (getModel().isPressed()) fill = bgColor.darker();
                else if (getModel().isRollover()) fill = bgColor.brighter();
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, w, h, 15, 15);
                g2.setColor(Color.WHITE);
                g2.setStroke(new java.awt.BasicStroke(2f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 15, 15);
                g2.dispose();
                setContentAreaFilled(false); setOpaque(false);
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, height));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorderPainted(false);
        return button;
    }
}
