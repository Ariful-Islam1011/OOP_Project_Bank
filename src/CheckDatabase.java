import java.sql.*;

public class CheckDatabase {
    public static void main(String[] args) {
        System.out.println("🔍 Checking Database Connection and Data...\n");
        
        // Try MySQL first (with your creatives database)
        checkMySQLDatabase();
        
        // Then check SQLite
        System.out.println("\n" + "=".repeat(60));
        checkSQLiteDatabase();
    }
    
    private static void checkMySQLDatabase() {
        System.out.println("📊 MYSQL DATABASE (creatives):");
        System.out.println("-".repeat(60));
        
        String url = "jdbc:mysql://localhost:3306/creatives?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "17475354";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            
            System.out.println("✅ Connected to MySQL - creatives database\n");
            
            // Check accounts
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) as count FROM accounts");
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("📋 Total Accounts: " + count);
            }
            
            // Check transactions
            rs = st.executeQuery("SELECT COUNT(*) as count FROM transactions");
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("💰 Total Transactions: " + count);
            }
            
            // Check transfers
            rs = st.executeQuery("SELECT COUNT(*) as count FROM transfers");
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("🔄 Total Transfers: " + count);
            }
            
            // Check ATM cards
            rs = st.executeQuery("SELECT COUNT(*) as count FROM atm_cards");
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("🎫 Total ATM Cards: " + count);
            }
            
            // Display account details
            System.out.println("\n📌 Account Details:");
            rs = st.executeQuery("SELECT account_number, name, mobile, balance FROM accounts LIMIT 10");
            while (rs.next()) {
                System.out.println("  • Account: " + rs.getString("account_number") + 
                                 " | Name: " + rs.getString("name") + 
                                 " | Mobile: " + rs.getString("mobile") + 
                                 " | Balance: ৳" + rs.getDouble("balance"));
            }
            
            conn.close();
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ MySQL Connection Error: " + e.getMessage());
        }
    }
    
    private static void checkSQLiteDatabase() {
        System.out.println("📊 SQLITE DATABASE (bank.db):");
        System.out.println("-".repeat(60));
        
        String url = "jdbc:sqlite:bank.db";
        
        try {
            Connection conn = DriverManager.getConnection(url);
            
            System.out.println("✅ Connected to SQLite - bank.db\n");
            
            Statement st = conn.createStatement();
            
            // Check accounts
            ResultSet rs = st.executeQuery("SELECT COUNT(*) as count FROM accounts");
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("📋 Total Accounts: " + count);
            }
            
            // Check transactions
            rs = st.executeQuery("SELECT COUNT(*) as count FROM transactions");
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("💰 Total Transactions: " + count);
            }
            
            // Check transfers
            rs = st.executeQuery("SELECT COUNT(*) as count FROM transfers");
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("🔄 Total Transfers: " + count);
            }
            
            // Check ATM cards
            rs = st.executeQuery("SELECT COUNT(*) as count FROM atm_cards");
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("🎫 Total ATM Cards: " + count);
            }
            
            conn.close();
            
        } catch (SQLException e) {
            System.out.println("❌ SQLite Connection Error: " + e.getMessage());
        }
    }
}
