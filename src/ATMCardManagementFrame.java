import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ATMCardManagementFrame extends JFrame {
    private JFrame parentFrame;
    private Account account;

    public ATMCardManagementFrame(JFrame parent, Account acc) {
        this.parentFrame = parent;
        this.account = acc;
        setTitle("ATM Card Management - " + account.getAccountNumber());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        String projectDir = System.getProperty("user.dir");
        java.awt.Image bg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/pukur.jpg", projectDir + "/Icon/Hall.jpg");
        if (bg != null) setContentPane(new BackgroundPanel(bg));

        JTabbedPane tabs = new JTabbedPane();

        // Issue Card Tab
        JPanel issuePanel = createIssueCardPanel();
        tabs.addTab("Issue New Card", issuePanel);

        // Existing Cards Tab
        JPanel existingPanel = createExistingCardsPanel();
        tabs.addTab("Existing Cards", existingPanel);

        // Search Cards Tab
        JPanel searchPanel = createSearchCardsPanel();
        tabs.addTab("Search Cards", searchPanel);

        add(tabs);
    }

    private JPanel createIssueCardPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());

        TranslucentPanel content = new TranslucentPanel(new Color(0, 0, 0, 120), 18, 18);
        content.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.anchor = GridBagConstraints.WEST;

        // Header
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        JLabel title = new JLabel("Issue New ATM Card");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setForeground(Color.WHITE);
        content.add(title, gc);

        gc.gridwidth = 1;
        gc.gridy++;

        // Account Info
        gc.gridx = 0;
        JLabel accLabel = new JLabel("Account Number:");
        accLabel.setForeground(Color.WHITE);
        content.add(accLabel, gc);
        gc.gridx = 1;
        JLabel accField = new JLabel(account.getAccountNumber());
        accField.setForeground(Color.WHITE);
        content.add(accField, gc);

        gc.gridx = 0;
        gc.gridy++;
        JLabel nameLabel = new JLabel("Cardholder Name:");
        nameLabel.setForeground(Color.WHITE);
        content.add(nameLabel, gc);
        gc.gridx = 1;
        JLabel nameField = new JLabel(account.getName());
        nameField.setForeground(Color.WHITE);
        content.add(nameField, gc);

        // Buttons
        gc.gridx = 0;
        gc.gridy += 3;
        gc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton issueBtn = new JButton("Issue Card");
        issueBtn.setBackground(new Color(34, 139, 34));
        issueBtn.setForeground(Color.WHITE);
        issueBtn.setOpaque(true);
        issueBtn.setFocusPainted(false);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(139, 69, 19));
        backBtn.setForeground(Color.WHITE);
        backBtn.setOpaque(true);
        backBtn.setFocusPainted(false);

        buttonPanel.add(issueBtn);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(backBtn);
        content.add(buttonPanel, gc);

        issueBtn.addActionListener(e -> issueNewCard());
        backBtn.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        });

        GridBagConstraints pcons = new GridBagConstraints();
        pcons.gridx = 0;
        pcons.gridy = 0;
        pcons.weightx = 1;
        pcons.weighty = 1;
        pcons.fill = GridBagConstraints.BOTH;
        panel.add(content, pcons);

        return panel;
    }

    private JPanel createExistingCardsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(10, 10));

        // Table
        String[] columns = {"Card Number", "Cardholder", "Issue Date", "Expiry Date", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable cardsTable = new JTable(tableModel);
        cardsTable.setForeground(Color.BLACK);
        cardsTable.setBackground(Color.WHITE);
        cardsTable.setRowHeight(25);
        JScrollPane sp = new JScrollPane(cardsTable);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);

        // Load existing cards
        try {
            List<Map<String, Object>> cards = DBHelper.getATMCards(account.getAccountNumber());
            for (Map<String, Object> card : cards) {
                Object[] row = {
                        card.get("cardNumber"),
                        card.get("cardholderName"),
                        card.get("issueDate"),
                        card.get("expiryDate"),
                        card.get("status")
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading cards: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSearchCardsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(10, 10));

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setOpaque(false);
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel searchLabel = new JLabel("Search by:");
        searchLabel.setForeground(Color.WHITE);
        String[] searchTypes = {"Name", "Mobile", "Account Number"};
        JComboBox<String> searchTypeCombo = new JComboBox<>(searchTypes);

        JLabel valueLabel = new JLabel("Value:");
        valueLabel.setForeground(Color.WHITE);
        JTextField searchField = new JTextField(15);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(0, 102, 204));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setOpaque(true);
        searchBtn.setFocusPainted(false);

        searchPanel.add(searchLabel);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(valueLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        // Table
        String[] columns = {"Card Number", "Account", "Cardholder", "Issue Date", "Expiry Date", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable resultsTable = new JTable(tableModel);
        resultsTable.setForeground(Color.BLACK);
        resultsTable.setBackground(Color.WHITE);
        resultsTable.setRowHeight(25);
        JScrollPane sp = new JScrollPane(resultsTable);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);

        searchBtn.addActionListener(e -> {
            String searchType = ((String) searchTypeCombo.getSelectedItem()).toLowerCase();
            String value = searchField.getText().trim();
            if (value.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter search value", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tableModel.setRowCount(0);
            try {
                List<Map<String, Object>> results = DBHelper.searchATMCards(searchType, value);
                for (Map<String, Object> card : results) {
                    Object[] row = {
                            card.get("cardNumber"),
                            card.get("accountNumber"),
                            card.get("cardholderName"),
                            card.get("issueDate"),
                            card.get("expiryDate"),
                            card.get("status")
                    };
                    tableModel.addRow(row);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error searching cards: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    private void issueNewCard() {
        try {
            String cardNumber = DBHelper.issueATMCard(account.getAccountNumber(), account.getName());
            JOptionPane.showMessageDialog(this, "Card issued successfully!\nCard Number: " + cardNumber, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error issuing card: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
