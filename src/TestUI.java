import javax.swing.SwingUtilities;

public class TestUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Launching EmployeeFrame test (no DB)");
            new EmployeeFrame().setVisible(true);
        });
    }
}
