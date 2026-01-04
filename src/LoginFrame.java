import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {
    // Accessible, modern palette
    private static final Color BG_GLASS_START = new Color(18, 22, 28, 180);
    private static final Color BG_GLASS_END   = new Color(18, 22, 28, 120);
    private static final Color TEXT_PRIMARY   = new Color(255, 255, 255);
    private static final Color TEXT_MUTED     = new Color(208, 215, 226);
    private static final Color ACCENT_ADMIN   = new Color(56, 142, 60);  // #388E3C
    private static final Color ACCENT_ATM     = new Color(47, 128, 237); // #2F80ED

    public LoginFrame() {
        setTitle("Dhaka University Bank - Login");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Use full-window background image
        setLayout(new BorderLayout());
        java.awt.Image loginBg = UIUtils.loadImageFromCandidates(System.getProperty("user.dir") + "/Icon/curzon.jpeg");
        if (loginBg != null) setContentPane(new BackgroundPanel(loginBg));

        String projectDir = System.getProperty("user.dir");

        // Main container with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        // ===== TOP HEADER PANEL =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(900, 120));
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        ImageIcon logoIcon = UIUtils.loadScaledIcon(projectDir + "/Icon/DU_Logo.png", 80, 80);
        if (logoIcon != null) headerPanel.add(new JLabel(logoIcon), BorderLayout.WEST);

        JLabel titleLabel = new JLabel("DHAKA UNIVERSITY BANK");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(0, 255, 255));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // ===== CENTER PANEL WITH TWO COLUMNS (Admin & ATM cards) =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(30, 60, 60, 60));

        // LEFT PANEL: Login Form (Glass card)
        JPanel leftPanel = createFormPanel(projectDir);

        // RIGHT PANEL: ATM Login card (Glass card)
        JPanel rightPanel = createATMLoginPanel(projectDir);

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        // Assemble main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Set content pane
        if (loginBg != null) {
            setContentPane(new BackgroundPanel(loginBg));
            getContentPane().add(mainPanel);
        } else {
            add(mainPanel);
        }
    }

    private JPanel createFormPanel(String projectDir) {
        GlassCardPanel formPanel = new GlassCardPanel(
                BG_GLASS_START,
                BG_GLASS_END,
                20,
                12);
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(15, 15, 15, 15);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        // Form Title (accent pill)
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        AccentLabel formTitle = new AccentLabel("ADMIN LOGIN", ACCENT_ADMIN);
        formPanel.add(formTitle, gc);
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Separator
        gc.gridy++;
        gc.gridwidth = 2;
        JSeparator sep = new JSeparator();
        sep.setForeground(ACCENT_ADMIN);
        formPanel.add(sep, gc);

        // Password Label
        gc.gridy++;
        gc.gridwidth = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(TEXT_PRIMARY);
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(passLabel, gc);

        // Password Field with Eye Icon
        gc.gridx = 0;
        gc.gridy++;
        gc.gridwidth = 2;
        JPanel passFieldPanel = createStyledPasswordField();
        formPanel.add(passFieldPanel, gc);
        // Get the actual password field from the panel
        JPasswordField passField = (JPasswordField) passFieldPanel.getComponent(0);
        
        gc.gridwidth = 1;

        // Bank Number Label
        gc.gridx = 0;
        gc.gridy++;
        JLabel bankLabel = new JLabel("Bank Number:");
        bankLabel.setForeground(TEXT_PRIMARY);
        bankLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(bankLabel, gc);

        // Bank Number Field
        gc.gridx = 0;
        gc.gridy++;
        JTextField bankField = createStyledTextField();
        formPanel.add(bankField, gc);

        // Login Button
        gc.gridx = 0;
        gc.gridy++;
        gc.gridwidth = 2;
        gc.ipady = 15;
        JButton loginBtn = createStyledButton("LOGIN", ACCENT_ADMIN);
        loginBtn.setEnabled(true);
        System.out.println("Login button created and enabled");
        formPanel.add(loginBtn, gc);

        // Button Actions
        final String ADMIN_PASS = "DhakaUniversity";
        final String BANK_NUM = "192117475354";

        loginBtn.addActionListener(e -> {
            System.out.println("=== LOGIN BUTTON CLICKED ===");
            String pw = new String(passField.getPassword()).trim();
            String bn = bankField.getText().trim();
            System.out.println("Debug - Password entered: '" + pw + "' Expected: '" + ADMIN_PASS + "'");
            System.out.println("Debug - Bank# entered: '" + bn + "' Expected: '" + BANK_NUM + "'");
            System.out.println("Debug - Pass match: " + ADMIN_PASS.equals(pw));
            System.out.println("Debug - Bank# match: " + BANK_NUM.equalsIgnoreCase(bn));
            
            if (pw.isEmpty() || bn.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Please fill in both fields", "Missing Fields", JOptionPane.WARNING_MESSAGE);
            } else if (ADMIN_PASS.equals(pw) && BANK_NUM.equalsIgnoreCase(bn)) {
                new EmployeeFrame().setVisible(true);
                LoginFrame.this.dispose();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid credentials. Check password and bank number.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
                bankField.setText("");
            }
        });

        return formPanel;
    }

    private JPanel createATMLoginPanel(String projectDir) {
        GlassCardPanel actionsPanel = new GlassCardPanel(
            BG_GLASS_START,
            BG_GLASS_END,
            20,
            12);
        actionsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(20, 20, 20, 20);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        // Title (accent pill)
        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        AccentLabel actionsTitle = new AccentLabel("ATM LOGIN", ACCENT_ATM);
        actionsPanel.add(actionsTitle, gc);
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Bank Logo
        gc.gridy++;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        ImageIcon bankLogoIcon = UIUtils.loadScaledIcon(projectDir + "/Icon/Bank Logo.png", 80, 80);
        if (bankLogoIcon != null) {
            JLabel bankLogoLabel = new JLabel(bankLogoIcon);
            actionsPanel.add(bankLogoLabel, gc);
        }
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Proceed to ATM Login (Large and prominent)
        gc.gridy++;
        gc.ipady = 20;
        JButton atmBtn = createStyledButton("PROCEED", ACCENT_ATM);
        actionsPanel.add(atmBtn, gc);

        // Decorative spacing
        gc.gridy++;
        gc.ipady = 0;
        JLabel spacing1 = new JLabel("");
        spacing1.setPreferredSize(new Dimension(0, 10));
        actionsPanel.add(spacing1, gc);

        // Info text
        gc.gridy++;
        gc.ipady = 0;
        JLabel infoLabel = new JLabel("<html><center>Use ATM card to access<br>your account</center></html>");
        infoLabel.setForeground(TEXT_MUTED);
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        actionsPanel.add(infoLabel, gc);

        // Button Actions
        atmBtn.addActionListener(e -> {
            new ATMLoginFrame(this).setVisible(true);
            setVisible(false);
        });

        // Make whole card clickable for convenience
        actionsPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionsPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                atmBtn.doClick();
            }
        });

        return actionsPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();

                // Soft shadow
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(2, 4, w - 4, h - 2, 12, 12);

                // Gradient fill
                Color c1 = getModel().isPressed() ? bgColor.darker() : bgColor.brighter();
                Color c2 = getModel().isPressed() ? bgColor : bgColor.darker();
                GradientPaint gp = new GradientPaint(0, 0, c1, 0, h, c2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w - 4, h - 4, 12, 12);

                // Text rendering
                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 50));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setFont(new Font("Segoe UI", Font.BOLD, 15));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            }
        });

        return button;
    }

    private JPanel createStyledPasswordField() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JPasswordField field = new JPasswordField();
        field.setBackground(new Color(42, 46, 52));
        field.setForeground(TEXT_PRIMARY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_ADMIN, 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 40)
        ));
        field.setPreferredSize(new Dimension(200, 38));
        
        // Eye icon button to show/hide password
        JButton eyeBtn = new JButton("👁");
        eyeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        eyeBtn.setBackground(new Color(42, 46, 52));
        eyeBtn.setForeground(ACCENT_ADMIN);
        eyeBtn.setBorderPainted(false);
        eyeBtn.setFocusPainted(false);
        eyeBtn.setContentAreaFilled(false);
        eyeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeBtn.setPreferredSize(new Dimension(35, 38));
        eyeBtn.setToolTipText("Show/Hide Password");
        
        eyeBtn.addActionListener(e -> {
            if (field.getEchoChar() == (char) 0) {
                field.setEchoChar('•');
                eyeBtn.setText("👁");
            } else {
                field.setEchoChar((char) 0);
                eyeBtn.setText("🙈");
            }
        });
        
        panel.add(field, BorderLayout.CENTER);
        panel.add(eyeBtn, BorderLayout.EAST);
        
        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(42, 46, 52));
        field.setForeground(TEXT_PRIMARY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_ADMIN, 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setPreferredSize(new Dimension(200, 38));
        return field;
    }
}
