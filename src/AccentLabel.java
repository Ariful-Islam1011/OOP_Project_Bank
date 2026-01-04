import javax.swing.*;
import java.awt.*;

public class AccentLabel extends JLabel {
    private final Color start;
    private final Color end;
    private final int radius;

    public AccentLabel(String text, Color accent) {
        super(text, SwingConstants.CENTER);
        this.start = accent.brighter();
        this.end = accent.darker();
        this.radius = 18;
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 22));
        setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();

        // Gradient pill background
        GradientPaint gp = new GradientPaint(0, 0, start, 0, h, end);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, w, h, radius, radius);

        // Subtle inner highlight
        g2.setColor(new Color(255, 255, 255, 30));
        g2.fillRoundRect(2, 2, w - 4, (int) (h * 0.45), radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}
