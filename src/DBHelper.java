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
        private static final String MYSQL_DB = java.util.Optional.ofNullable(System.getenv().get("DB_NAME")).orElse("creatives");
        private static final String MYSQL_USER = java.util.Optional.ofNullable(System.getenv().get("DB_USER")).orElse("root");
        private static final String MYSQL_PASS = java.util.Optional.ofNullable(System.getenv().get("DB_PASS")).orElse("17475354");

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
            String transfers;
            String atmCards;
            if (USE_MYSQL) {
                accounts = "CREATE TABLE IF NOT EXISTS accounts ("
                        + "id INT PRIMARY KEY AUTO_INCREMENT,"
                        + "account_number VARCHAR(64) UNIQUE,"
                        + "name VARCHAR(255), father VARCHAR(255), mother VARCHAR(255), dob VARCHAR(50), gender VARCHAR(20), mobile VARCHAR(50), address VARCHAR(512), nid VARCHAR(100),"
                        + "account_type VARCHAR(100), pin VARCHAR(100), signature_path VARCHAR(512), profile_image_path VARCHAR(512), balance DOUBLE DEFAULT 0, created_at DATETIME"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

                transactions = "CREATE TABLE IF NOT EXISTS transactions ("
                        + "id INT PRIMARY KEY AUTO_INCREMENT,"
                        + "account_number VARCHAR(64), type VARCHAR(100), amount DOUBLE, balance DOUBLE, timestamp DATETIME, serial_number VARCHAR(64) UNIQUE"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

                transfers = "CREATE TABLE IF NOT EXISTS transfers ("
                        + "id INT PRIMARY KEY AUTO_INCREMENT,"
                        + "from_account VARCHAR(64), to_account VARCHAR(64), amount DOUBLE, timestamp DATETIME, status VARCHAR(50), remarks TEXT, serial_number VARCHAR(64) UNIQUE"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

                atmCards = "CREATE TABLE IF NOT EXISTS atm_cards ("
                        + "id INT PRIMARY KEY AUTO_INCREMENT,"
                        + "card_number VARCHAR(64) UNIQUE, account_number VARCHAR(64), cardholder_name VARCHAR(255), issue_date DATETIME, expiry_date VARCHAR(50), status VARCHAR(50), pin VARCHAR(100)"
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
                        + "account_number TEXT, type TEXT, amount REAL, balance REAL, timestamp TEXT, serial_number TEXT UNIQUE"
                        + ")";

                transfers = "CREATE TABLE IF NOT EXISTS transfers ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "from_account TEXT, to_account TEXT, amount REAL, timestamp TEXT, status TEXT, remarks TEXT, serial_number TEXT UNIQUE"
                        + ")";

                atmCards = "CREATE TABLE IF NOT EXISTS atm_cards ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "card_number TEXT UNIQUE, account_number TEXT, cardholder_name TEXT, issue_date TEXT, expiry_date TEXT, status TEXT, pin TEXT"
                        + ")";
            }
            st.execute(accounts);
            st.execute(transactions);
            st.execute(transfers);
            st.execute(atmCards);
            
            // Ensure serial_number column exists by adding it if missing
            try {
                String checkColumn = USE_MYSQL ? 
                    "ALTER TABLE transactions ADD COLUMN serial_number VARCHAR(64) UNIQUE" :
                    "ALTER TABLE transactions ADD COLUMN serial_number TEXT UNIQUE";
                st.execute(checkColumn);
            } catch (SQLException e) {
                // Column might already exist, ignore silently
            }
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

    public static java.util.List<Account> getAccountsByNID(String nid) throws SQLException {
        java.util.List<Account> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE nid LIKE ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nid + "%");
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
        String serialNumber = generateSerialNumber();
        String sql = "INSERT INTO transactions(account_number,type,amount,balance,timestamp,serial_number) VALUES(?,?,?,?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accNum);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setDouble(4, balance);
            ps.setString(5, now);
            ps.setString(6, serialNumber);
            ps.executeUpdate();
        }
    }

    public static String generateSerialNumber() {
        // Generate 4-digit serial number
        int randomNum = (int)(Math.random() * 9000 + 1000);
        return "SN-" + randomNum;
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
                    t.setSerialNumber(rs.getString("serial_number"));
                    list.add(t);
                }
            }
        }
        return list;
    }

    public static java.util.List<TransactionRecord> getTransactionsByType(String accNum, String type) throws SQLException {
        java.util.List<TransactionRecord> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? AND type = ? ORDER BY id DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accNum);
            ps.setString(2, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TransactionRecord t = new TransactionRecord();
                    t.setType(rs.getString("type"));
                    t.setAmount(rs.getDouble("amount"));
                    t.setBalance(rs.getDouble("balance"));
                    t.setTimestamp(rs.getString("timestamp"));
                    t.setSerialNumber(rs.getString("serial_number"));
                    list.add(t);
                }
            }
        }
        return list;
    }

    public static java.util.List<TransactionRecord> getTransactionsBySerialNumber(String accNum, String serialNumber) throws SQLException {
        java.util.List<TransactionRecord> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? AND serial_number LIKE ? ORDER BY id DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accNum);
            ps.setString(2, "%" + serialNumber + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TransactionRecord t = new TransactionRecord();
                    t.setType(rs.getString("type"));
                    t.setAmount(rs.getDouble("amount"));
                    t.setBalance(rs.getDouble("balance"));
                    t.setTimestamp(rs.getString("timestamp"));
                    t.setSerialNumber(rs.getString("serial_number"));
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

    // Transfer methods
    public static String createTransfer(String fromAcc, String toAcc, double amount) throws SQLException {
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String serialNumber = generateSerialNumber();
        String sql = "INSERT INTO transfers(from_account,to_account,amount,timestamp,status,serial_number) VALUES(?,?,?,?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fromAcc);
            ps.setString(2, toAcc);
            ps.setDouble(3, amount);
            ps.setString(4, now);
            ps.setString(5, "Completed");
            ps.setString(6, serialNumber);
            ps.executeUpdate();
        }
        // Update balances
        Account from = getAccountByNumber(fromAcc);
        Account to = getAccountByNumber(toAcc);
        if (from != null && to != null) {
            changeBalance(fromAcc, -amount, "Transfer");
            changeBalance(toAcc, amount, "Transfer");
        }
        return serialNumber;
    }

    public static java.util.List<java.util.Map<String, Object>> getTransfers(String accountNumber) throws SQLException {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE from_account = ? OR to_account = ? ORDER BY id DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.setString(2, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("fromAccount", rs.getString("from_account"));
                    map.put("toAccount", rs.getString("to_account"));
                    map.put("amount", rs.getDouble("amount"));
                    map.put("timestamp", rs.getString("timestamp"));
                    map.put("status", rs.getString("status"));
                    map.put("serialNumber", rs.getString("serial_number"));
                    map.put("type", accountNumber.equals(rs.getString("from_account")) ? "Sent" : "Received");
                    list.add(map);
                }
            }
        }
        return list;
    }

    public static java.util.List<java.util.Map<String, Object>> getAllTransfers() throws SQLException {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM transfers ORDER BY id DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("fromAccount", rs.getString("from_account"));
                    map.put("toAccount", rs.getString("to_account"));
                    map.put("amount", rs.getDouble("amount"));
                    map.put("timestamp", rs.getString("timestamp"));
                    map.put("status", rs.getString("status"));
                    map.put("serialNumber", rs.getString("serial_number"));
                    map.put("type", "Transfer");
                    list.add(map);
                }
            }
        }
        return list;
    }

    // ATM Card methods
    public static String issueATMCard(String accountNumber, String cardholderName) throws SQLException {
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String expiryDate = LocalDateTime.now().plusYears(5).format(DateTimeFormatter.ofPattern("MM/yyyy"));
        String cardNumber = generateCardNumber();
        String pin = generateATMPin();
        String sql = "INSERT INTO atm_cards(card_number,account_number,cardholder_name,issue_date,expiry_date,status,pin) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cardNumber);
            ps.setString(2, accountNumber);
            ps.setString(3, cardholderName);
            ps.setString(4, now);
            ps.setString(5, expiryDate);
            ps.setString(6, "Active");
            ps.setString(7, pin);
            ps.executeUpdate();
        }
        return cardNumber + "|" + pin;
    }

    public static String generateCardNumber() {
        return "4532" + (int)(Math.random() * 900000000 + 100000000) + "" + (int)(Math.random() * 9000 + 1000);
    }

    public static String generateATMPin() {
        return "" + (int)(Math.random() * 9000 + 1000);
    }

    public static java.util.List<java.util.Map<String, Object>> getATMCards(String accountNumber) throws SQLException {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String sql = "SELECT atm.*, ac.mobile FROM atm_cards atm LEFT JOIN accounts ac ON atm.account_number = ac.account_number WHERE atm.account_number = ? ORDER BY atm.id DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("cardNumber", rs.getString("card_number"));
                    map.put("accountNumber", rs.getString("account_number"));
                    map.put("cardholderName", rs.getString("cardholder_name"));
                    map.put("mobile", rs.getString("mobile"));
                    map.put("issueDate", rs.getString("issue_date"));
                    map.put("expiryDate", rs.getString("expiry_date"));
                    map.put("status", rs.getString("status"));
                    list.add(map);
                }
            }
        }
        return list;
    }

    public static java.util.List<java.util.Map<String, Object>> getAllATMCards() throws SQLException {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String sql = "SELECT atm.*, ac.mobile FROM atm_cards atm LEFT JOIN accounts ac ON atm.account_number = ac.account_number ORDER BY atm.id DESC";
        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("cardNumber", rs.getString("card_number"));
                map.put("accountNumber", rs.getString("account_number"));
                map.put("cardholderName", rs.getString("cardholder_name"));
                map.put("mobile", rs.getString("mobile"));
                map.put("issueDate", rs.getString("issue_date"));
                map.put("expiryDate", rs.getString("expiry_date"));
                map.put("status", rs.getString("status"));
                list.add(map);
            }
        }
        return list;
    }

    public static java.util.List<java.util.Map<String, Object>> searchATMCards(String searchType, String searchValue) throws SQLException {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String sql = "";
        if ("name".equalsIgnoreCase(searchType)) {
            sql = "SELECT atm.*, ac.mobile FROM atm_cards atm LEFT JOIN accounts ac ON atm.account_number = ac.account_number WHERE atm.cardholder_name LIKE ? ORDER BY atm.id DESC";
        } else if ("mobile".equalsIgnoreCase(searchType)) {
            sql = "SELECT atm.*, ac.mobile FROM atm_cards atm LEFT JOIN accounts ac ON atm.account_number = ac.account_number WHERE ac.mobile LIKE ? ORDER BY atm.id DESC";
        } else if ("accountNumber".equalsIgnoreCase(searchType)) {
            sql = "SELECT atm.*, ac.mobile FROM atm_cards atm LEFT JOIN accounts ac ON atm.account_number = ac.account_number WHERE atm.account_number = ? ORDER BY atm.id DESC";
        } else if ("cardNumber".equalsIgnoreCase(searchType)) {
            sql = "SELECT atm.*, ac.mobile FROM atm_cards atm LEFT JOIN accounts ac ON atm.account_number = ac.account_number WHERE atm.card_number LIKE ? ORDER BY atm.id DESC";
        }
        
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if ("name".equalsIgnoreCase(searchType) || "mobile".equalsIgnoreCase(searchType) || "cardNumber".equalsIgnoreCase(searchType)) {
                ps.setString(1, "%" + searchValue + "%");
            } else {
                ps.setString(1, searchValue);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("cardNumber", rs.getString("card_number"));
                    map.put("accountNumber", rs.getString("account_number"));
                    map.put("cardholderName", rs.getString("cardholder_name"));
                    map.put("mobile", rs.getString("mobile"));
                    map.put("issueDate", rs.getString("issue_date"));
                    map.put("expiryDate", rs.getString("expiry_date"));
                    map.put("status", rs.getString("status"));
                    list.add(map);
                }
            }
        }
        return list;
    }

    // Update ATM card status (Active/Cancelled)
    public static boolean updateATMCardStatus(String cardNumber, String newStatus) throws SQLException {
        String sql = "UPDATE atm_cards SET status = ? WHERE card_number = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setString(2, cardNumber);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        }
    }
}
