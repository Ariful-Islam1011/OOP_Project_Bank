import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ATMTransactionFrame extends JFrame implements ActionListener {
    private JButton withdrawBtn, balanceBtn, miniStatementBtn, pinChangeBtn, exitBtn;
    private String accountNumber, name, cardNumber, pin;
    
    public ATMTransactionFrame(String accountNumber, String name, String cardNumber, String pin) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.cardNumber = cardNumber;
        this.pin = pin;
        
        setTitle("ATM - " + name);
        setSize(1550, 830);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        String projectDir = System.getProperty("user.dir");
        
        // Try to load ATM background
        try {
            ImageIcon atmIcon = new ImageIcon(projectDir + "/Icon/atm2.png");
            if (atmIcon.getIconWidth() > 0) {
                Image atmImg = atmIcon.getImage().getScaledInstance(1550, 830, Image.SCALE_SMOOTH);
                JLabel atmLabel = new JLabel(new ImageIcon(atmImg));
                atmLabel.setBounds(0, 0, 1550, 830);
                atmLabel.setLayout(null); // allow absolute positioning of buttons
                add(atmLabel);
                
                setupButtons(atmLabel);
            } else {
                getContentPane().setBackground(new Color(30, 60, 90));
                setLayout(null);
                setupButtons(null);
            }
        } catch (Exception e) {
            getContentPane().setBackground(new Color(30, 60, 90));
            setLayout(null);
            setupButtons(null);
        }
    }
    
    private void setupButtons(JLabel parentLabel) {
        JLabel titleLabel = new JLabel("Please Select Your Transaction");
        titleLabel.setBounds(430, 180, 700, 35);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("System", Font.BOLD, 28));

        withdrawBtn = new JButton("CASH WITHDRAWAL");
        withdrawBtn.setForeground(Color.WHITE);
        withdrawBtn.setBackground(new Color(65, 125, 128));
        withdrawBtn.setOpaque(true);
        withdrawBtn.setContentAreaFilled(true);
        withdrawBtn.setBorderPainted(false);
        withdrawBtn.setBounds(410, 274, 150, 35);
        withdrawBtn.addActionListener(this);

        balanceBtn = new JButton("BALANCE ENQUIRY");
        balanceBtn.setForeground(Color.WHITE);
        balanceBtn.setBackground(new Color(65, 125, 128));
        balanceBtn.setOpaque(true);
        balanceBtn.setContentAreaFilled(true);
        balanceBtn.setBorderPainted(false);
        balanceBtn.setBounds(700, 274, 150, 35);
        balanceBtn.addActionListener(this);

        miniStatementBtn = new JButton("MINI STATEMENT");
        miniStatementBtn.setForeground(Color.WHITE);
        miniStatementBtn.setBackground(new Color(65, 125, 128));
        miniStatementBtn.setOpaque(true);
        miniStatementBtn.setContentAreaFilled(true);
        miniStatementBtn.setBorderPainted(false);
        miniStatementBtn.setBounds(410, 318, 150, 35);
        miniStatementBtn.addActionListener(this);

        pinChangeBtn = new JButton("PIN CHANGE");
        pinChangeBtn.setForeground(Color.WHITE);
        pinChangeBtn.setBackground(new Color(65, 125, 128));
        pinChangeBtn.setOpaque(true);
        pinChangeBtn.setContentAreaFilled(true);
        pinChangeBtn.setBorderPainted(false);
        pinChangeBtn.setBounds(700, 318, 150, 35);
        pinChangeBtn.addActionListener(this);

        exitBtn = new JButton("EXIT");
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(new Color(65, 125, 128));
        exitBtn.setOpaque(true);
        exitBtn.setContentAreaFilled(true);
        exitBtn.setBorderPainted(false);
        exitBtn.setBounds(700, 362, 150, 35);
        exitBtn.addActionListener(this);

        if (parentLabel != null) {
            parentLabel.add(titleLabel);
            parentLabel.add(withdrawBtn);
            parentLabel.add(balanceBtn);
            parentLabel.add(miniStatementBtn);
            parentLabel.add(pinChangeBtn);
            parentLabel.add(exitBtn);
        } else {
            add(titleLabel);
            add(withdrawBtn);
            add(balanceBtn);
            add(miniStatementBtn);
            add(pinChangeBtn);
            add(exitBtn);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == withdrawBtn) {
            handleWithdrawal();
        } else if (e.getSource() == balanceBtn) {
            handleBalanceEnquiry();
        } else if (e.getSource() == miniStatementBtn) {
            handleMiniStatement();
        } else if (e.getSource() == pinChangeBtn) {
            handlePinChange();
        } else if (e.getSource() == exitBtn) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }

    private void handleWithdrawal() {
        String amount = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (amount != null && !amount.trim().isEmpty()) {
            try {
                double withdrawAmount = Double.parseDouble(amount);
                if (withdrawAmount <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = DBHelper.changeBalance(accountNumber, -withdrawAmount, "Withdrawal");
                if (success) {
                    JOptionPane.showMessageDialog(this, "Withdrawal Successful!\nAmount: " + withdrawAmount, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient balance or invalid account!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleBalanceEnquiry() {
        try {
            Account account = DBHelper.getAccountByNumber(accountNumber);
            if (account != null) {
                String message = "Account Number: " + accountNumber + "\n" +
                               "Name: " + account.getName() + "\n" +
                               "Balance: BDT " + String.format("%.2f", account.getBalance());
                JOptionPane.showMessageDialog(this, message, "Balance Enquiry", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Account not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleMiniStatement() {
        try {
            java.util.List<TransactionRecord> transactions = DBHelper.getTransactions(accountNumber, 10);
            
            if (transactions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No transactions found!", "Mini Statement", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder statement = new StringBuilder();
            statement.append("Last 10 Transactions\n");
            statement.append("Account: ").append(accountNumber).append("\n\n");
            statement.append(String.format("%-15s %-12s %-12s %-20s\n", "Type", "Amount", "Balance", "Date"));
            statement.append("----------------------------------------------------------------\n");

            for (TransactionRecord tr : transactions) {
                statement.append(String.format("%-15s %-12.2f %-12.2f %-20s\n", 
                    tr.getType(), tr.getAmount(), tr.getBalance(), tr.getTimestamp()));
            }

            JTextArea textArea = new JTextArea(statement.toString());
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            JOptionPane.showMessageDialog(this, scrollPane, "Mini Statement", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handlePinChange() {
        JPasswordField oldPinField = new JPasswordField(10);
        JPasswordField newPinField = new JPasswordField(10);
        JPasswordField confirmPinField = new JPasswordField(10);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Old PIN:"));
        panel.add(oldPinField);
        panel.add(new JLabel("New PIN:"));
        panel.add(newPinField);
        panel.add(new JLabel("Confirm PIN:"));
        panel.add(confirmPinField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Change PIN", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String oldPin = new String(oldPinField.getPassword());
            String newPin = new String(newPinField.getPassword());
            String confirmPin = new String(confirmPinField.getPassword());

            if (!oldPin.equals(this.pin)) {
                JOptionPane.showMessageDialog(this, "Incorrect old PIN!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newPin.isEmpty() || newPin.length() != 4) {
                JOptionPane.showMessageDialog(this, "PIN must be 4 digits!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPin.equals(confirmPin)) {
                JOptionPane.showMessageDialog(this, "PINs do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = DBHelper.getConnection();
                String updateQuery = "UPDATE accounts SET atm_pin = ? WHERE account_number = ?";
                PreparedStatement ps = conn.prepareStatement(updateQuery);
                ps.setString(1, newPin);
                ps.setString(2, accountNumber);
                int updated = ps.executeUpdate();
                ps.close();
                conn.close();

                if (updated > 0) {
                    this.pin = newPin;
                    JOptionPane.showMessageDialog(this, "PIN changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "PIN change failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
