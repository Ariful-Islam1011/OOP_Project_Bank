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

        setLayout(new BorderLayout(10,10));

        // try to load building background (attached image) and logo
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBorder(BorderFactory.createEmptyBorder(0,0,6,0));

        JLabel header = new JLabel("Welcome to Dhaka University Bank", SwingConstants.CENTER);
        header.setOpaque(false);
        header.setForeground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        header.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        // Preferred background image candidates (please copy the attached building image to one of these paths
        // if it isn't already on your machine):
        //  - ~/Downloads/du_building.jpg
        //  - ~/Downloads/dhaka_building.jpg
        //  - ~/Downloads/DhakaUniversity_building.jpg
        // The code will also attempt the classpath resource `/images/login_bg.jpg`.
        String[] bgCandidates = new String[] {
            System.getProperty("user.home") + "/Downloads/du_building.jpg",
            System.getProperty("user.home") + "/Downloads/dhaka_building.jpg",
            System.getProperty("user.home") + "/Downloads/DhakaUniversity_building.jpg",
            "/images/login_bg.jpg"
        };

        // Load logo from known absolute paths (try SVG then PNG/JPG fallbacks and classpath)
        String[] logoCandidates = new String[] {
            System.getProperty("user.home") + "/Downloads/Dhaka_University_logo.svg",
            System.getProperty("user.home") + "/Downloads/Dhaka_University_logo.png",
            System.getProperty("user.home") + "/Downloads/Dhaka_University_logo.jpg",
            "/images/logo.png",
            "/images/logo.jpg"
        };
        ImageIcon logoIcon = null;
        for (String lp : logoCandidates) {
            logoIcon = UIUtils.loadScaledIcon(lp, 64, 64);
            if (logoIcon != null) break;
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
        for (String cand : bgCandidates) {
            bg = UIUtils.loadScaledIcon(cand, 800, 120);
            if (bg != null) break;
        }
        if (bg != null) {
            JLabel bgLabel = new JLabel(bg);
            bgLabel.setLayout(new BorderLayout());
            bgLabel.add(header, BorderLayout.CENTER);
            northPanel.add(bgLabel, BorderLayout.CENTER);
        } else {
            // fallback solid color
            JPanel colored = new JPanel(new BorderLayout());
            colored.setBackground(new Color(0x2E8B57));
            colored.add(header, BorderLayout.CENTER);
            northPanel.add(colored, BorderLayout.CENTER);
        }

        add(northPanel, BorderLayout.NORTH);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.EAST;

        c.gridx = 0; c.gridy = 0; p.add(new JLabel("Admin Password:"), c);
        c.gridx = 1; JPasswordField pass = new JPasswordField(15); p.add(pass, c);

        c.gridx = 0; c.gridy = 1; p.add(new JLabel("Bank Number:"), c);
        c.gridx = 1; JTextField bankNum = new JTextField(15); p.add(bankNum, c);

        c.gridx = 0; c.gridy = 2; JButton login = new JButton("Login as Admin"); p.add(login, c);
        c.gridx = 1; JButton exit = new JButton("Exit"); p.add(exit, c);

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
