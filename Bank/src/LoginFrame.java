import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/*
 * Enhancements:
 * - Load background image for the login header from the provided path if available.
 * - Use project logo in the header (left) when present.
 */

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Dhaka University Bank - Welcome");
        setSize(450, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Use full-window background image (login: curzon.jpeg)
        setLayout(new BorderLayout());
        java.awt.Image loginBg = UIUtils.loadImageFromCandidates(System.getProperty("user.dir") + "/Icon/curzon.jpeg");
        if (loginBg != null) setContentPane(new BackgroundPanel(loginBg));

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.setBorder(BorderFactory.createEmptyBorder(6,12,6,12));

        JLabel header = new JLabel("Welcome to Dhaka University Bank", SwingConstants.CENTER);
        header.setOpaque(false);
        header.setForeground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        header.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        // Preferred background image candidates. Also try the project's `Icon/` folder so
        // the two images included in the repository can be used as the header background.
        String projectDir = System.getProperty("user.dir");
        String[] bgCandidates = new String[] {
            // project Icon folder (preferred)
            projectDir + "/Icon/image-253831-1741682637.jpg",
            projectDir + "/Icon/avrsat1ap.png",
            // user Downloads fallbacks
            System.getProperty("user.home") + "/Downloads/du_building.jpg",
            System.getProperty("user.home") + "/Downloads/dhaka_building.jpg",
            System.getProperty("user.home") + "/Downloads/DhakaUniversity_building.jpg",
            // classpath resource fallback
            "/images/login_bg.jpg",
            // classpath Icon resource fallback
            "/Icon/image-253831-1741682637.jpg",
            "/Icon/avrsat1ap.png"
        };

        // Load logo from project Icon folder (prefer default_logo.png)
        String[] logoCandidates = new String[] {
            // prefer the project's Icon logo if present
            projectDir + "/Icon/avrsat1ap.png",
            projectDir + "/Icon/image-253831-1741682637.jpg",
            // user Downloads fallbacks
            System.getProperty("user.home") + "/Downloads/Dhaka_University_logo.svg",
            System.getProperty("user.home") + "/Downloads/Dhaka_University_logo.png",
            System.getProperty("user.home") + "/Downloads/Dhaka_University_logo.jpg",
            // classpath fallbacks
            "/images/logo.png",
            "/images/logo.jpg",
            "/Icon/avrsat1ap.png"
        };
        ImageIcon logoIcon = null;
        java.awt.Image explicitLogo = UIUtils.loadImageFromCandidates(projectDir + "/Icon/default_logo.png", projectDir + "/Icon/avrsat1ap.png");
        if (explicitLogo != null) {
            logoIcon = new ImageIcon(explicitLogo.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
        } else {
            for (String lp : logoCandidates) {
                logoIcon = UIUtils.loadScaledIcon(lp, 64, 64);
                if (logoIcon != null) break;
            }
        }
        if (logoIcon != null) {
            JLabel logoLabel = new JLabel(logoIcon);
            logoLabel.setBorder(BorderFactory.createEmptyBorder(6,12,6,12));
            northPanel.add(logoLabel, BorderLayout.WEST);
        } else {
            // small placeholder so layout looks intentional
            JLabel ph = new JLabel(" ");
            ph.setPreferredSize(new Dimension(64,64));
            northPanel.add(ph, BorderLayout.WEST);
        }
        // try setting a background image for the header area (prefer building image candidates)
        ImageIcon bg = null;
        // Prefer the explicit first image in Icon/ as the header background
        java.awt.Image explicitBg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/image-253831-1741682637.jpg");
        if (explicitBg != null) {
            bg = new ImageIcon(explicitBg.getScaledInstance(800, 120, Image.SCALE_SMOOTH));
        } else {
            for (String cand : bgCandidates) {
                bg = UIUtils.loadScaledIcon(cand, 800, 120);
                if (bg != null) break;
            }
        }
        northPanel.add(header, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.EAST;

        c.gridx = 0; c.gridy = 0; p.add(new JLabel("Admin Password:"), c);
        c.gridx = 1; JPasswordField pass = new JPasswordField(15); p.add(pass, c);

        c.gridx = 0; c.gridy = 1; p.add(new JLabel("Bank Number:"), c);
        c.gridx = 1; JTextField bankNum = new JTextField(15); p.add(bankNum, c);

        c.gridx = 0; c.gridy = 2; JButton login = new JButton("Login as Admin");
        login.setForeground(Color.WHITE); login.setOpaque(false); login.setContentAreaFilled(false); login.setBorderPainted(false); login.setFont(login.getFont().deriveFont(Font.BOLD, 12f));
        p.add(login, c);
        c.gridx = 1; JButton exit = new JButton("Exit"); exit.setForeground(Color.WHITE); exit.setOpaque(false); exit.setContentAreaFilled(false); exit.setBorderPainted(false); exit.setFont(exit.getFont().deriveFont(Font.BOLD, 12f)); p.add(exit, c);

        add(p, BorderLayout.CENTER);

        // fixed credentials
        final String ADMIN_PASS = "DhakaUniversity";
        final String BANK_NUM = "192117475354";

        login.addActionListener((ActionEvent e) -> {
            String pw = new String(pass.getPassword()).trim();
            String bn = bankNum.getText().trim();
            // allow bank number comparison to be case-insensitive and trim whitespace from password
            if (ADMIN_PASS.equals(pw) && BANK_NUM.equalsIgnoreCase(bn)) {
                // open employee panel
                new EmployeeFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin password or bank number.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        exit.addActionListener(e -> System.exit(0));
    }
}
