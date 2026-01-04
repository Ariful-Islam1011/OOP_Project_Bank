import javax.swing.*;
import java.awt.*;

public class GlassCardPanel extends JPanel {
    private Color startColor;
    private Color endColor;
    private int cornerRadius;
    private int shadowSize;

    public GlassCardPanel(Color startColor, Color endColor, int cornerRadius, int shadowSize) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.cornerRadius = cornerRadius;
        this.shadowSize = shadowSize;
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    public void setPadding(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Soft shadow (fake blur by multiple passes)
        for (int i = shadowSize; i > 0; i--) {
            float alpha = 0.04f;
            g2.setColor(new Color(0f, 0f, 0f, alpha));
            g2.fillRoundRect(i, i, w - 2 * i, h - 2 * i, cornerRadius, cornerRadius);
        }

        // Gradient glass background
        GradientPaint gp = new GradientPaint(0, 0, startColor, w, h, endColor);
        g2.setPaint(gp);
        g2.fillRoundRect(shadowSize, shadowSize, w - 2 * shadowSize, h - 2 * shadowSize, cornerRadius, cornerRadius);

        // Subtle border
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(255, 255, 255, 60));
        g2.drawRoundRect(shadowSize, shadowSize, w - 2 * shadowSize, h - 2 * shadowSize, cornerRadius, cornerRadius);

        g2.dispose();
    }
}
