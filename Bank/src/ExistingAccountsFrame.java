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

        JPanel p = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        // optionally show compact logo on the left
        ImageIcon logo = UIUtils.loadScaledIcon("/Users/md.arifulislam/Downloads/Dhaka_University_logo.svg", 40, 40);
        if (logo != null) top.add(new JLabel(logo));
        JComboBox<String> types = new JComboBox<>(new String[]{"Savings","Current","Fixed"});
        JComboBox<String> searchBy = new JComboBox<>(new String[]{"Account Number","Name","Mobile"});
        JTextField search = new JTextField(16);
        JButton searchBtn = new JButton("OK");
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
        JScrollPane sp = new JScrollPane(list);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton open = new JButton("Open");
        JButton back = new JButton("Back");
        bottom.add(open); bottom.add(back);

        p.add(top, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

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
            } catch (Exception ex) { ex.printStackTrace(); }
        };

        search.addActionListener(doSearch);
        searchBtn.addActionListener(doSearch);

        open.addActionListener(e -> {
            String sel = list.getSelectedValue();
            if (sel == null) return;
            String acc = sel.split(" - ")[0];
            try {
                Account a = DBHelper.getAccountByNumber(acc);
                if (a != null) new AccountDetailFrame(this, a).setVisible(true);
                setVisible(false);
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        back.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });
    }
}
