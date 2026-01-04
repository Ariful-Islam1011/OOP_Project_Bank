import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ATMCardExistingFrame extends JFrame {
    private JFrame parentFrame;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JComboBox<String> statusFilterCombo;

    public ATMCardExistingFrame(JFrame parent) {
        this.parentFrame = parent;
        setTitle("ATM Cards History");
        setSize(1000, 650);
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
        JLabel title = new JLabel("ATM Cards History & Search", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 255, 255));
        north.add(title, BorderLayout.CENTER);

        // Search & Filter Panel
        TranslucentPanel searchPanel = new TranslucentPanel(new Color(0, 0, 0, 180), 10, 10);
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        // Search Type
        JLabel searchTypeLabel = new JLabel("Search By:");
        searchTypeLabel.setForeground(Color.WHITE);
        searchTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        String[] searchTypes = {"Account Number", "Card Number", "Cardholder Name", "Mobile"};
        searchTypeCombo = new JComboBox<>(searchTypes);
        searchTypeCombo.setBackground(Color.WHITE);
        searchTypeCombo.setPreferredSize(new Dimension(150, 30));

        // Search Field
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchField = new JTextField(20);
        searchField.setBackground(new Color(50, 50, 50));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setPreferredSize(new Dimension(200, 30));

        // Status Filter
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        String[] statuses = {"All", "Active", "Expired", "Cancelled"};
        statusFilterCombo = new JComboBox<>(statuses);
        statusFilterCombo.setBackground(Color.WHITE);
        statusFilterCombo.setPreferredSize(new Dimension(120, 30));

        // Search Button
        JButton searchBtn = createRoundedButton("Search", new Color(30, 144, 255), 130, 35);

        // Show All Button
        JButton showAllBtn = createRoundedButton("Show All", new Color(50, 205, 50), 130, 35);

        searchPanel.add(searchTypeLabel);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(statusLabel);
        searchPanel.add(statusFilterCombo);
        searchPanel.add(searchBtn);
        searchPanel.add(showAllBtn);

        // Active Status Management Button
        JButton manageStatusBtn = createRoundedButton("Active Status", new Color(255, 165, 0), 160, 35);
        manageStatusBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchPanel.add(manageStatusBtn);

        // Table
        String[] columns = {"Card Number", "Account Number", "Cardholder", "Mobile", "Issue Date", "Expiry Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable cardsTable = new JTable(tableModel);
        cardsTable.setForeground(Color.BLACK);
        cardsTable.setBackground(Color.WHITE);
        cardsTable.setRowHeight(28);
        cardsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        cardsTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        cardsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        JScrollPane sp = new JScrollPane(cardsTable);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);

        // Back Button
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        south.setOpaque(false);
        JButton backBtn = createRoundedButton("Back", new Color(205, 92, 0), 120, 45);
        south.add(backBtn);

        // Actions
        searchBtn.addActionListener(e -> performSearch());
        showAllBtn.addActionListener(e -> loadAllCards());
        manageStatusBtn.addActionListener(e -> showActiveStatusDialog());
        backBtn.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        });

        // Assemble
        add(north, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        // Initial load
        loadAllCards();
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        String searchType = (String) searchTypeCombo.getSelectedItem();
        String statusFilter = (String) statusFilterCombo.getSelectedItem();

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter search text", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        try {
            List<Map<String, Object>> cards = null;
            
            if ("Account Number".equals(searchType)) {
                cards = DBHelper.getATMCards(searchText);
            } else if ("Card Number".equals(searchType)) {
                cards = DBHelper.searchATMCards("cardNumber", searchText);
            } else if ("Cardholder Name".equals(searchType)) {
                cards = DBHelper.searchATMCards("name", searchText);
            } else if ("Mobile".equals(searchType)) {
                cards = DBHelper.searchATMCards("mobile", searchText);
            }

            if (cards != null) {
                for (Map<String, Object> card : cards) {
                    String status = (String) card.get("status");
                    if (!statusFilter.equals("All") && !status.equals(statusFilter)) continue;

                    Object[] row = {
                            card.get("cardNumber"),
                            card.get("accountNumber"),
                            card.get("cardholderName"),
                            card.get("mobile"),
                            card.get("issueDate"),
                            card.get("expiryDate"),
                            card.get("status")
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadAllCards() {
        String statusFilter = (String) statusFilterCombo.getSelectedItem();
        tableModel.setRowCount(0);
        try {
            List<Map<String, Object>> cards = DBHelper.getAllATMCards();
            for (Map<String, Object> card : cards) {
                String status = (String) card.get("status");
                if (!statusFilter.equals("All") && !status.equals(statusFilter)) continue;

                Object[] row = {
                        card.get("cardNumber"),
                        card.get("accountNumber"),
                        card.get("cardholderName"),
                        card.get("mobile"),
                        card.get("issueDate"),
                        card.get("expiryDate"),
                        card.get("status")
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void showActiveStatusDialog() {
        JDialog dialog = new JDialog(this, "Active Status Management", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        headerPanel.setBackground(new Color(30, 60, 90));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JTextField dialogSearchField = new JTextField(15);
        dialogSearchField.setPreferredSize(new Dimension(150, 30));

        JComboBox<String> filterCombo = new JComboBox<>(new String[]{"Account Number", "Card Number"});
        filterCombo.setPreferredSize(new Dimension(140, 30));

        JButton searchButton = createRoundedButton("🔍 Search", new Color(30, 144, 255), 150, 35);
        JButton showActiveBtn = createRoundedButton("📋 Show All Active", new Color(50, 205, 50), 200, 35);

        headerPanel.add(searchLabel);
        headerPanel.add(dialogSearchField);
        headerPanel.add(filterCombo);
        headerPanel.add(searchButton);
        headerPanel.add(showActiveBtn);

        // Table for active cards
        String[] columns = {"Card Number", "Account Number", "Cardholder", "Status", "Expiry Date"};
        DefaultTableModel dialogTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable dialogTable = new JTable(dialogTableModel);
        dialogTable.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(dialogTable);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton activateBtn = createRoundedButton("✓ Activate", new Color(34, 139, 34), 150, 45);
        JButton deactivateBtn = createRoundedButton("✗ Deactivate", new Color(220, 20, 60), 150, 45);
        JButton closeBtn = createRoundedButton("Close", new Color(100, 100, 100), 130, 45);

        buttonPanel.add(activateBtn);
        buttonPanel.add(deactivateBtn);
        buttonPanel.add(closeBtn);

        // Load all active cards initially
        Runnable loadActiveCards = () -> {
            dialogTableModel.setRowCount(0);
            try {
                List<Map<String, Object>> cards = DBHelper.getAllATMCards();
                for (Map<String, Object> card : cards) {
                    String status = (String) card.get("status");
                    if ("Active".equals(status)) {
                        Object[] row = {
                            card.get("cardNumber"),
                            card.get("accountNumber"),
                            card.get("cardholderName"),
                            card.get("status"),
                            card.get("expiryDate")
                        };
                        dialogTableModel.addRow(row);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error loading cards: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        };

        // Search functionality
        searchButton.addActionListener(e -> {
            String searchText = dialogSearchField.getText().trim();
            String searchType = (String) filterCombo.getSelectedItem();
            
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter search text", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dialogTableModel.setRowCount(0);
            try {
                List<Map<String, Object>> cards = null;
                if ("Account Number".equals(searchType)) {
                    cards = DBHelper.getATMCards(searchText);
                } else if ("Card Number".equals(searchType)) {
                    cards = DBHelper.searchATMCards("cardNumber", searchText);
                }

                if (cards != null) {
                    for (Map<String, Object> card : cards) {
                        Object[] row = {
                            card.get("cardNumber"),
                            card.get("accountNumber"),
                            card.get("cardholderName"),
                            card.get("status"),
                            card.get("expiryDate")
                        };
                        dialogTableModel.addRow(row);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error searching: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        showActiveBtn.addActionListener(e -> loadActiveCards.run());

        // Activate button action
        activateBtn.addActionListener(e -> {
            int selectedRow = dialogTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select a card", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cardNumber = (String) dialogTableModel.getValueAt(selectedRow, 0);
            String currentStatus = (String) dialogTableModel.getValueAt(selectedRow, 3);

            if ("Active".equals(currentStatus)) {
                JOptionPane.showMessageDialog(dialog, "Card is already active", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(dialog, 
                "Are you sure you want to activate card: " + cardNumber + "?", 
                "Confirm Activation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = DBHelper.updateATMCardStatus(cardNumber, "Active");
                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Card activated successfully!", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        dialogTableModel.setValueAt("Active", selectedRow, 3);
                        loadAllCards(); // Refresh main table
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to activate card", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Deactivate button action
        deactivateBtn.addActionListener(e -> {
            int selectedRow = dialogTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select a card", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cardNumber = (String) dialogTableModel.getValueAt(selectedRow, 0);
            String currentStatus = (String) dialogTableModel.getValueAt(selectedRow, 3);

            if ("Cancelled".equals(currentStatus)) {
                JOptionPane.showMessageDialog(dialog, "Card is already deactivated", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(dialog, 
                "Are you sure you want to deactivate card: " + cardNumber + "?", 
                "Confirm Deactivation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = DBHelper.updateATMCardStatus(cardNumber, "Cancelled");
                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Card deactivated successfully!", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        dialogTableModel.setValueAt("Cancelled", selectedRow, 3);
                        loadAllCards(); // Refresh main table
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to deactivate card", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        loadActiveCards.run();
        dialog.setVisible(true);
    }

    private JButton createRoundedButton(String text, Color bgColor, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Determine fill color based on state
                Color fill = bgColor;
                if (getModel().isPressed()) fill = bgColor.darker();
                else if (getModel().isRollover()) fill = bgColor.brighter();
                
                // Draw rounded rectangle background
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, w, h, 15, 15);
                
                // Draw border
                g2.setColor(Color.WHITE);
                g2.setStroke(new java.awt.BasicStroke(2f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 15, 15);
                
                g2.dispose();
                
                // Draw text
                setContentAreaFilled(false);
                setOpaque(false);
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Border is handled in paintComponent
            }
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
