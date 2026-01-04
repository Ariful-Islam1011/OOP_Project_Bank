import javax.swing.*;
import java.awt.*;

public class CustomerServiceFrame extends JFrame {
    private JFrame parentFrame;
    private Account account;
    
    public CustomerServiceFrame(JFrame parent) {
        this(parent, null);
    }

    public CustomerServiceFrame(JFrame parent, Account acc) {
        this.parentFrame = parent;
        this.account = acc;
        setTitle("Customer Service - Dhaka University Bank");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10,10));

        String projectDir = System.getProperty("user.dir");
        java.awt.Image bg = UIUtils.loadImageFromCandidates(projectDir + "/Icon/oporajeyo.jpg", projectDir + "/Icon/cse.jpg", projectDir + "/Icon/Science_Library.jpg");
        if (bg != null) setContentPane(new BackgroundPanel(bg));

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.setPreferredSize(new Dimension(100, 100));
        ImageIcon logoIcon = UIUtils.loadScaledIcon(projectDir + "/Icon/DU_Logo.png", 64, 64);
        if (logoIcon != null) north.add(new JLabel(logoIcon), BorderLayout.WEST);
        JLabel title = new JLabel("Customer Service", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 32f));
        title.setForeground(new Color(0, 255, 255));
        north.add(title, BorderLayout.CENTER);

        // Center - stacked pill-style buttons
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(60, 200, 40, 200));

        JButton balanceTransfer = UIUtils.createStyledButton("💸 Balance Transfer", new Color(33, 150, 243, 200), 320, 60);
        JButton atmCardIssue = UIUtils.createStyledButton("💳 ATM Card Management", new Color(76, 175, 80, 200), 320, 60);

        balanceTransfer.setAlignmentX(Component.CENTER_ALIGNMENT);
        atmCardIssue.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(Box.createVerticalGlue());
        center.add(balanceTransfer);
        center.add(Box.createVerticalStrut(18));
        center.add(atmCardIssue);
        center.add(Box.createVerticalGlue());

        // Bottom Back button centered
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setOpaque(false);
        JButton backBtn = UIUtils.createStyledButton("◄ Back", new Color(244, 67, 54, 200), 160, 40);

        bottom.add(backBtn);

        // now add panels into the frame or into a content wrapper when a bg exists
        java.awt.Container cp = getContentPane();
        if (cp instanceof BackgroundPanel) {
            TranslucentPanel content = new TranslucentPanel(new Color(0,0,0,100), 18, 18);
            content.setLayout(new BorderLayout());
            // keep inner panels transparent so the translucent overlay shows the bg
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

        balanceTransfer.addActionListener(e -> {
            System.out.println("[DEBUG] CustomerService: Balance Transfer clicked");
            new BalanceTransferFrame(this, account).setVisible(true);
            setVisible(false);
        });

        atmCardIssue.addActionListener(e -> {
            System.out.println("[DEBUG] CustomerService: ATM Card Management clicked");
            new ATMCardMenuFrame(this, account).setVisible(true);
            setVisible(false);
        });

        backBtn.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            dispose();
        });
    }

}
