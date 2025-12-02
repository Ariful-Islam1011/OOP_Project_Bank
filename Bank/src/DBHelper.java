import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DBHelper {
            // Read DB configuration from environment variables with sensible defaults
            // To use MySQL set environment variable DB_USE_MYSQL=true (default false)
            private static final boolean USE_MYSQL = Boolean.parseBoolean(
                java.util.Optional.ofNullable(System.getenv().get("DB_USE_MYSQL")).orElse("false")
            );

        private static final String MYSQL_HOST = java.util.Optional.ofNullable(System.getenv().get("DB_HOST")).orElse("localhost");
        private static final int MYSQL_PORT = Integer.parseInt(java.util.Optional.ofNullable(System.getenv().get("DB_PORT")).orElse("3306"));
        private static final String MYSQL_DB = java.util.Optional.ofNullable(System.getenv().get("DB_NAME")).orElse("bankdb");
        private static final String MYSQL_USER = java.util.Optional.ofNullable(System.getenv().get("DB_USER")).orElse("root");
        private static final String MYSQL_PASS = java.util.Optional.ofNullable(System.getenv().get("DB_PASS")).orElse("password");

        private static final String MYSQL_URL = "jdbc:mysql://" + MYSQL_HOST + ":" + MYSQL_PORT + "/" + MYSQL_DB
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

        private static final String SQLITE_URL = java.util.Optional.ofNullable(System.getenv().get("SQLITE_URL")).orElse("jdbc:sqlite:bank.db");

    public static Connection getConnection() throws SQLException {
        if (USE_MYSQL) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC driver not found. Add MySQL Connector/J to classpath.", e);
            }
            return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
        } else {
            return DriverManager.getConnection(SQLITE_URL);
        }
    }

    public static void initDB() throws SQLException {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            String accounts;
            String transactions;
            if (USE_MYSQL) {
                accounts = "CREATE TABLE IF NOT EXISTS accounts ("
                        + "id INT PRIMARY KEY AUTO_INCREMENT,"
                        + "account_number VARCHAR(64) UNIQUE,"
                        + "name VARCHAR(255), father VARCHAR(255), mother VARCHAR(255), dob VARCHAR(50), gender VARCHAR(20), mobile VARCHAR(50), address VARCHAR(512), nid VARCHAR(100),"
                        + "account_type VARCHAR(100), pin VARCHAR(100), signature_path VARCHAR(512), profile_image_path VARCHAR(512), balance DOUBLE DEFAULT 0, created_at DATETIME"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

                transactions = "CREATE TABLE IF NOT EXISTS transactions ("
                        + "id INT PRIMARY KEY AUTO_INCREMENT,"
                        + "account_number VARCHAR(64), type VARCHAR(100), amount DOUBLE, balance DOUBLE, timestamp DATETIME"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
            } else {
                accounts = "CREATE TABLE IF NOT EXISTS accounts ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "account_number TEXT UNIQUE,"
                    + "name TEXT, father TEXT, mother TEXT, dob TEXT, gender TEXT, mobile TEXT, address TEXT, nid TEXT,"
                    + "account_type TEXT, pin TEXT, signature_path TEXT, profile_image_path TEXT, balance REAL DEFAULT 0, created_at TEXT"
                    + ")";

                transactions = "CREATE TABLE IF NOT EXISTS transactions ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "account_number TEXT, type TEXT, amount REAL, balance REAL, timestamp TEXT"
                        + ")";
            }
            st.execute(accounts);
            st.execute(transactions);
        }
    }

    public static String createAccount(Account a) throws SQLException {
        String accNum = "DU" + (System.currentTimeMillis() % 10000000L);
        a.setAccountNumber(accNum);
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String sql = "INSERT INTO accounts(account_number,name,father,mother,dob,gender,mobile,address,nid,account_type,pin,signature_path,profile_image_path,balance,created_at)"
            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getAccountNumber());
            ps.setString(2, a.getName());
            ps.setString(3, a.getFather());
            ps.setString(4, a.getMother());
            ps.setString(5, a.getDob());
            ps.setString(6, a.getGender());
            ps.setString(7, a.getMobile());
            ps.setString(8, a.getAddress());
            ps.setString(9, a.getNid());
            ps.setString(10, a.getAccountType());
            ps.setString(11, a.getPin());
            ps.setString(12, a.getSignaturePath());
            ps.setString(13, a.getProfileImagePath());
            ps.setDouble(14, a.getBalance());
            ps.setString(15, now);
            ps.executeUpdate();
        }
        if (a.getBalance() > 0) {
            insertTransaction(accNum, "Deposit", a.getBalance(), a.getBalance());
        }
        return accNum;
    }

    public static Account getAccountByNumber(String accNum) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accNum);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account a = new Account();
                    a.setAccountNumber(rs.getString("account_number"));
                    a.setName(rs.getString("name"));
                    a.setFather(rs.getString("father"));
                    a.setMother(rs.getString("mother"));
                    a.setDob(rs.getString("dob"));
                    a.setGender(rs.getString("gender"));
                    a.setMobile(rs.getString("mobile"));
                    a.setAddress(rs.getString("address"));
                    a.setNid(rs.getString("nid"));
                    a.setAccountType(rs.getString("account_type"));
                    a.setPin(rs.getString("pin"));
                    a.setSignaturePath(rs.getString("signature_path"));
                    try {
                        a.setProfileImagePath(rs.getString("profile_image_path"));
                    } catch (Exception ignore) { }
                    a.setBalance(rs.getDouble("balance"));
                    return a;
                }
            }
        }
        return null;
    }

    public static java.util.List<Account> getAccountsByType(String type) throws SQLException {
        java.util.List<Account> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE account_type = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Account a = new Account();
                    a.setAccountNumber(rs.getString("account_number"));
                    a.setName(rs.getString("name"));
                    a.setBalance(rs.getDouble("balance"));
                    a.setAccountType(rs.getString("account_type"));
                    list.add(a);
                }
            }
        }
        return list;
    }

    public static java.util.List<Account> getAccountsByName(String name) throws SQLException {
        java.util.List<Account> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE name LIKE ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Account a = new Account();
                    a.setAccountNumber(rs.getString("account_number"));
                    a.setName(rs.getString("name"));
                    a.setBalance(rs.getDouble("balance"));
                    a.setAccountType(rs.getString("account_type"));
                    list.add(a);
                }
            }
        }
        return list;
    }

    public static java.util.List<Account> getAccountsByMobile(String mobile) throws SQLException {
        java.util.List<Account> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE mobile LIKE ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + mobile + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Account a = new Account();
                    a.setAccountNumber(rs.getString("account_number"));
                    a.setName(rs.getString("name"));
                    a.setBalance(rs.getDouble("balance"));
                    a.setAccountType(rs.getString("account_type"));
                    list.add(a);
                }
            }
        }
        return list;
    }

    public static java.util.List<Account> getAllAccounts() throws SQLException {
        java.util.List<Account> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY id DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Account a = new Account();
                    a.setAccountNumber(rs.getString("account_number"));
                    a.setName(rs.getString("name"));
                    a.setBalance(rs.getDouble("balance"));
                    a.setAccountType(rs.getString("account_type"));
                    list.add(a);
                }
            }
        }
        return list;
    }

    public static void insertTransaction(String accNum, String type, double amount, double balance) throws SQLException {
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String sql = "INSERT INTO transactions(account_number,type,amount,balance,timestamp) VALUES(?,?,?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accNum);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setDouble(4, balance);
            ps.setString(5, now);
            ps.executeUpdate();
        }
    }

    public static java.util.List<TransactionRecord> getTransactions(String accNum, int limit) throws SQLException {
        java.util.List<TransactionRecord> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY id DESC LIMIT ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accNum);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TransactionRecord t = new TransactionRecord();
                    t.setType(rs.getString("type"));
                    t.setAmount(rs.getDouble("amount"));
                    t.setBalance(rs.getDouble("balance"));
                    t.setTimestamp(rs.getString("timestamp"));
                    list.add(t);
                }
            }
        }
        return list;
    }

    public static boolean changeBalance(String accNum, double delta, String txType) throws SQLException {
        Account a = getAccountByNumber(accNum);
        if (a == null) return false;
        double newBal = a.getBalance() + delta;
        if (newBal < 0) return false;
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBal);
            ps.setString(2, accNum);
            ps.executeUpdate();
        }
        insertTransaction(accNum, txType, Math.abs(delta), newBal);
        return true;
    }

    public static boolean changePin(String accNum, String newPin) throws SQLException {
        String sql = "UPDATE accounts SET pin = ? WHERE account_number = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPin);
            ps.setString(2, accNum);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateAccount(Account a) throws SQLException {
        String sql = "UPDATE accounts SET name=?, father=?, mother=?, dob=?, gender=?, mobile=?, address=?, nid=?, account_type=?, pin=?, signature_path=?, profile_image_path=?, balance=? WHERE account_number = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getName());
            ps.setString(2, a.getFather());
            ps.setString(3, a.getMother());
            ps.setString(4, a.getDob());
            ps.setString(5, a.getGender());
            ps.setString(6, a.getMobile());
            ps.setString(7, a.getAddress());
            ps.setString(8, a.getNid());
            ps.setString(9, a.getAccountType());
            ps.setString(10, a.getPin());
            ps.setString(11, a.getSignaturePath());
            ps.setString(12, a.getProfileImagePath());
            ps.setDouble(13, a.getBalance());
            ps.setString(14, a.getAccountNumber());
            return ps.executeUpdate() > 0;
        }
    }
}
