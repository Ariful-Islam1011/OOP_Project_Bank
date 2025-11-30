import javax.swing.*;
import java.awt.*;

public class EmployeeFrame extends JFrame {
    public EmployeeFrame() {
        setTitle("Employee Panel - Dhaka University Bank");
        setSize(400,200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10,10));

        // show small logo in header if available
        JPanel north = new JPanel(new BorderLayout());
        ImageIcon logoIcon = UIUtils.loadScaledIcon("/Users/md.arifulislam/Downloads/Dhaka_University_logo.svg", 48, 48);
        if (logoIcon != null) north.add(new JLabel(logoIcon), BorderLayout.WEST);
        JLabel title = new JLabel("Employee Dashboard", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        north.add(title, BorderLayout.CENTER);
        add(north, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(3,1,10,10));
        center.setBorder(BorderFactory.createEmptyBorder(20,80,20,80));
        JButton newAcc = new JButton("New Account");
        JButton existing = new JButton("Existing Account");
        JButton logout = new JButton("Logout");
        center.add(newAcc);
        center.add(existing);
        center.add(logout);
        add(center, BorderLayout.CENTER);

        newAcc.addActionListener(e -> {
            new NewAccountForm1(this).setVisible(true);
            setVisible(false);
        });

        existing.addActionListener(e -> {
            new ExistingAccountsFrame(this).setVisible(true);
            setVisible(false);
        });

        logout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }
}
