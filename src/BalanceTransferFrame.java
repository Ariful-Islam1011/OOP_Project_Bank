import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class BalanceTransferFrame extends JFrame {
    private JFrame parentFrame;
    private Account account;
    private JTextField toAccountField;
    private JTextField amountField;
    private DefaultTableModel transferTableModel;
    private JTable transferTable;
    private JTextArea transferHistory;

    public BalanceTransferFrame(JFrame parent, Account acc) {
        this.parentFrame = parent;
        this.account = acc;
        setTitle(account != null ? "Balance Transfer - " + account.getAccountNumber() : "Balance Transfer");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        String projectDir = System.getProperty("user.dir");
        java.awt.Image bg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/pukur.jpg", projectDir + "/Icon/Hall.jpg");
        if (bg != null) {
            BackgroundPanel bgPanel = new BackgroundPanel(bg);
            setContentPane(bgPanel);
            
            // Add translucent overlay for consistency
            JPanel overlay = new JPanel(new BorderLayout(10, 10));
            overlay.setBackground(new Color(0, 0, 0, 100));
            overlay.setOpaque(false);
            bgPanel.setLayout(new BorderLayout());
            bgPanel.add(overlay, BorderLayout.CENTER);
            setLayout(new BorderLayout(10, 10));
        } else {
            setLayout(new BorderLayout(10, 10));
        }

        // Header
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.setPreferredSize(new Dimension(100, 80));
        ImageIcon logoIcon = UIUtils.loadScaledIcon(projectDir + "/Icon/DU_Logo.png", 48, 48);
        if (logoIcon != null) northPanel.add(new JLabel(logoIcon), BorderLayout.WEST);
        JLabel title = new JLabel("Balance Transfer", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setForeground(new Color(0, 255, 255));
        northPanel.add(title, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);

        // Center with tabs
        JTabbedPane tabs = new JTabbedPane();
        if (account != null) {
            tabs.addTab("New Transfer", createTransferPanel());
        }
        tabs.addTab("Transfer History", createHistoryPanel());
        add(tabs, BorderLayout.CENTER);

        // Footer
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        southPanel.setOpaque(false);
        JButton backBtn = UIUtils.createStyledButton("◄ Back", new Color(205, 92, 0), 130, 42);
        backBtn.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        });
        southPanel.add(backBtn);
        add(southPanel, BorderLayout.SOUTH);

        if (account != null) {
            loadTransferHistory("All");
        } else {
            loadAllTransfers("All");
        }
    }

    private JPanel createTransferPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        TranslucentPanel content = new TranslucentPanel(new Color(0, 0, 0, 180), 18, 18);
        content.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 15, 10, 15);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // From Account
        gc.gridx = 0;
        gc.gridy = row;
        JLabel fromLabel = new JLabel("From Account:");
        fromLabel.setForeground(Color.WHITE);
        fromLabel.setFont(fromLabel.getFont().deriveFont(Font.BOLD, 14f));
        content.add(fromLabel, gc);

        gc.gridx = 1;
        JLabel fromValue = new JLabel(account.getAccountNumber() + " (" + account.getName() + ")");
        fromValue.setForeground(Color.YELLOW);
        fromValue.setFont(fromValue.getFont().deriveFont(Font.BOLD, 14f));
        content.add(fromValue, gc);

        // Current Balance
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        JLabel balanceLabel = new JLabel("Current Balance:");
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(Font.BOLD, 14f));
        content.add(balanceLabel, gc);

        gc.gridx = 1;
        JLabel balanceValue = new JLabel("৳ " + String.format("%.2f", account.getBalance()));
        balanceValue.setForeground(new Color(0, 255, 0));
        balanceValue.setFont(balanceValue.getFont().deriveFont(Font.BOLD, 16f));
        content.add(balanceValue, gc);

        // Separator
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 100));
        content.add(sep, gc);

        gc.gridwidth = 1;

        // To Account
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        JLabel toLabel = new JLabel("To Account Number:");
        toLabel.setForeground(Color.WHITE);
        toLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        content.add(toLabel, gc);

        gc.gridx = 1;
        toAccountField = new JTextField(20);
        toAccountField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        toAccountField.setBackground(new Color(50, 50, 50));
        toAccountField.setForeground(Color.WHITE);
        toAccountField.setCaretColor(Color.WHITE);
        toAccountField.setPreferredSize(new Dimension(300, 35));
        content.add(toAccountField, gc);

        // Amount
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        JLabel amountLabel = new JLabel("Amount (৳):");
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        content.add(amountLabel, gc);

        gc.gridx = 1;
        amountField = new JTextField(20);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        amountField.setBackground(new Color(50, 50, 50));
        amountField.setForeground(Color.WHITE);
        amountField.setCaretColor(Color.WHITE);
        amountField.setPreferredSize(new Dimension(300, 35));
        content.add(amountField, gc);

        // Buttons Panel
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        gc.fill = GridBagConstraints.CENTER;
        gc.anchor = GridBagConstraints.CENTER;
        gc.ipady = 20;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);

        JButton transferBtn = UIUtils.createStyledButton("✓ CONFIRM TRANSFER", new Color(76, 175, 80), 220, 45);

        JButton clearBtn = UIUtils.createStyledButton("✗ CLEAR", new Color(128, 128, 128), 150, 45);

        buttonPanel.add(transferBtn);
        buttonPanel.add(clearBtn);
        content.add(buttonPanel, gc);

        transferBtn.addActionListener(e -> performTransfer());
        clearBtn.addActionListener(e -> {
            toAccountField.setText("");
            amountField.setText("");
        });

        // Add glue at the end to push content up
        row++;
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        gc.weighty = 1.0;
        content.add(Box.createVerticalGlue(), gc);

        JScrollPane sp = new JScrollPane(content);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search & Filter Panel
        TranslucentPanel filterPanel = new TranslucentPanel(new Color(0, 0, 0, 140), 10, 10);
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        // Search by Account
        JLabel searchLabel = new JLabel("Search Account:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField searchField = new JTextField(15);
        searchField.setBackground(new Color(50, 50, 50));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setPreferredSize(new Dimension(180, 30));

        // Filter by Type
        JLabel filterLabel = new JLabel("Filter Type:");
        filterLabel.setForeground(Color.WHITE);
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        String[] types = {"All", "Sent", "Received"};
        JComboBox<String> filterCombo = new JComboBox<>(types);
        filterCombo.setBackground(Color.WHITE);
        filterCombo.setPreferredSize(new Dimension(120, 30));

        // Search Button
        JButton searchBtn = UIUtils.createStyledButton("🔍 Search", new Color(33, 150, 243), 110, 35);

        // Show All Button
        JButton showAllBtn = UIUtils.createStyledButton("📋 Show All", new Color(76, 175, 80), 120, 35);

        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);
        filterPanel.add(searchBtn);
        filterPanel.add(showAllBtn);

        // Table
        String[] columns = {"Serial Number", "From", "To", "Amount (৳)", "Type", "Date & Time"};
        transferTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transferTable = new JTable(transferTableModel);
        transferTable.setForeground(Color.BLACK);
        transferTable.setBackground(Color.WHITE);
        transferTable.setRowHeight(28);
        // Improve header readability
        javax.swing.table.JTableHeader th = transferTable.getTableHeader();
        th.setFont(th.getFont().deriveFont(Font.BOLD, 13f));
        th.setForeground(Color.WHITE);
        th.setBackground(new Color(0,0,0,180));
        th.setOpaque(true);
        transferTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        transferTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        JScrollPane sp = new JScrollPane(transferTable);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);

        // Actions
        // Install highlighter to emphasize matching rows
        javax.swing.table.TableCellRenderer defaultRenderer = transferTable.getDefaultRenderer(Object.class);
        transferTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.BLACK);
                c.setBackground(Color.WHITE);
                String matchAcc = searchField.getText().trim();
                if (!matchAcc.isEmpty()) {
                    String from = String.valueOf(table.getValueAt(row, 1));
                    String to = String.valueOf(table.getValueAt(row, 2));
                    if (from.equalsIgnoreCase(matchAcc) || to.equalsIgnoreCase(matchAcc)) {
                        c.setBackground(new Color(255, 249, 196)); // soft highlight
                    }
                }
                if (isSelected) {
                    c.setBackground(new Color(200, 230, 255));
                }
                return c;
            }
        });

        searchBtn.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                loadTransferHistory((String) filterCombo.getSelectedItem());
            } else {
                loadTransferHistoryByAccount(searchText, (String) filterCombo.getSelectedItem());
                // Auto-select first matching row for clarity
                for (int r = 0; r < transferTableModel.getRowCount(); r++) {
                    String from = String.valueOf(transferTableModel.getValueAt(r, 1));
                    String to = String.valueOf(transferTableModel.getValueAt(r, 2));
                    if (from.equalsIgnoreCase(searchText) || to.equalsIgnoreCase(searchText)) {
                        transferTable.setRowSelectionInterval(r, r);
                        break;
                    }
                }
            }
        });
        
        showAllBtn.addActionListener(e -> {
            searchField.setText("");
            loadAllTransfers((String) filterCombo.getSelectedItem());
        });

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    private void performTransfer() {
        String toAccount = toAccountField.getText().trim();
        String amountStr = amountField.getText().trim();

        if (toAccount.isEmpty() || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (toAccount.equals(account.getAccountNumber())) {
            JOptionPane.showMessageDialog(this, "Cannot transfer to same account", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (amount > account.getBalance()) {
                JOptionPane.showMessageDialog(this, "Insufficient balance", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Account toAcc = DBHelper.getAccountByNumber(toAccount);
            if (toAcc == null) {
                JOptionPane.showMessageDialog(this, "Account not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String serialNumber = DBHelper.createTransfer(account.getAccountNumber(), toAccount, amount);
            account = DBHelper.getAccountByNumber(account.getAccountNumber());
            
            JOptionPane.showMessageDialog(this, 
                "Transfer successful!\n\n" +
                "Serial Number: " + serialNumber + "\n" +
                "Amount: ৳ " + String.format("%.2f", amount) + "\n" +
                "To Account: " + toAccount,
                "Success", JOptionPane.INFORMATION_MESSAGE);

            toAccountField.setText("");
            amountField.setText("");
            loadTransferHistory("All");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadTransferHistory(String filter) {
        transferTableModel.setRowCount(0);
        try {
            List<Map<String, Object>> transfers = DBHelper.getTransfers(account.getAccountNumber());
            for (Map<String, Object> transfer : transfers) {
                String type = (String) transfer.get("type");
                if (!filter.equals("All") && !type.equals(filter)) continue;

                Object[] row = {
                        transfer.get("serialNumber"),
                        transfer.get("fromAccount"),
                        transfer.get("toAccount"),
                        String.format("%.2f", transfer.get("amount")),
                        transfer.get("type"),
                        transfer.get("timestamp")
                };
                transferTableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading transfers: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadTransferHistoryByAccount(String accountNumber, String filter) {
        transferTableModel.setRowCount(0);
        try {
            List<Map<String, Object>> transfers = DBHelper.getTransfers(accountNumber);
            for (Map<String, Object> transfer : transfers) {
                String type = (String) transfer.get("type");
                if (!filter.equals("All") && !type.equals(filter)) continue;

                Object[] row = {
                        transfer.get("serialNumber"),
                        transfer.get("fromAccount"),
                        transfer.get("toAccount"),
                        String.format("%.2f", transfer.get("amount")),
                        transfer.get("type"),
                        transfer.get("timestamp")
                };
                transferTableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading transfers: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadAllTransfers(String filter) {
        transferTableModel.setRowCount(0);
        try {
            List<Map<String, Object>> transfers = DBHelper.getAllTransfers();
            for (Map<String, Object> transfer : transfers) {
                String type = (String) transfer.get("type");
                if (!filter.equals("All") && !type.equals(filter)) continue;

                Object[] row = {
                        transfer.get("serialNumber"),
                        transfer.get("fromAccount"),
                        transfer.get("toAccount"),
                        String.format("%.2f", transfer.get("amount")),
                        transfer.get("type"),
                        transfer.get("timestamp")
                };
                transferTableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading transfers: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
