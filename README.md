# 🏦 Dhaka University Bank Management System

A professional desktop banking application built with Java Swing and SQLite, featuring ATM card management, account operations, and secure admin controls.

## 🚀 Features

### Customer Features
- **Account Management**
  - Create and manage bank accounts
  - View transaction history
  - Check account balance
  - Update personal information
  - Change PIN
  - Deposit/Withdraw funds

### ATM Features
- ATM Card Management
- Issue new ATM cards
- View existing cards
- ATM transactions (with PIN verification)
- Balance inquiry

### Admin Features
- Employee Dashboard
- Customer Service Portal
- Account verification
- Transaction monitoring

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 11+ | Core application |
| Swing | Built-in | GUI Framework |
| SQLite | 3.42.0.0 | Database (default) |
| MySQL | 8.0.33 | Optional database |
| Maven | 3.9+ | Build automation |

## 📋 Prerequisites

- **Java**: JDK 11 or higher
- **Maven**: 3.6 or higher
- **Git**: For version control
- **Database**: SQLite (included) or MySQL (optional)

## 🔧 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Ariful-Islam1011/OOP_Project_Bank.git
cd OOP_Project_Bank
```

### 2. Install Dependencies

Maven will automatically download required dependencies from `pom.xml`:
- MySQL Connector/J
- SQLite JDBC

### 3. Build the Project

```bash
mvn clean compile
```

## ▶️ Running the Application

### Quick Start (All Platforms)

```bash
bash run.sh    # macOS/Linux
run.bat        # Windows
```

Or manually:

```bash
mvn clean compile exec:java
```

### Default Credentials

**Admin Login:**
- Bank Number: `192117475354`
- Password: `DhakaUniversity`

**Database:** SQLite (`bank.db`) is created automatically on first run.

## 📁 Project Structure

```
OOP_Project_Bank/
├── src/
│   ├── App.java                      # Application entry point
│   ├── LoginFrame.java               # Login interface (Bank Logo in ATM section)
│   ├── EmployeeFrame.java            # Admin dashboard
│   ├── ATMLoginFrame.java            # ATM login
│   ├── ATMCardManagementFrame.java    # ATM card management
│   ├── AccountDetailFrame.java        # Account details
│   ├── NewAccountForm1.java           # Account creation
│   ├── ExistingAccountsFrame.java     # Account browsing
│   ├── BalanceTransferFrame.java      # Fund transfers
│   ├── DBHelper.java                  # Database initialization
│   ├── Account.java                   # Data model
│   ├── TransactionRecord.java         # Transaction model
│   ├── UIUtils.java                   # UI utilities
│   └── ...
├── Icon/                             # Application assets
├── lib/                              # External libraries
├── pom.xml                           # Maven configuration
├── run.sh / run.bat                  # Quick start scripts
└── README.md                         # This file
```

## 💾 Database Options

### SQLite (Default)
- **No setup required**
- **Location:** `bank.db` in project root
- **Automatic initialization** on first run

### MySQL (Optional)

1. **Install MySQL:**
   ```bash
   # macOS
   brew install mysql
   
   # Or download from dev.mysql.com
   ```

2. **Configure in code** (see `DBHelper.java`):
   ```java
   // MySQL connection
   String url = "jdbc:mysql://localhost:3306/bank_db";
   String user = "root";
   String password = "your_password";
   ```

3. **Run database setup:**
   ```bash
   mysql -u root -p < setup_database.sql
   ```

## 🔐 Security Notes

⚠️ **Important:** This is a prototype/educational project.

- **PINs are stored in plain text** - Use proper hashing (bcrypt/argon2) in production
- **No encryption** on sensitive data - Implement TLS/SSL for production
- **Demo credentials included** - Change immediately in production
- **Database credentials hardcoded** - Use environment variables in production

## 🎨 UI/UX Features

### Modern Design Elements
- **Glass card panels** with transparency effects
- **Professional color scheme** (cyan, green, blue accents)
- **Responsive layouts** that adapt to window sizes
- **Integrated backgrounds** with bank-themed imagery
- **Professional icons and logos** including Bank Logo in ATM section
- **Smooth transitions** between screens

## 📊 Sample Operations

### Create a New Account

1. Click "Proceed" on login screen
2. Fill in account details
3. Submit form
4. Account created with unique account number

### Issue ATM Card

1. Login as Admin
2. Navigate to "Customer Service" → "ATM Card Management"
3. Select "Issue New Card"
4. Choose account and set PIN
5. Card generated and ready to use

### Make a Transaction

1. Login with ATM card
2. Select "Proceed to ATM"
3. Enter PIN
4. Choose transaction type (Withdraw/Deposit/Balance)
5. Complete transaction

## 🐛 Troubleshooting

### Maven Build Issues
```bash
# Clean and rebuild
mvn clean compile
mvn clean compile exec:java
```

### Database Connection Error
- Verify SQLite is available: Check `bank.db` in project root
- For MySQL: Ensure MySQL server is running
- Check database credentials in `DBHelper.java`

### Java Version Error
```bash
# Check Java version
java -version

# Ensure Java 11+ is installed
# macOS: brew install openjdk@11
```

## 📝 Configuration Files

| File | Purpose |
|------|---------|
| `pom.xml` | Maven dependencies and build config |
| `setup_database.sql` | MySQL database schema |
| `create_tables_mysql.sql` | MySQL table definitions |
| `.gitignore` | Git exclusion rules (clean repository) |
| `run.sh / run.bat` | Platform-specific run scripts |

## 🤝 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is provided as-is for educational purposes.

## 👨‍💻 Author & Contact

**Ariful Islam**
- GitHub: [@Ariful-Islam1011](https://github.com/Ariful-Islam1011)
- Repository: [OOP_Project_Bank](https://github.com/Ariful-Islam1011/OOP_Project_Bank)

## 🏫 Educational Purpose

Developed for learning Object-Oriented Programming principles and GUI development with Java Swing. This project demonstrates:
- MVC architectural pattern
- GUI framework implementation
- Database integration
- Event handling
- Professional code organization

---

**Last Updated:** January 4, 2026  
**Status:** Active Development ✅  
**Repository:** Clean, Professional Structure

For issues, feature requests, and feedback, please open a GitHub issue in the repository.
