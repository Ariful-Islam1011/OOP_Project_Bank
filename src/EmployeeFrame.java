import javax.swing.*;
import java.awt.*;

public class EmployeeFrame extends JFrame {
    public EmployeeFrame() {
        setTitle("Employee Panel - Dhaka University Bank");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10,10));

        String projectDir = System.getProperty("user.dir");
        // prefer requested image `oporajeyo.jpg`, fall back to cse or Science_Library
        java.awt.Image bg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/oporajeyo.jpg", projectDir + "/Icon/cse.jpg", projectDir + "/Icon/Science_Library.jpg");
        if (bg != null) setContentPane(new BackgroundPanel(bg));

        // show compact logo and title
        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.setPreferredSize(new Dimension(100, 100));
        ImageIcon logoIcon = UIUtils.loadScaledIcon(projectDir + "/Icon/DU_Logo.png", 64, 64);
        if (logoIcon != null) north.add(new JLabel(logoIcon), BorderLayout.WEST);
        JLabel title = new JLabel("Employee Dashboard", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 32f));
        title.setForeground(new Color(0, 255, 255));
        north.add(title, BorderLayout.CENTER);

        // Center - stacked pill-style buttons
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(60, 200, 40, 200));

        JButton newAcc = UIUtils.createStyledButton("🆕 New Account", new Color(76, 175, 80, 200), 320, 60);
        JButton existing = UIUtils.createStyledButton("📂 Existing Account", new Color(33, 150, 243, 200), 320, 60);
        JButton customerService = UIUtils.createStyledButton("👩‍💼 Customer Service", new Color(255, 152, 0, 200), 320, 60);

        newAcc.setAlignmentX(Component.CENTER_ALIGNMENT);
        existing.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerService.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        center.add(Box.createVerticalGlue());
        center.add(newAcc);
        center.add(Box.createVerticalStrut(18));
        center.add(existing);
        center.add(Box.createVerticalStrut(18));
        center.add(customerService);
        center.add(Box.createVerticalGlue());

        // Logout button at bottom center
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false);
        
        JButton logout = UIUtils.createStyledButton("🔒 Logout", new Color(244, 67, 54, 200), 160, 40);
        bottomPanel.add(logout);

        // now add panels into the frame
        java.awt.Container cp = getContentPane();
        if (cp instanceof BackgroundPanel) {
            JPanel content = new JPanel(new BorderLayout());
            content.setOpaque(false);
            north.setOpaque(false);
            center.setOpaque(false);
            content.add(north, BorderLayout.NORTH);
            content.add(center, BorderLayout.CENTER);
            content.add(bottomPanel, BorderLayout.SOUTH);
            add(content, BorderLayout.CENTER);
        } else {
            add(north, BorderLayout.NORTH);
            add(center, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);
        }

        newAcc.addActionListener(e -> {
            new NewAccountForm1(this).setVisible(true);
            setVisible(false);
        });

        existing.addActionListener(e -> {
            new ExistingAccountsFrame(this).setVisible(true);
            setVisible(false);
        });

        customerService.addActionListener(e -> {
            new CustomerServiceFrame(this).setVisible(true);
            setVisible(false);
        });

        logout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

}
