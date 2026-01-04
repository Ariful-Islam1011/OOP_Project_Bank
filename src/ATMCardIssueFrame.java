import javax.swing.*;
import java.awt.*;

public class ATMCardIssueFrame extends JFrame {
    private JFrame parentFrame;
    private Account account;

    public ATMCardIssueFrame(JFrame parent, Account acc) {
        this.parentFrame = parent;
        this.account = acc;
        setTitle("Issue New ATM Card - " + account.getAccountNumber());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        String projectDir = System.getProperty("user.dir");
        java.awt.Image bg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/pukur.jpg", projectDir + "/Icon/Hall.jpg");
        if (bg != null) setContentPane(new BackgroundPanel(bg));

        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.setPreferredSize(new Dimension(100, 80));
        ImageIcon logoIcon = UIUtils.loadScaledIcon(projectDir + "/Icon/DU_Logo.png", 48, 48);
        if (logoIcon != null) north.add(new JLabel(logoIcon), BorderLayout.WEST);
        JLabel title = new JLabel("Issue New ATM Card", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setForeground(new Color(0, 255, 255));
        north.add(title, BorderLayout.CENTER);

        // Content
        TranslucentPanel content = new TranslucentPanel(new Color(0, 0, 0, 120), 18, 18);
        content.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.anchor = GridBagConstraints.WEST;

        gc.gridx = 0;
        gc.gridy = 0;
        JLabel infoLabel = new JLabel("Account Information");
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 16f));
        infoLabel.setForeground(Color.WHITE);
        gc.gridwidth = 2;
        content.add(infoLabel, gc);

        gc.gridwidth = 1;
        gc.gridy++;

        gc.gridx = 0;
        JLabel accLabel = new JLabel("Account Number:");
        accLabel.setForeground(Color.WHITE);
        content.add(accLabel, gc);
        gc.gridx = 1;
        JLabel accValue = new JLabel(account.getAccountNumber());
        accValue.setForeground(Color.YELLOW);
        content.add(accValue, gc);

        gc.gridx = 0;
        gc.gridy++;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        content.add(nameLabel, gc);
        gc.gridx = 1;
        JLabel nameValue = new JLabel(account.getName());
        nameValue.setForeground(Color.YELLOW);
        content.add(nameValue, gc);

        gc.gridx = 0;
        gc.gridy++;
        JLabel mobileLabel = new JLabel("Mobile:");
        mobileLabel.setForeground(Color.WHITE);
        content.add(mobileLabel, gc);
        gc.gridx = 1;
        JLabel mobileValue = new JLabel(account.getMobile());
        mobileValue.setForeground(Color.YELLOW);
        content.add(mobileValue, gc);

        gc.gridx = 0;
        gc.gridy += 2;
        gc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton issueBtn = createRoundedButton("Issue Card", new Color(34, 139, 34), 120, 40);
        JButton backBtn = createRoundedButton("Back", new Color(139, 69, 19), 120, 40);

        buttonPanel.add(issueBtn);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(backBtn);
        content.add(buttonPanel, gc);

        issueBtn.addActionListener(e -> issueNewCard());
        backBtn.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        });

        // Assemble
        add(north, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }

    private void issueNewCard() {
        try {
            String cardNumber = DBHelper.issueATMCard(account.getAccountNumber(), account.getName());
            JOptionPane.showMessageDialog(this, "Card issued successfully!\nCard Number: " + cardNumber, "Success", JOptionPane.INFORMATION_MESSAGE);
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createRoundedButton(String text, Color bgColor, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(); int h = getHeight();
                Color fill = bgColor;
                if (getModel().isPressed()) fill = bgColor.darker();
                else if (getModel().isRollover()) fill = bgColor.brighter();
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, w, h, 15, 15);
                g2.setColor(Color.WHITE);
                g2.setStroke(new java.awt.BasicStroke(2f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 15, 15);
                g2.dispose();
                setContentAreaFilled(false); setOpaque(false);
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, height));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorderPainted(false);
        return button;
    }
}
