import javax.swing.*;
import java.awt.*;

/**
 * Simple panel that paints a background image scaled to fill the panel.
 */
public class BackgroundPanel extends JPanel {
    private final Image img;

    public BackgroundPanel(Image img) {
        this.img = img;
        setLayout(new BorderLayout());
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
