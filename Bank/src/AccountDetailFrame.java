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

        // create an inner content panel with a semi-opaque dark background
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(true);
        content.setBackground(new Color(0,0,0,180));
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

        // show small logo if available (treat as profile thumbnail)
        ImageIcon logo = UIUtils.loadScaledIcon(System.getProperty("user.dir") + "/Icon/default_logo.png", 120, 60);
        if (logo != null) {
            gc.gridx = 0; gc.gridy++; content.add(new JLabel("Profile:"), gc); gc.gridx = 1; content.add(new JLabel(logo), gc);
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

        tabs.addTab("Profile", new JScrollPane(pd));

        // Transactions panel
        // prefer requested `tsc.jpg` for transactions background; fall back to Doyelcottor
        java.awt.Image txBg = UIUtils.loadImageFromCandidates(System.getProperty("user.dir") + "/Icon/tsc.jpg", System.getProperty("user.dir") + "/Icon/Doyelcottor.jpg");
        JPanel tx = txBg != null ? new BackgroundPanel(txBg) : new JPanel(new BorderLayout());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10)); actions.setOpaque(false);
        JButton dep = new JButton("Deposit"); dep.setForeground(Color.WHITE); dep.setOpaque(true); dep.setBackground(new Color(0,0,0,170)); dep.setContentAreaFilled(true); dep.setBorderPainted(false);
        JButton wit = new JButton("Withdraw"); wit.setForeground(Color.WHITE); wit.setOpaque(true); wit.setBackground(new Color(0,0,0,170)); wit.setContentAreaFilled(true); wit.setBorderPainted(false);
        actions.add(dep); actions.add(wit);

        JTextArea statement = new JTextArea();
        statement.setEditable(false);
        statement.setForeground(Color.WHITE);
        statement.setOpaque(false);
        statement.setBackground(new Color(0,0,0,0));
        JScrollPane statementSp = new JScrollPane(statement);
        statementSp.setOpaque(false);
        statementSp.getViewport().setOpaque(false);
        tx.add(actions, BorderLayout.NORTH);
        tx.add(statementSp, BorderLayout.CENTER);

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
        JButton balBtn = new JButton("Balance"); balBtn.setForeground(Color.WHITE); balBtn.setOpaque(true); balBtn.setBackground(new Color(0,0,0,170)); balBtn.setContentAreaFilled(true); balBtn.setBorderPainted(false);
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
        bottom.setOpaque(false);
        JButton back = new JButton("Back");
        back.setForeground(Color.WHITE);
        back.setOpaque(true);
        back.setBackground(new Color(0,0,0,170));
        back.setContentAreaFilled(true);
        back.setBorderPainted(false);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        back.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
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
}
