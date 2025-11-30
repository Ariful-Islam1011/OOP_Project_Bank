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
        JPanel pd = new JPanel(new GridBagLayout());
        pd.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6);
        gc.anchor = GridBagConstraints.WEST;
        // large balance label at top of profile
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        JLabel bigBal = new JLabel("Balance: " + account.getBalance(), SwingConstants.CENTER);
        bigBal.setFont(bigBal.getFont().deriveFont(Font.BOLD, 20f));
        pd.add(bigBal, gc);
        gc.gridwidth = 1;
        gc.gridy++;
        gc.gridx = 0; pd.add(new JLabel("Account Number:"), gc); gc.gridx = 1; pd.add(new JLabel(account.getAccountNumber()), gc);
        gc.gridx = 0; gc.gridy++; pd.add(new JLabel("Name:"), gc); gc.gridx = 1; pd.add(new JLabel(account.getName()), gc);
        gc.gridx = 0; gc.gridy++; pd.add(new JLabel("Father:"), gc); gc.gridx = 1; pd.add(new JLabel(account.getFather()), gc);
        gc.gridx = 0; gc.gridy++; pd.add(new JLabel("Mother:"), gc); gc.gridx = 1; pd.add(new JLabel(account.getMother()), gc);
        gc.gridx = 0; gc.gridy++; pd.add(new JLabel("DOB:"), gc); gc.gridx = 1; pd.add(new JLabel(account.getDob()), gc);
        gc.gridx = 0; gc.gridy++; pd.add(new JLabel("Gender:"), gc); gc.gridx = 1; pd.add(new JLabel(account.getGender()), gc);
        gc.gridx = 0; gc.gridy++; pd.add(new JLabel("Mobile:"), gc); gc.gridx = 1; pd.add(new JLabel(account.getMobile()), gc);
        gc.gridx = 0; gc.gridy++; pd.add(new JLabel("Address:"), gc); gc.gridx = 1; pd.add(new JLabel(account.getAddress()), gc);
        gc.gridx = 0; gc.gridy++; pd.add(new JLabel("NID:"), gc); gc.gridx = 1; pd.add(new JLabel(account.getNid()), gc);
        gc.gridx = 0; gc.gridy++; pd.add(new JLabel("Account Type:"), gc); gc.gridx = 1; pd.add(new JLabel(account.getAccountType()), gc);
        gc.gridx = 0; gc.gridy++; pd.add(new JLabel("Balance:"), gc); gc.gridx = 1; JLabel balLabel = new JLabel(String.valueOf(account.getBalance())); balLabel.setFont(balLabel.getFont().deriveFont(Font.BOLD, 14f)); pd.add(balLabel, gc);

        // signature display (if exists)
        if (account.getSignaturePath() != null && !account.getSignaturePath().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(account.getSignaturePath());
                Image img = icon.getImage().getScaledInstance(200,80,Image.SCALE_SMOOTH);
                JLabel sig = new JLabel(new ImageIcon(img));
                gc.gridx = 0; gc.gridy++; pd.add(new JLabel("Signature:"), gc); gc.gridx = 1; pd.add(sig, gc);
            } catch (Exception ex) { gc.gridx = 0; gc.gridy++; pd.add(new JLabel("Signature:"), gc); gc.gridx = 1; pd.add(new JLabel("(failed to load)"), gc); }
        }

        // show small logo if available (treat as profile thumbnail)
        ImageIcon logo = UIUtils.loadScaledIcon("/Users/md.arifulislam/Downloads/Dhaka_University_logo.svg", 120, 60);
        if (logo != null) {
            gc.gridx = 0; gc.gridy++; pd.add(new JLabel("Profile:"), gc); gc.gridx = 1; pd.add(new JLabel(logo), gc);
        }

        tabs.addTab("Profile", new JScrollPane(pd));

        // Transactions panel
        JPanel tx = new JPanel(new BorderLayout());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        JButton dep = new JButton("Deposit");
        JButton wit = new JButton("Withdraw");
        actions.add(dep); actions.add(wit);

        JTextArea statement = new JTextArea(); statement.setEditable(false);
        tx.add(actions, BorderLayout.NORTH);
        tx.add(new JScrollPane(statement), BorderLayout.CENTER);

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

        // add Balance button into transactions actions
        JButton balBtn = new JButton("Balance");
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
        JButton back = new JButton("Back");
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
