import javax.swing.*;
import java.awt.*;

public class EmployeeFrame extends JFrame {
    public EmployeeFrame() {
        setTitle("Employee Panel - Dhaka University Bank");
        setSize(400,200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10,10));

        String projectDir = System.getProperty("user.dir");
        // prefer requested image `cse.jpg`, fall back to Oporajeyo or Science_Library
        java.awt.Image bg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/cse.jpg", projectDir + "/Icon/Oporajeyo.jpg", projectDir + "/Icon/Science_Library.jpg");
        if (bg != null) setContentPane(new BackgroundPanel(bg));

        // show compact logo and title
        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.setPreferredSize(new Dimension(100, 80));
        ImageIcon logoIcon = UIUtils.loadScaledIcon(projectDir + "/Icon/default_logo.png", 48, 48);
        if (logoIcon != null) north.add(new JLabel(logoIcon), BorderLayout.WEST);
        JLabel title = new JLabel("Employee Dashboard", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setForeground(Color.WHITE);
        north.add(title, BorderLayout.CENTER);
        add(north, BorderLayout.NORTH);

        // center - three minimalist transparent options (text-only style)
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        // use glue to center options vertically; provide horizontal padding
        // reduce top padding and add glue above to push options downward towards bottom
        center.setBorder(BorderFactory.createEmptyBorder(10,80,40,80));
        center.add(Box.createVerticalGlue());

        JButton newAcc = new JButton("New Account");
        JButton existing = new JButton("Existing Account");
        JButton logout = new JButton("Logout");

        Font optFont = newAcc.getFont().deriveFont(Font.BOLD, 18f);
        for (JButton b : new JButton[]{newAcc, existing, logout}) {
            b.setFont(optFont);
            b.setForeground(Color.WHITE);
            // give each option a semi-opaque dark background so it's visible on any image
            b.setOpaque(true);
            b.setBackground(new Color(0,0,0,150));
            b.setContentAreaFilled(true);
            b.setBorderPainted(false);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setFocusPainted(false);
            center.add(b);
            center.add(Box.createVerticalStrut(18));
        }
        // intentionally do not add bottom glue so options sit lower on the panel

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
