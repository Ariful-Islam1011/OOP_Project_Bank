import javax.swing.*;
import java.awt.*;

public class ATMCardMenuFrame extends JFrame {
    private JFrame parentFrame;
    private Account account;

    public ATMCardMenuFrame(JFrame parent, Account acc) {
        System.out.println("[DEBUG] Opening ATMCardMenuFrame");
        this.parentFrame = parent;
        this.account = acc;
        setTitle(account != null ? "ATM Card Management - " + account.getAccountNumber() : "ATM Card Management");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        String projectDir = System.getProperty("user.dir");
        java.awt.Image bg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/oporajeyo.jpg", projectDir + "/Icon/cse.jpg");
        if (bg != null) setContentPane(new BackgroundPanel(bg));

        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.setPreferredSize(new Dimension(100, 100));
        ImageIcon logoIcon = UIUtils.loadScaledIcon(projectDir + "/Icon/DU_Logo.png", 64, 64);
        if (logoIcon != null) north.add(new JLabel(logoIcon), BorderLayout.WEST);
        JLabel title = new JLabel("ATM Card Management", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 32f));
        title.setForeground(new Color(0, 255, 255));
        north.add(title, BorderLayout.CENTER);

        // center - stacked pill/transparent-style options
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(60, 200, 40, 200));

        JButton issueCard = UIUtils.createStyledButton("🆕 Issue New Card", new Color(76, 175, 80, 200), 320, 60);
        JButton existingCard = UIUtils.createStyledButton("📂 Existing Cards", new Color(33, 150, 243, 200), 320, 60);

        issueCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        existingCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(Box.createVerticalGlue());
        center.add(issueCard);
        center.add(Box.createVerticalStrut(18));
        center.add(existingCard);
        center.add(Box.createVerticalGlue());

        // Bottom Back button centered
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setOpaque(false);
        JButton backBtn = UIUtils.createStyledButton("◄ Back", new Color(244, 67, 54, 200), 160, 40);
        bottom.add(backBtn);

        // Assemble frame
        java.awt.Container cp = getContentPane();
        if (cp instanceof BackgroundPanel) {
            TranslucentPanel content = new TranslucentPanel(new Color(0, 0, 0, 120), 18, 18);
            content.setLayout(new BorderLayout());
            north.setOpaque(false);
            center.setOpaque(false);
            bottom.setOpaque(false);
            content.add(north, BorderLayout.NORTH);
            content.add(center, BorderLayout.CENTER);
            content.add(bottom, BorderLayout.SOUTH);
            add(content, BorderLayout.CENTER);
        } else {
            add(north, BorderLayout.NORTH);
            add(center, BorderLayout.CENTER);
            add(bottom, BorderLayout.SOUTH);
        }

        // Button Actions
        issueCard.addActionListener(e -> {
            System.out.println("[DEBUG] ATMCardMenu: Issue New Card clicked");
            new NewATMCardIssueFrame(this).setVisible(true);
            setVisible(false);
        });

        existingCard.addActionListener(e -> {
            System.out.println("[DEBUG] ATMCardMenu: Existing Cards clicked");
            new ATMCardExistingFrame(this).setVisible(true);
            setVisible(false);
        });

        backBtn.addActionListener(e -> {
            System.out.println("[DEBUG] ATMCardMenu: Back clicked");
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            dispose();
        });
    }
}
