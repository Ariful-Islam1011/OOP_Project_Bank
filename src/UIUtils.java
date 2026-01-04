import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.io.File;
import java.io.InputStream;

public class UIUtils {
    // Try loading an image from several candidate locations (absolute path, project resources, classpath)
    public static Image loadImageFromCandidates(String... candidates) {
        for (String p : candidates) {
            if (p == null) continue;
            try {
                System.out.println("UIUtils: trying candidate -> " + p);
                // try absolute file
                File f = new File(p);
                if (f.exists() && f.isFile()) {
                    Image img = ImageIO.read(f);
                    System.out.println("UIUtils: loaded from file -> " + p + " (" + (img!=null) + ")");
                    if (img != null) return img;
                }

                // If candidate is an SVG, also try common raster fallbacks (.png, .jpg)
                if (p.toLowerCase().endsWith(".svg")) {
                    String png = p.substring(0, p.length()-4) + ".png";
                    File fp = new File(png);
                    if (fp.exists() && fp.isFile()) {
                        Image img = ImageIO.read(fp);
                        System.out.println("UIUtils: loaded raster fallback -> " + png + " (" + (img!=null) + ")");
                        if (img != null) return img;
                    }
                    String jpg = p.substring(0, p.length()-4) + ".jpg";
                    File fj = new File(jpg);
                    if (fj.exists() && fj.isFile()) {
                        Image img = ImageIO.read(fj);
                        System.out.println("UIUtils: loaded raster fallback -> " + jpg + " (" + (img!=null) + ")");
                        if (img != null) return img;
                    }
                }
            } catch (Exception ignored) {
                System.out.println("UIUtils: failed reading file candidate -> " + p + " : " + ignored.getMessage());
            }
            try {
                // try classpath resource
                InputStream is = UIUtils.class.getResourceAsStream(p);
                System.out.println("UIUtils: trying classpath resource -> " + p + " -> " + (is!=null));
                if (is != null) {
                    Image img = ImageIO.read(is);
                    System.out.println("UIUtils: loaded from classpath -> " + p + " (" + (img!=null) + ")");
                    if (img != null) return img;
                }

                // also try classpath raster fallbacks for svg candidate
                if (p.toLowerCase().endsWith(".svg")) {
                    String png = p.substring(0, p.length()-4) + ".png";
                    InputStream isp = UIUtils.class.getResourceAsStream(png);
                    System.out.println("UIUtils: trying classpath resource -> " + png + " -> " + (isp!=null));
                    if (isp != null) {
                        Image img = ImageIO.read(isp);
                        System.out.println("UIUtils: loaded from classpath -> " + png + " (" + (img!=null) + ")");
                        if (img != null) return img;
                    }
                    String jpg = p.substring(0, p.length()-4) + ".jpg";
                    InputStream isj = UIUtils.class.getResourceAsStream(jpg);
                    System.out.println("UIUtils: trying classpath resource -> " + jpg + " -> " + (isj!=null));
                    if (isj != null) {
                        Image img = ImageIO.read(isj);
                        System.out.println("UIUtils: loaded from classpath -> " + jpg + " (" + (img!=null) + ")");
                        if (img != null) return img;
                    }
                }
            } catch (Exception ignored) {
                System.out.println("UIUtils: failed reading classpath candidate -> " + p + " : " + ignored.getMessage());
            }
        }
        return null;
    }

    public static ImageIcon loadScaledIcon(String pathCandidate, int w, int h) {
        Image img = loadImageFromCandidates(pathCandidate);
        if (img == null) {
            // create a runtime placeholder image to avoid empty UI
            img = createPlaceholder(pathCandidate, w, h);
        }
        Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private static Image createPlaceholder(String hint, int w, int h) {
        try {
            BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bi.createGraphics();
            // simple gradient-like background
            Color c1 = new Color(0x2E8B57);
            Color c2 = new Color(0x77B77A);
            for (int y = 0; y < h; y++) {
                float t = (float)y / (float)h;
                int r = (int)(c1.getRed()*(1-t) + c2.getRed()*t);
                int gr = (int)(c1.getGreen()*(1-t) + c2.getGreen()*t);
                int b = (int)(c1.getBlue()*(1-t) + c2.getBlue()*t);
                g.setColor(new Color(r, gr, b));
                g.fillRect(0, y, w, 1);
            }
            // Draw label only for logo-like placeholders. For background/placeholders used as header
            // background we keep the image text-free to avoid duplicating the header JLabel text.
            boolean drawText = true;
            if (hint != null) {
                String low = hint.toLowerCase();
                if (low.contains("login_bg") || low.contains("building") || low.contains("du_building") || low.contains("dhaka_building")) {
                    drawText = false;
                }
            }
            if (drawText) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("SansSerif", Font.BOLD, Math.max(12, h/6)));
                String text = "Dhaka University Bank";
                if (hint != null && hint.toLowerCase().contains("logo")) {
                    text = "DU Bank";
                }
                int sw = g.getFontMetrics().stringWidth(text);
                g.drawString(text, Math.max(8, (w-sw)/2), Math.max(h/2 + 6, h/2 + g.getFontMetrics().getAscent()/2));
            }
            g.dispose();
            return bi;
        } catch (Exception ex) {
            return null;
        }
    }

    // Create styled button with modern rounded design
    public static JButton createStyledButton(String text, Color bgColor, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Determine fill color based on state
                Color fill = bgColor;
                if (getModel().isPressed()) fill = bgColor.darker();
                else if (getModel().isRollover()) fill = bgColor.brighter();

                int w = getWidth();
                int h = getHeight();
                int arc = Math.min(h, w) - 2; // pill-like rounded ends

                // Draw pill background
                g2.setColor(fill);
                g2.fillRoundRect(1, 1, w - 2, h - 2, arc, arc);

                // Draw border
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, arc, arc);

                g2.dispose();

                // Draw text/icon
                setContentAreaFilled(false); // ensure super doesn't overpaint the background
                setOpaque(false);
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) { /* handled in paintComponent */ }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, height));
        button.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));

        // Hover effect via repaint
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { button.repaint(); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { button.repaint(); }
            @Override public void mousePressed(java.awt.event.MouseEvent e) { button.repaint(); }
            @Override public void mouseReleased(java.awt.event.MouseEvent e) { button.repaint(); }
        });
        return button;
    }
}
