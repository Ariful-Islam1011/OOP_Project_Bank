import javax.swing.*;
import java.awt.*;

/**
 * Panel that paints a translucent rounded rectangle so background images show through.
 * Add components to this panel; keep child components opaque=false for best effect.
 */
public class TranslucentPanel extends JPanel {
    private final Color fill;
    private final int arcWidth;
    private final int arcHeight;

    public TranslucentPanel(Color fill, int arcWidth, int arcHeight) {
        this.fill = fill == null ? new Color(0,0,0,120) : fill;
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setOpaque(false);
    }

    public TranslucentPanel() { this(new Color(0,0,0,120), 18, 18); }

    @Override
    protected void paintComponent(Graphics g) {
        // paint translucent rounded rect background then allow children to be painted
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
        } finally {
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
