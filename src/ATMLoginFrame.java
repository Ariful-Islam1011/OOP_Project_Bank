import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ATMLoginFrame extends JFrame implements ActionListener {
    private JFrame parentFrame;
    private JTextField cardNumberField;
    private JPasswordField pinField;
    private JButton signInButton, backButton, clearButton;
    
    public ATMLoginFrame(JFrame parent) {
        this.parentFrame = parent;
        setTitle("ATM Login - Dhaka University Bank");
        setSize(850, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setUndecorated(true);

        String projectDir = System.getProperty("user.dir");

        // Set background image as content pane so components stay visible
        boolean bgSet = false;
        try {
            ImageIcon bgIcon = new ImageIcon(projectDir + "/Icon/backbg.png");
            if (bgIcon.getIconWidth() > 0) {
                java.awt.Image bgImg = bgIcon.getImage().getScaledInstance(850, 480, Image.SCALE_SMOOTH);
                setContentPane(new JLabel(new ImageIcon(bgImg)));
                getContentPane().setLayout(null);
                bgSet = true;
            }
        } catch (Exception ignore) { }
        if (!bgSet) {
            getContentPane().setBackground(new Color(30, 60, 90));
        }

        // Try to load DU logo (centered between title and form)
        try {
            ImageIcon duIcon = UIUtils.loadScaledIcon(projectDir + "/Icon/DU_Logo.png", 80, 80);
            if (duIcon != null) {
                JLabel logoLabel = new JLabel(duIcon);
                logoLabel.setBounds(385, 30, 80, 80);
                add(logoLabel);
            }
        } catch (Exception e) {
            // Icon not found or failed to load, continue without it
        }

        JLabel titleLabel = new JLabel("WELCOME TO ATM");
        titleLabel.setFont(new Font("AvantGarde", Font.BOLD, 38));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(230, 125, 450, 40);
        add(titleLabel);

        JLabel cardLabel = new JLabel("Card No:");
        cardLabel.setFont(new Font("Raleway", Font.BOLD, 28));
        cardLabel.setForeground(Color.WHITE);
        cardLabel.setBounds(150, 190, 375, 30);
        add(cardLabel);

        cardNumberField = new JTextField(15);
        cardNumberField.setBounds(325, 190, 230, 30);
        cardNumberField.setFont(new Font("Arial", Font.BOLD, 14));
        add(cardNumberField);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Raleway", Font.BOLD, 28));
        pinLabel.setForeground(Color.WHITE);
        pinLabel.setBounds(150, 250, 375, 30);
        add(pinLabel);

        pinField = new JPasswordField(15);
        pinField.setBounds(325, 250, 230, 30);
        pinField.setFont(new Font("Arial", Font.BOLD, 14));
        add(pinField);

        signInButton = new JButton("SUBMIT");
        signInButton.setFont(new Font("Arial", Font.BOLD, 14));
        signInButton.setForeground(Color.WHITE);
        signInButton.setBackground(Color.BLACK);
        signInButton.setOpaque(true);
        signInButton.setContentAreaFilled(true);
        signInButton.setBorderPainted(false);
        signInButton.setBounds(300, 300, 100, 30);
        signInButton.addActionListener(this);
        add(signInButton);

        clearButton = new JButton("CLEAR");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setForeground(Color.WHITE);
        clearButton.setBackground(Color.BLACK);
        clearButton.setOpaque(true);
        clearButton.setContentAreaFilled(true);
        clearButton.setBorderPainted(false);
        clearButton.setBounds(430, 300, 100, 30);
        clearButton.addActionListener(this);
        add(clearButton);

        backButton = new JButton("BACK");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setOpaque(true);
        backButton.setContentAreaFilled(true);
        backButton.setBorderPainted(false);
        backButton.setBounds(300, 350, 230, 30);
        backButton.addActionListener(this);
        add(backButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signInButton) {
            String cardNumber = cardNumberField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();

            if (cardNumber.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Card Number and PIN!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DBHelper.getConnection()) {
                // Check ATM card table (card_number, pin) and join to get account info
                String sql = "SELECT atm.account_number, ac.name FROM atm_cards atm "
                        + "LEFT JOIN accounts ac ON atm.account_number = ac.account_number "
                        + "WHERE atm.card_number = ? AND atm.pin = ? AND atm.status = 'Active'";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, cardNumber);
                    ps.setString(2, pin);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String accountNumber = rs.getString("account_number");
                            String name = rs.getString("name");

                            new ATMTransactionFrame(accountNumber, name, cardNumber, pin).setVisible(true);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid Card Number or PIN!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == clearButton) {
            cardNumberField.setText("");
            pinField.setText("");
        } else if (e.getSource() == backButton) {
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            dispose();
        }
    }
}
