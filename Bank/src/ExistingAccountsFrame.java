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
        // prefer requested `newbuilding.jpg`, fall back to Mosque or Hall
        java.awt.Image bg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/newbuilding.jpg", projectDir + "/Icon/Mosque.jpg", projectDir + "/Icon/Hall.jpg");
        JPanel p = bg != null ? new BackgroundPanel(bg) : new JPanel(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        top.setOpaque(false);
        // optionally show compact logo on the left
        ImageIcon logo = UIUtils.loadScaledIcon(projectDir + "/Icon/default_logo.png", 40, 40);
        if (logo != null) top.add(new JLabel(logo));
        JComboBox<String> types = new JComboBox<>(new String[]{"Savings","Current","Fixed"});
        JComboBox<String> searchBy = new JComboBox<>(new String[]{"Account Number","Name","Mobile"});
        JTextField search = new JTextField(16);
        // style search controls for dark background: make search field readable (dark text on translucent light bg)
        search.setForeground(Color.BLACK);
        search.setOpaque(true);
        search.setBackground(new Color(255,255,255,220));
        search.setCaretColor(Color.BLACK);
        JButton searchBtn = new JButton("OK");
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setOpaque(true);
        searchBtn.setBackground(new Color(0,0,0,170));
        searchBtn.setContentAreaFilled(true);
        searchBtn.setBorderPainted(false);
        types.setForeground(Color.WHITE);
        types.setOpaque(false);
        searchBy.setForeground(Color.WHITE);
        searchBy.setOpaque(false);
        top.add(new JLabel("Filter:"));
        top.add(types);
        // place search bar where Load used to be
        top.add(Box.createHorizontalStrut(8));
        top.add(search);
        top.add(Box.createHorizontalStrut(8));
        top.add(new JLabel("Search by:"));
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
        JButton open = new JButton("Open"); open.setForeground(Color.WHITE); open.setOpaque(true); open.setBackground(new Color(0,0,0,170)); open.setContentAreaFilled(true); open.setBorderPainted(false); open.setFocusPainted(false);
        JButton back = new JButton("Back"); back.setForeground(Color.WHITE); back.setOpaque(true); back.setBackground(new Color(0,0,0,170)); back.setContentAreaFilled(true); back.setBorderPainted(false); back.setFocusPainted(false);
        bottom.add(open); bottom.add(back);

        p.add(top, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

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
                model.clear();
                if ("Account Number".equals(by)) {
                    Account a = DBHelper.getAccountByNumber(q);
                    if (a != null) model.addElement(a.getAccountNumber() + " - " + a.getName() + " - " + a.getBalance());
                    else JOptionPane.showMessageDialog(this, "Account not found", "Not found", JOptionPane.INFORMATION_MESSAGE);
                } else if ("Name".equals(by)) {
                    java.util.List<Account> res = DBHelper.getAccountsByName(q);
                    if (res.isEmpty()) JOptionPane.showMessageDialog(this, "No accounts found for name", "Not found", JOptionPane.INFORMATION_MESSAGE);
                    for (Account a : res) model.addElement(a.getAccountNumber() + " - " + a.getName() + " - " + a.getBalance());
                } else if ("Mobile".equals(by)) {
                    java.util.List<Account> res2 = DBHelper.getAccountsByMobile(q);
                    if (res2.isEmpty()) JOptionPane.showMessageDialog(this, "No accounts found for mobile", "Not found", JOptionPane.INFORMATION_MESSAGE);
                    for (Account a : res2) model.addElement(a.getAccountNumber() + " - " + a.getName() + " - " + a.getBalance());
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
}
