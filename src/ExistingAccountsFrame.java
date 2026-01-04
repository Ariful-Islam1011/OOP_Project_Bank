import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ExistingAccountsFrame extends JFrame {
    private JFrame parent;

    public ExistingAccountsFrame(JFrame parent) {
        this.parent = parent;
        setTitle("Existing Accounts");
        setSize(600,400);
        setLocationRelativeTo(null);

        String projectDir = System.getProperty("user.dir");
        // prefer `DuBus.jpg` for search background, fall back to previous set
        java.awt.Image bg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/DuBus.jpg", projectDir + "/Icon/nature.jpg", projectDir + "/Icon/new_building.jpg", projectDir + "/Icon/newbuilding.jpg", projectDir + "/Icon/Mosque.jpg", projectDir + "/Icon/Hall.jpg");
        JPanel p = bg != null ? new BackgroundPanel(bg) : new JPanel(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        top.setOpaque(false);
        // optionally show compact logo on the left
        ImageIcon logo = UIUtils.loadScaledIcon(projectDir + "/Icon/DU_Logo.png", 40, 40);
        if (logo != null) top.add(new JLabel(logo));
        JComboBox<String> types = new JComboBox<>(new String[]{"All Types","Savings","Current","Fixed"});
        JComboBox<String> searchBy = new JComboBox<>(new String[]{"Account Number","Name","Mobile","NID"});
        // High-contrast renderer for combos: selected = blue bg, white text; unselected = black bg, white text
        DefaultListCellRenderer comboRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setOpaque(true);
                if (isSelected) {
                    lbl.setBackground(new Color(33, 150, 243));
                    lbl.setForeground(Color.WHITE);
                } else {
                    lbl.setBackground(Color.BLACK);
                    lbl.setForeground(Color.WHITE);
                }
                return lbl;
            }
        };
        types.setRenderer(comboRenderer);
        searchBy.setRenderer(comboRenderer);
        // Ensure combo boxes are consistently readable
        types.setForeground(Color.WHITE);
        types.setBackground(Color.BLACK);
        types.setOpaque(true);
        ((JLabel)types.getRenderer()).setOpaque(true);
        searchBy.setForeground(Color.WHITE);
        searchBy.setBackground(Color.BLACK);
        searchBy.setOpaque(true);
        ((JLabel)searchBy.getRenderer()).setOpaque(true);
        // Force UI to show selection
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", new Color(33, 150, 243));
        JTextField search = new JTextField(16);
        // style search controls per request: black background and white text, OK button white text
        search.setForeground(Color.WHITE);
        search.setOpaque(true);
        search.setBackground(Color.BLACK);
        search.setCaretColor(Color.WHITE);
        JButton searchBtn = UIUtils.createStyledButton("🔍 Search", new Color(33, 150, 243), 110, 32);
        // ensure labels are white
        JLabel lblAccountType = new JLabel("Account Type:"); lblAccountType.setForeground(Color.WHITE); lblAccountType.setFont(lblAccountType.getFont().deriveFont(Font.BOLD, 13f));
        top.add(lblAccountType);
        top.add(types);
        top.add(Box.createHorizontalStrut(15));
        top.add(search);
        top.add(Box.createHorizontalStrut(8));
        JLabel lblSearchBy = new JLabel("Search by:"); lblSearchBy.setForeground(Color.WHITE); lblSearchBy.setFont(lblSearchBy.getFont().deriveFont(Font.BOLD, 13f));
        top.add(lblSearchBy);
        top.add(searchBy);
        top.add(searchBtn);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        // Custom renderer to ensure contrast on various backgrounds
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setOpaque(true);
                if (isSelected) {
                    lbl.setBackground(new Color(255,255,255,220));
                    lbl.setForeground(Color.BLACK);
                } else {
                    lbl.setBackground(new Color(0,0,0,140));
                    lbl.setForeground(Color.WHITE);
                }
                return lbl;
            }
        });
        list.setBackground(new Color(0,0,0,80));
        JScrollPane sp = new JScrollPane(list);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setOpaque(false);
        JButton open = UIUtils.createStyledButton("✓ Open Account", new Color(76, 175, 80), 150, 40);
        JButton back = UIUtils.createStyledButton("◄ Back", new Color(244, 67, 54), 120, 40);
        bottom.add(open); bottom.add(back);

        // place main content into a light translucent overlay for readability
        TranslucentPanel wrapper = new TranslucentPanel(new Color(0,0,0,110), 16, 16);
        wrapper.setLayout(new BorderLayout());
        top.setOpaque(false);
        wrapper.add(top, BorderLayout.NORTH);
        wrapper.add(sp, BorderLayout.CENTER);
        wrapper.add(bottom, BorderLayout.SOUTH);
        p.add(wrapper, BorderLayout.CENTER);

        // populate list initially with all accounts so Open works without a prior search
        try {
            java.util.List<Account> all = DBHelper.getAllAccounts();
            for (Account a : all) {
                model.addElement(a.getAccountNumber() + " - " + a.getName() + " - " + a.getBalance());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        add(p);

        // Search action used by OK button and Enter key in the search field
        java.awt.event.ActionListener doSearch = evt -> {
            String q = search.getText().trim();
            if (q.isEmpty()) return;
            try {
                String by = (String)searchBy.getSelectedItem();
                String typeFilter = (String)types.getSelectedItem();
                model.clear();
                java.util.List<Account> results = new java.util.ArrayList<>();
                
                if ("Account Number".equals(by)) {
                    Account a = DBHelper.getAccountByNumber(q);
                    if (a != null) results.add(a);
                } else if ("Name".equals(by)) {
                    results = DBHelper.getAccountsByName(q);
                } else if ("Mobile".equals(by)) {
                    results = DBHelper.getAccountsByMobile(q);
                } else if ("NID".equals(by)) {
                    results = DBHelper.getAccountsByNID(q);
                }
                
                // Apply account type filter
                if (!"All Types".equals(typeFilter)) {
                    results.removeIf(a -> !a.getAccountType().equals(typeFilter));
                }
                
                if (results.isEmpty()) {
                    showMessage("No accounts found", "Not found", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    for (Account a : results) {
                        model.addElement(a.getAccountNumber() + " - " + a.getName() + " - " + a.getBalance());
                    }
                }
                // if results present, select the first row so Open can work immediately
                if (model.getSize() > 0) {
                    list.setSelectedIndex(0);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        };

        search.addActionListener(doSearch);
        searchBtn.addActionListener(doSearch);

        open.addActionListener(e -> {
            String sel = list.getSelectedValue();
            // if nothing selected but there are results, open first result as a convenience
            if (sel == null) {
                if (model.getSize() > 0) {
                    sel = model.getElementAt(0);
                    list.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select an account from the list first.", "No selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            String acc = sel.split(" - ")[0];
            try {
                Account a = DBHelper.getAccountByNumber(acc);
                if (a == null) {
                    JOptionPane.showMessageDialog(this, "Account not found: " + acc, "Not found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // create and show the detail frame on the EDT and only hide this frame after successful show
                final Account fa = a;
                SwingUtilities.invokeLater(() -> {
                    try {
                        AccountDetailFrame f = new AccountDetailFrame(ExistingAccountsFrame.this, fa);
                        f.setVisible(true);
                        ExistingAccountsFrame.this.setVisible(false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(ExistingAccountsFrame.this, "Failed to open account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error while loading account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // allow double-clicking an entry to open it
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int idx = list.locationToIndex(evt.getPoint());
                    if (idx >= 0) {
                        String sel = list.getModel().getElementAt(idx);
                        if (sel != null) {
                            String acc = sel.split(" - ")[0];
                            try {
                                Account a = DBHelper.getAccountByNumber(acc);
                                if (a != null) new AccountDetailFrame(ExistingAccountsFrame.this, a).setVisible(true);
                                setVisible(false);
                            } catch (Exception ex) { ex.printStackTrace(); }
                        }
                    }
                }
            }
        });

        back.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });
    }

    // show a message dialog with black background and white text for better contrast on this UI
    private void showMessage(String message, String title, int messageType) {
        // save previous UI defaults
        Object prevPanelBg = UIManager.get("Panel.background");
        Object prevOptionBg = UIManager.get("OptionPane.background");
        Object prevMsgFg = UIManager.get("OptionPane.messageForeground");
        try {
            UIManager.put("Panel.background", new javax.swing.plaf.ColorUIResource(Color.BLACK));
            UIManager.put("OptionPane.background", new javax.swing.plaf.ColorUIResource(Color.BLACK));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            JOptionPane.showMessageDialog(this, message, title, messageType);
        } finally {
            UIManager.put("Panel.background", prevPanelBg);
            UIManager.put("OptionPane.background", prevOptionBg);
            UIManager.put("OptionPane.messageForeground", prevMsgFg);
        }
    }
}
