import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Starting Dhaka University Bank application...");
                DBHelper.initDB();
                System.out.println("Database initialized successfully.");
                new LoginFrame().setVisible(true);
                System.out.println("Login window shown.");
            } catch (Exception e) {
                e.printStackTrace();
                // show a visible dialog with the error so it's obvious to the user
                StringBuilder sb = new StringBuilder();
                sb.append(e.toString()).append("\n");
                for (StackTraceElement st : e.getStackTrace()) {
                    sb.append(st.toString()).append("\n");
                }
                JOptionPane.showMessageDialog(null, sb.toString(), "Startup Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
