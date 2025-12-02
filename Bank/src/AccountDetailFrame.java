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

        // Profile tab (personal details)
        String projectDir = System.getProperty("user.dir");
        java.awt.Image profImg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/chemistry.jpg", projectDir + "/Icon/Science_Library.jpg");
        JPanel pd;
        if (profImg != null) {
            pd = new BackgroundPanel(profImg);
            // BackgroundPanel default layout is BorderLayout; we need GridBagLayout for profile fields
            pd.setLayout(new GridBagLayout());
            pd.setOpaque(false);
        } else {
            pd = new JPanel(new GridBagLayout());
            pd.setBackground(Color.BLACK);
            pd.setOpaque(true);
        }
        pd.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // create an inner content panel — use a translucent rounded overlay so background shows through
        TranslucentPanel content = new TranslucentPanel(new Color(0,0,0,120), 18, 18);
        content.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6);
        gc.anchor = GridBagConstraints.WEST;
        // large balance label at top of profile
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        JLabel bigBal = new JLabel("Balance: " + account.getBalance(), SwingConstants.CENTER);
        bigBal.setForeground(Color.WHITE);
        bigBal.setFont(bigBal.getFont().deriveFont(Font.BOLD, 20f));
        content.add(bigBal, gc);
        gc.gridwidth = 1;
        gc.gridy++;
        gc.gridx = 0; content.add(new JLabel("Account Number:"), gc); gc.gridx = 1; content.add(new JLabel(account.getAccountNumber()), gc);
        gc.gridx = 0; gc.gridy++; content.add(new JLabel("Name:"), gc); gc.gridx = 1; content.add(new JLabel(account.getName()), gc);
        gc.gridx = 0; gc.gridy++; content.add(new JLabel("Father:"), gc); gc.gridx = 1; content.add(new JLabel(account.getFather()), gc);
        gc.gridx = 0; gc.gridy++; content.add(new JLabel("Mother:"), gc); gc.gridx = 1; content.add(new JLabel(account.getMother()), gc);
        gc.gridx = 0; gc.gridy++; content.add(new JLabel("DOB:"), gc); gc.gridx = 1; content.add(new JLabel(account.getDob()), gc);
        gc.gridx = 0; gc.gridy++; content.add(new JLabel("Gender:"), gc); gc.gridx = 1; content.add(new JLabel(account.getGender()), gc);
        gc.gridx = 0; gc.gridy++; content.add(new JLabel("Mobile:"), gc); gc.gridx = 1; content.add(new JLabel(account.getMobile()), gc);
        gc.gridx = 0; gc.gridy++; content.add(new JLabel("Address:"), gc); gc.gridx = 1; content.add(new JLabel(account.getAddress()), gc);
        gc.gridx = 0; gc.gridy++; content.add(new JLabel("NID:"), gc); gc.gridx = 1; content.add(new JLabel(account.getNid()), gc);
        gc.gridx = 0; gc.gridy++; content.add(new JLabel("Account Type:"), gc); gc.gridx = 1; content.add(new JLabel(account.getAccountType()), gc);
        gc.gridx = 0; gc.gridy++; content.add(new JLabel("Balance:"), gc); gc.gridx = 1; JLabel balLabel = new JLabel(String.valueOf(account.getBalance())); balLabel.setFont(balLabel.getFont().deriveFont(Font.BOLD, 14f)); content.add(balLabel, gc);

        // signature display (if exists)
        if (account.getSignaturePath() != null && !account.getSignaturePath().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(account.getSignaturePath());
                Image img = icon.getImage().getScaledInstance(200,80,Image.SCALE_SMOOTH);
                JLabel sig = new JLabel(new ImageIcon(img));
                gc.gridx = 0; gc.gridy++; content.add(new JLabel("Signature:"), gc); gc.gridx = 1; content.add(sig, gc);
            } catch (Exception ex) { gc.gridx = 0; gc.gridy++; content.add(new JLabel("Signature:"), gc); gc.gridx = 1; content.add(new JLabel("(failed to load)"), gc); }
        }

        // show profile photo if provided
        if (account.getProfileImagePath() != null && !account.getProfileImagePath().isEmpty()) {
            try {
                ImageIcon photoIcon = new ImageIcon(account.getProfileImagePath());
                Image pimg = photoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                gc.gridx = 0; gc.gridy++; content.add(new JLabel("Photo:"), gc); gc.gridx = 1; content.add(new JLabel(new ImageIcon(pimg)), gc);
            } catch (Exception ex) { /* fall back to no photo */ }
        }

        // ensure all labels in content are white for readability
        SwingUtilities.invokeLater(() -> {
            for (Component comp : content.getComponents()) {
                if (comp instanceof JLabel) ((JLabel)comp).setForeground(Color.WHITE);
            }
        });

        // add the content panel into the profile container
        GridBagConstraints pcons = new GridBagConstraints();
        pcons.gridx = 0; pcons.gridy = 0; pcons.weightx = 1; pcons.weighty = 1; pcons.fill = GridBagConstraints.BOTH;
        pd.add(content, pcons);

        JScrollPane profileSp = new JScrollPane(pd);
        // keep the scroll viewport transparent so the background shows, but the content panel itself remains semi-opaque
        profileSp.setOpaque(false);
        profileSp.getViewport().setOpaque(false);
        tabs.addTab("Profile", profileSp);

        // Transactions panel
        // prefer requested `Hall.jpg` for transactions background; fall back to `tsc.jpg` then `Doyelcottor.jpg`
        java.awt.Image txBg = UIUtils.loadImageFromCandidates(System.getProperty("user.dir") + "/Icon/Hall.jpg", System.getProperty("user.dir") + "/Icon/tsc.jpg", System.getProperty("user.dir") + "/Icon/Doyelcottor.jpg");
        JPanel tx = txBg != null ? new BackgroundPanel(txBg) : new JPanel(new BorderLayout());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10)); actions.setOpaque(false);
        Color actionBtnBg = new Color(0,0,0,120);
        JButton dep = new JButton("Deposit"); dep.setForeground(Color.WHITE); dep.setOpaque(true); dep.setBackground(actionBtnBg); dep.setContentAreaFilled(true); dep.setBorderPainted(false);
        JButton wit = new JButton("Withdraw"); wit.setForeground(Color.WHITE); wit.setOpaque(true); wit.setBackground(actionBtnBg); wit.setContentAreaFilled(true); wit.setBorderPainted(false);
        actions.add(dep); actions.add(wit);

        JTextArea statement = new JTextArea();
        statement.setEditable(false);
        statement.setForeground(Color.WHITE);
        statement.setOpaque(false);
        statement.setBackground(new Color(0,0,0,0));
        JScrollPane statementSp = new JScrollPane(statement);
        statementSp.setOpaque(false);
        statementSp.getViewport().setOpaque(false);
        // Put actions and statement into a semi-opaque content panel so they are readable on top of background
        TranslucentPanel txContent = new TranslucentPanel(new Color(0,0,0,120), 18, 18);
        txContent.setLayout(new BorderLayout());
        txContent.add(actions, BorderLayout.NORTH);
        txContent.add(statementSp, BorderLayout.CENTER);
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
                    refreshStatement(statement);
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
                    refreshStatement(statement);
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient funds", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        // add Balance button into transactions actions (styled to merge with background)
        JButton balBtn = new JButton("Balance"); balBtn.setForeground(Color.WHITE); balBtn.setOpaque(true); balBtn.setBackground(actionBtnBg); balBtn.setContentAreaFilled(true); balBtn.setBorderPainted(false);
        balBtn.setFocusPainted(false);
        actions.add(balBtn);
        tabs.addTab("Transactions", tx);

        balBtn.addActionListener(e -> {
            try {
                account = DBHelper.getAccountByNumber(account.getAccountNumber());
                JOptionPane.showMessageDialog(this, "Current balance: " + account.getBalance(), "Balance", JOptionPane.INFORMATION_MESSAGE);
                balLabel.setText(String.valueOf(account.getBalance()));
                bigBal.setText("Balance: " + account.getBalance());
                refreshStatement(statement);
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        add(tabs, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(true);
        bottom.setBackground(new Color(0,0,0,160));
        JButton editBtn = new JButton("Edit");
        editBtn.setForeground(Color.WHITE);
        editBtn.setOpaque(true);
        // make Edit button color match the Back button color (semi-opaque dark)
        Color backBtnBg = new Color(0,0,0,120);
        editBtn.setBackground(backBtnBg);
        editBtn.setBorderPainted(false);
        editBtn.setPreferredSize(new Dimension(110,36));
        editBtn.setFont(editBtn.getFont().deriveFont(Font.BOLD, 13f));
        JButton back = new JButton("Back");
        back.setForeground(Color.WHITE);
        back.setOpaque(true);
        back.setBackground(new Color(0,0,0,120));
        back.setContentAreaFilled(true);
        back.setBorderPainted(false);
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
        refreshStatement(statement);
    }

    private void refreshStatement(JTextArea statement) {
        try {
            java.util.List<TransactionRecord> recs = DBHelper.getTransactions(account.getAccountNumber(), 20);
            StringBuilder sb = new StringBuilder();
            for (TransactionRecord r : recs) {
                sb.append(r.getTimestamp()).append(" | ").append(r.getType()).append(" | ").append(r.getAmount()).append(" | bal:").append(r.getBalance()).append('\n');
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
}
