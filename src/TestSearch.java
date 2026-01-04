import javax.swing.SwingUtilities;

public class TestSearch {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Launching ExistingAccountsFrame (search page) test");
            javax.swing.JFrame dummy = new javax.swing.JFrame();
            dummy.setSize(200,200);
            dummy.setLocationRelativeTo(null);
            dummy.setVisible(false);
            new ExistingAccountsFrame(dummy).setVisible(true);
        });
    }
}
