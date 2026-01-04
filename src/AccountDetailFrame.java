import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AccountDetailFrame extends JFrame {
    private JFrame parent;
    private Account account;

    public AccountDetailFrame(JFrame parent, Account acctParam) {
        this.parent = parent;
        this.account = acctParam;
        setTitle("Account: " + account.getAccountNumber());
        setSize(700,500);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(new Color(56, 142, 60));

        // Profile tab (personal details)
        String projectDir = System.getProperty("user.dir");
        // profile background: prefer `Doyel.jpg`, full screen background
        java.awt.Image profImg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/Doyel.jpg", projectDir + "/Icon/Tsc.jpg", projectDir + "/Icon/tsc .jpg", projectDir + "/Icon/tsc.jpg", projectDir + "/Icon/udoyon.jpg", projectDir + "/Icon/chemistry.jpg", projectDir + "/Icon/Science_Library.jpg");
        JPanel pd = profImg != null ? new BackgroundPanel(profImg) : new JPanel();
        pd.setLayout(new GridBagLayout());
        pd.setOpaque(false);
        // remove padding so background photo fills edge-to-edge
        pd.setBorder(BorderFactory.createEmptyBorder());

        // create scrollable content with minimal padding
        JPanel scrollContent = new JPanel(new GridBagLayout());
        scrollContent.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8,15,8,15);
        gc.anchor = GridBagConstraints.WEST;
        // large balance label at top of profile
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        JLabel bigBal = new JLabel("Balance: " + account.getBalance(), SwingConstants.CENTER);
        bigBal.setForeground(Color.WHITE);
        bigBal.setFont(bigBal.getFont().deriveFont(Font.BOLD, 20f));
        scrollContent.add(bigBal, gc);
        gc.gridwidth = 1;
        gc.gridy++;
        gc.gridx = 0; scrollContent.add(new JLabel("Account Number:"), gc); gc.gridx = 1; scrollContent.add(new JLabel(account.getAccountNumber()), gc);
        gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("Name:"), gc); gc.gridx = 1; scrollContent.add(new JLabel(account.getName()), gc);
        gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("Father:"), gc); gc.gridx = 1; scrollContent.add(new JLabel(account.getFather()), gc);
        gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("Mother:"), gc); gc.gridx = 1; scrollContent.add(new JLabel(account.getMother()), gc);
        gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("DOB:"), gc); gc.gridx = 1; scrollContent.add(new JLabel(account.getDob()), gc);
        gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("Gender:"), gc); gc.gridx = 1; scrollContent.add(new JLabel(account.getGender()), gc);
        gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("Mobile:"), gc); gc.gridx = 1; scrollContent.add(new JLabel(account.getMobile()), gc);
        gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("Address:"), gc); gc.gridx = 1; scrollContent.add(new JLabel(account.getAddress()), gc);
        gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("NID:"), gc); gc.gridx = 1; scrollContent.add(new JLabel(account.getNid()), gc);
        gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("Account Type:"), gc); gc.gridx = 1; scrollContent.add(new JLabel(account.getAccountType()), gc);
        gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("Balance:"), gc); gc.gridx = 1; JLabel balLabel = new JLabel(String.valueOf(account.getBalance())); balLabel.setFont(balLabel.getFont().deriveFont(Font.BOLD, 14f)); scrollContent.add(balLabel, gc);

        // ATM Cards display
        try {
            java.util.List<java.util.Map<String, Object>> cards = DBHelper.getATMCards(account.getAccountNumber());
            if (!cards.isEmpty()) {
                gc.gridx = 0; gc.gridy++; 
                JLabel atmLabel = new JLabel("ATM Cards:");
                atmLabel.setFont(atmLabel.getFont().deriveFont(Font.BOLD, 13f));
                scrollContent.add(atmLabel, gc);
                
                gc.gridx = 1;
                JPanel cardsPanel = new JPanel();
                cardsPanel.setOpaque(false);
                cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
                
                for (java.util.Map<String, Object> card : cards) {
                    String cardInfo = String.format("%s (%s) - Exp: %s", 
                        card.get("cardNumber"), 
                        card.get("status"),
                        card.get("expiryDate"));
                    JLabel cardLabel = new JLabel(cardInfo);
                    cardLabel.setForeground(Color.WHITE);
                    cardLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    cardsPanel.add(cardLabel);
                }
                scrollContent.add(cardsPanel, gc);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // signature display (if exists)
        if (account.getSignaturePath() != null && !account.getSignaturePath().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(account.getSignaturePath());
                Image img = icon.getImage().getScaledInstance(200,80,Image.SCALE_SMOOTH);
                JLabel sig = new JLabel(new ImageIcon(img));
                gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("Signature:"), gc); gc.gridx = 1; scrollContent.add(sig, gc);
            } catch (Exception ex) { gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("Signature:"), gc); gc.gridx = 1; scrollContent.add(new JLabel("(failed to load)"), gc); }
        }

        // show profile photo if provided
        if (account.getProfileImagePath() != null && !account.getProfileImagePath().isEmpty()) {
            try {
                ImageIcon photoIcon = new ImageIcon(account.getProfileImagePath());
                Image pimg = photoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    gc.gridx = 0; gc.gridy++; scrollContent.add(new JLabel("Photo:"), gc);
                    gc.gridx = 1; JLabel photoLbl = new JLabel(new ImageIcon(pimg)); photoLbl.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 220, 160), 2)); scrollContent.add(photoLbl, gc);
            } catch (Exception ex) { /* fall back to no photo */ }
        }

        // ensure all labels in content are white for readability
        SwingUtilities.invokeLater(() -> {
            for (Component comp : scrollContent.getComponents()) {
                if (comp instanceof JLabel) ((JLabel)comp).setForeground(Color.WHITE);
            }
        });

        // wrap content with a light translucent panel so text stays readable over photo
        // Slightly darker overlay so text stays readable on the photo background
        // flat overlay (no rounded corners) to avoid visible white edges
        TranslucentPanel profileContent = new TranslucentPanel(new Color(0,0,0,150), 0, 0);
        profileContent.setLayout(new GridBagLayout());
        GridBagConstraints pcons = new GridBagConstraints();
        pcons.gridx = 0; pcons.gridy = 0; pcons.weightx = 1; pcons.weighty = 1; pcons.fill = GridBagConstraints.BOTH;
        profileContent.add(scrollContent, pcons);

        // add the content panel into the profile container
        GridBagConstraints mainPcons = new GridBagConstraints();
        mainPcons.gridx = 0; mainPcons.gridy = 0; mainPcons.weightx = 1; mainPcons.weighty = 1; mainPcons.fill = GridBagConstraints.BOTH;
        pd.add(profileContent, mainPcons);

        JScrollPane profileSp = new JScrollPane(pd);
        // keep the scroll viewport transparent so the background shows, but the content panel itself remains semi-opaque
        profileSp.setOpaque(false);
        profileSp.getViewport().setOpaque(false);
        profileSp.setBorder(BorderFactory.createEmptyBorder());
        tabs.addTab("Profile", profileSp);


        // Transactions panel
        // transactions background: prefer `pukur.jpg`, fall back to Hall and tsc
        java.awt.Image txBg = UIUtils.loadImageFromCandidates(System.getProperty("user.dir") + "/Icon/pukur.jpg", System.getProperty("user.dir") + "/Icon/Hall.jpg", System.getProperty("user.dir") + "/Icon/tsc .jpg", System.getProperty("user.dir") + "/Icon/Tsc.jpg");
        JPanel tx = txBg != null ? new BackgroundPanel(txBg) : new JPanel(new BorderLayout());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10)); actions.setOpaque(false);
        Color depositColor = new Color(50, 205, 50); // Green
        Color withdrawColor = new Color(255, 69, 0); // Red-Orange
        Color transferColor = new Color(30, 144, 255); // Blue
        JButton dep = createRoundedButton("Deposit", depositColor, 130, 35);
        JButton wit = createRoundedButton("Withdraw", withdrawColor, 130, 35);
        JButton transfer = createRoundedButton("Transfer", transferColor, 130, 35);
        
        actions.add(dep); actions.add(wit); actions.add(transfer);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)); filterPanel.setOpaque(false);
        JLabel filterLabel = new JLabel("Filter By Type:"); filterLabel.setForeground(Color.WHITE); filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        String[] txTypes = {"All", "Deposit", "Withdraw", "Transfer"};
        JComboBox<String> typeFilter = new JComboBox<>(txTypes); 
        typeFilter.setForeground(Color.BLACK);
        typeFilter.setBackground(Color.WHITE);
        typeFilter.setPreferredSize(new Dimension(120, 30));
        
        JLabel serialLabel = new JLabel("Serial Number:"); serialLabel.setForeground(Color.WHITE); serialLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField serialField = new JTextField(15);
        serialField.setPreferredSize(new Dimension(150, 30));
        serialField.setForeground(Color.BLACK);
        serialField.setBackground(Color.WHITE);
        
        JButton filterBtn = createRoundedButton("Apply", new Color(30, 144, 255), 110, 30);
        JButton clearBtn = createRoundedButton("Clear", new Color(100, 100, 100), 110, 30);
        
        filterPanel.add(filterLabel); 
        filterPanel.add(typeFilter);
        filterPanel.add(serialLabel);
        filterPanel.add(serialField);
        filterPanel.add(filterBtn);
        filterPanel.add(clearBtn);

        JTextArea statement = new JTextArea();
        statement.setEditable(false);
        statement.setForeground(Color.WHITE);
        statement.setOpaque(false);
        statement.setBackground(new Color(0,0,0,0));
        statement.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane statementSp = new JScrollPane(statement);
        statementSp.setOpaque(false);
        statementSp.getViewport().setOpaque(false);
        statementSp.getViewport().setBackground(new Color(0,0,0,80));
        
        // Put actions, filter and statement into a semi-opaque content panel so they are readable on top of background
        TranslucentPanel txContent = new TranslucentPanel(new Color(0,0,0,120), 18, 18);
        txContent.setLayout(new BorderLayout(5, 5));
        txContent.add(actions, BorderLayout.NORTH);
        JPanel filterAndStatementWrapper = new JPanel(new BorderLayout(5, 5));
        filterAndStatementWrapper.setOpaque(false);
        filterAndStatementWrapper.add(filterPanel, BorderLayout.NORTH);
        filterAndStatementWrapper.add(statementSp, BorderLayout.CENTER);
        txContent.add(filterAndStatementWrapper, BorderLayout.CENTER);
        
        // add content into the transactions container (which may be a BackgroundPanel)
        tx.add(txContent, BorderLayout.CENTER);

        dep.addActionListener(e -> {
            try {
                String s = JOptionPane.showInputDialog(this, "Enter deposit amount:");
                if (s == null) return;
                double v = Double.parseDouble(s.trim());
                if (v <= 0) return;
                if (DBHelper.changeBalance(account.getAccountNumber(), v, "Deposit")) {
                    account = DBHelper.getAccountByNumber(account.getAccountNumber());
                    JOptionPane.showMessageDialog(this, "Deposit successful. New balance: " + account.getBalance());
                    refreshStatement(statement, "All");
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        wit.addActionListener(e -> {
            try {
                String s = JOptionPane.showInputDialog(this, "Enter withdrawal amount:");
                if (s == null) return;
                double v = Double.parseDouble(s.trim());
                if (v <= 0) return;
                if (DBHelper.changeBalance(account.getAccountNumber(), -v, "Withdraw")) {
                    account = DBHelper.getAccountByNumber(account.getAccountNumber());
                    JOptionPane.showMessageDialog(this, "Withdrawal successful. New balance: " + account.getBalance());
                    refreshStatement(statement, "All");
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient funds", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        transfer.addActionListener(e -> {
            new BalanceTransferFrame(this, account).setVisible(true);
            setVisible(false);
        });

        filterBtn.addActionListener(e -> {
            String selectedType = (String) typeFilter.getSelectedItem();
            String serialNumber = serialField.getText().trim();
            
            if (!serialNumber.isEmpty()) {
                // Filter by serial number
                try {
                    java.util.List<TransactionRecord> recs = DBHelper.getTransactionsBySerialNumber(account.getAccountNumber(), serialNumber);
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format("%-20s | %-12s | %-12s | %-15s | %-20s\n", "Serial Number", "Type", "Amount", "Balance", "Timestamp"));
                    sb.append("=".repeat(100)).append("\n");
                    if (recs.isEmpty()) {
                        sb.append("No transactions found for serial number: ").append(serialNumber).append("\n");
                    } else {
                        for (TransactionRecord r : recs) {
                            sb.append(String.format("%-20s | %-12s | %-12.2f | %-15.2f | %-20s\n", 
                                r.getSerialNumber(), r.getType(), r.getAmount(), r.getBalance(), r.getTimestamp()));
                        }
                    }
                    statement.setText(sb.toString());
                } catch (Exception ex) { ex.printStackTrace(); }
            } else {
                // Filter by type
                refreshStatement(statement, selectedType);
            }
        });
        
        clearBtn.addActionListener(e -> {
            typeFilter.setSelectedItem("All");
            serialField.setText("");
            refreshStatement(statement, "All");
        });

        // add Balance button into transactions actions
        JButton balBtn = new JButton("Balance"); 
        balBtn.setForeground(Color.WHITE); 
        balBtn.setOpaque(true); 
        balBtn.setBackground(new Color(138, 43, 226)); // Purple
        balBtn.setFocusPainted(false);
        balBtn.setPreferredSize(new Dimension(100, 35));
        balBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        balBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        actions.add(balBtn);
        tabs.addTab("Transactions", tx);

        balBtn.addActionListener(e -> {
            try {
                account = DBHelper.getAccountByNumber(account.getAccountNumber());
                JOptionPane.showMessageDialog(this, "Current balance: " + account.getBalance(), "Balance", JOptionPane.INFORMATION_MESSAGE);
                balLabel.setText(String.valueOf(account.getBalance()));
                bigBal.setText("Balance: " + account.getBalance());
                refreshStatement(statement, "All");
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        add(tabs, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        JButton editBtn = UIUtils.createStyledButton("✏️ Edit", new Color(255, 152, 0), 130, 40);
        JButton back = UIUtils.createStyledButton("◄ Back", new Color(244, 67, 54), 130, 40);
        bottom.add(editBtn);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        back.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });

        editBtn.addActionListener(e -> {
            EditAccountDialog dlg = new EditAccountDialog(this, account);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                // use the updated Account from the dialog to refresh the UI without relying on DB
                Account updated = dlg.getAccount();
                if (updated != null) {
                    account = updated;
                }
                // rebuild the frame from the in-memory account so UI updates even if DB failed
                AccountDetailFrame f = new AccountDetailFrame(parent, account);
                f.setVisible(true);
                dispose();
            }
        });

        // load statement initially
        refreshStatement(statement, "All");
    }

    private void refreshStatement(JTextArea statement, String filter) {
        try {
            java.util.List<TransactionRecord> recs;
            if ("All".equals(filter)) {
                recs = DBHelper.getTransactions(account.getAccountNumber(), 100);
            } else {
                recs = DBHelper.getTransactionsByType(account.getAccountNumber(), filter);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-20s | %-12s | %-12s | %-15s | %-20s\n", "Serial Number", "Type", "Amount", "Balance", "Timestamp"));
            sb.append("=".repeat(100)).append("\n");
            for (TransactionRecord r : recs) {
                sb.append(String.format("%-20s | %-12s | %-12.2f | %-15.2f | %-20s\n", 
                    r.getSerialNumber(), r.getType(), r.getAmount(), r.getBalance(), r.getTimestamp()));
            }
            statement.setText(sb.toString());
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    // simple modal edit dialog for account profile editing
    private static class EditAccountDialog extends JDialog {
        private boolean saved = false;
        private final Account accountRef;
        public EditAccountDialog(JFrame parent, Account a) {
            super(parent, "Edit Account - " + a.getAccountNumber(), true);
            setSize(500, 520);
            setLocationRelativeTo(parent);
            JPanel p = new JPanel(new GridBagLayout());
            p.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(6,6,6,6);
            c.fill = GridBagConstraints.HORIZONTAL;

            int y = 0;
            y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Name:"), c); c.gridx = 1; JTextField name = new JTextField(a.getName(), 20); p.add(name, c);
            y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Father:"), c); c.gridx = 1; JTextField father = new JTextField(a.getFather(), 20); p.add(father, c);
            y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Mother:"), c); c.gridx = 1; JTextField mother = new JTextField(a.getMother(), 20); p.add(mother, c);
            y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("DOB:"), c); c.gridx = 1; JTextField dob = new JTextField(a.getDob(), 12); p.add(dob, c);
            y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Gender:"), c); c.gridx = 1; JComboBox<String> gender = new JComboBox<>(new String[]{"Male","Female","Other"}); gender.setSelectedItem(a.getGender()); p.add(gender, c);
            y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Mobile:"), c); c.gridx = 1; JTextField mobile = new JTextField(a.getMobile(), 12); p.add(mobile, c);
            y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Address:"), c); c.gridx = 1; JTextField address = new JTextField(a.getAddress(), 20); p.add(address, c);
            y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("NID:"), c); c.gridx = 1; JTextField nid = new JTextField(a.getNid(), 20); p.add(nid, c);
            y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Account Type:"), c); c.gridx = 1; JComboBox<String> accType = new JComboBox<>(new String[]{"Savings","Current","Fixed"}); accType.setSelectedItem(a.getAccountType()); p.add(accType, c);
            // hold a reference to the passed account so we can return it even if DB fails
            this.accountRef = a;

            y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Signature Path:"), c); c.gridx = 1; JTextField sig = new JTextField(a.getSignaturePath(), 18); sig.setEditable(false); p.add(sig, c); JButton sigBtn = new JButton("Choose"); c.gridx = 2; p.add(sigBtn, c);
            y++; c.gridx = 0; c.gridy = y; p.add(new JLabel("Profile Photo:"), c); c.gridx = 1; JTextField photo = new JTextField(a.getProfileImagePath(), 18); photo.setEditable(false); p.add(photo, c); JButton photoBtn = new JButton("Choose"); c.gridx = 2; p.add(photoBtn, c);

            JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton save = new JButton("Save"); JButton cancel = new JButton("Cancel");
            btns.add(save); btns.add(cancel);

            save.addActionListener(e -> {
                try {
                    // update the in-memory account object first
                    a.setName(name.getText().trim());
                    a.setFather(father.getText().trim());
                    a.setMother(mother.getText().trim());
                    a.setDob(dob.getText().trim());
                    a.setGender((String)gender.getSelectedItem());
                    a.setMobile(mobile.getText().trim());
                    a.setAddress(address.getText().trim());
                    a.setNid(nid.getText().trim());
                    a.setAccountType((String)accType.getSelectedItem());
                    a.setSignaturePath(sig.getText().trim());
                    a.setProfileImagePath(photo.getText().trim());
                    // try to persist; if DB is unavailable, inform the user but keep the changes in-memory
                    try {
                        DBHelper.updateAccount(a);
                        } catch (java.sql.SQLException sqle) {
                        sqle.printStackTrace();
                        JOptionPane.showMessageDialog(EditAccountDialog.this, "Saved locally but failed to persist to DB: " + sqle.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    saved = true;
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(EditAccountDialog.this, "Failed to save: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancel.addActionListener(e -> dispose());

            sigBtn.addActionListener(e -> {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(EditAccountDialog.this) == JFileChooser.APPROVE_OPTION) {
                    sig.setText(fc.getSelectedFile().getAbsolutePath());
                }
            });
            photoBtn.addActionListener(e -> {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(EditAccountDialog.this) == JFileChooser.APPROVE_OPTION) {
                    photo.setText(fc.getSelectedFile().getAbsolutePath());
                }
            });

            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(new JScrollPane(p), BorderLayout.CENTER);
            getContentPane().add(btns, BorderLayout.SOUTH);
        }

        public boolean isSaved() { return saved; }
        public Account getAccount() { return accountRef; }
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
