# Dhaka University Bank - Database Configuration Guide

## 🗄️ Database Settings

### Current Configuration
- **Database Type:** SQLite (Default) / MySQL (Optional)
- **SQLite Database File:** `bank.db` (in project root)
- **MySQL Database Name:** `creatives`
- **MySQL User:** `root`
- **MySQL Password:** `17475354`
- **MySQL Host:** `localhost`
- **MySQL Port:** `3306`

## 🔄 How to Switch Databases

### Use SQLite (Recommended for Testing)
```bash
cd "/Users/md.arifulislam/Documents/Object_Oriented Programing /Project/Bank"
DB_USE_MYSQL=false mvn -q exec:java -Dexec.mainClass="App"
```

### Use MySQL (For Production/Existing Data)
```bash
cd "/Users/md.arifulislam/Documents/Object_Oriented Programing /Project/Bank"
DB_USE_MYSQL=true DB_HOST=localhost DB_PORT=3306 DB_NAME=creatives DB_USER=root DB_PASS=17475354 mvn -q exec:java -Dexec.mainClass="App"
```

Or add to ~/.zshrc:
```bash
alias runbank_mysql='cd "/Users/md.arifulislam/Documents/Object_Oriented Programing /Project/Bank" && DB_USE_MYSQL=true DB_HOST=localhost DB_PORT=3306 DB_NAME=creatives DB_USER=root DB_PASS=17475354 mvn -q exec:java -Dexec.mainClass="App"'
```

## 📊 Database Tables

### accounts
- `id` - Auto-increment ID
- `account_number` - Unique account number (DU format)
- `name` - Account holder name
- `father` - Father's name
- `mother` - Mother's name
- `dob` - Date of birth
- `gender` - Gender
- `mobile` - Mobile number
- `address` - Address
- `nid` - National ID
- `account_type` - Type of account
- `pin` - Account PIN
- `signature_path` - Path to signature image
- `profile_image_path` - Path to profile picture
- `balance` - Current balance
- `created_at` - Account creation date

### transactions
- `id` - Auto-increment ID
- `account_number` - Account number
- `type` - Transaction type (Deposit/Withdraw/Transfer)
- `amount` - Transaction amount
- `balance` - Balance after transaction
- `timestamp` - Transaction time
- `serial_number` - Unique serial number (SN-timestamp-random)

### transfers
- `id` - Auto-increment ID
- `from_account` - Sender account number
- `to_account` - Receiver account number
- `amount` - Transfer amount
- `timestamp` - Transfer time
- `status` - Transfer status (Completed/Pending)
- `remarks` - Additional remarks
- `serial_number` - Unique serial number

### atm_cards
- `id` - Auto-increment ID
- `card_number` - Card number (16 digits)
- `account_number` - Associated account
- `cardholder_name` - Name on card
- `issue_date` - Card issue date
- `expiry_date` - Card expiry date
- `status` - Card status (Active/Expired/Cancelled)
- `pin` - Card PIN (4 digits)

## ✅ Checking Your Old Data

### If You Have MySQL "creatives" Database:
Your old data is stored in the MySQL database. To access it:

1. Make sure MySQL is running
2. Update the password in the configuration if different
3. Run with MySQL enabled:
```bash
DB_USE_MYSQL=true DB_PASS=17475354 mvn -q exec:java -Dexec.mainClass="App"
```

### If You Have SQLite "bank.db" File:
Your old data is in the SQLite database. To restore:

1. Copy your old `bank.db` file to the project root
2. Run with SQLite (default):
```bash
mvn -q exec:java -Dexec.mainClass="App"
```

## 🔧 Creating Test Data

When you create a new account in the application:
1. Click "New Account" in the Employee Panel
2. Fill in all details
3. The account is automatically created with an account number starting with "DU"

### Test Login Credentials
- **Admin Login:** 
  - Password: `DhakaUniversity`
  - Bank Number: `192117475354`

## 📝 Transfer Feature Details

- **From Account:** Your current account
- **To Account:** Recipient's account number (must exist)
- **Amount:** Transfer amount (must be positive, ≤ your balance)
- **Serial Number:** Auto-generated (SN-[timestamp]-[random])
- **Transaction Record:** Automatically recorded in transactions table

## ⚠️ Important Notes

1. **Database Reset:** Deleting `bank.db` will reset all SQLite data
2. **MySQL Connection:** Requires MySQL server to be running
3. **Password:** Default password is `17475354` - change if different
4. **Backup:** Always backup your MySQL database before major changes

## 🚀 To Use with Your Existing "creatives" Database:

Run this command:
```bash
cd "/Users/md.arifulislam/Documents/Object_Oriented Programing /Project/Bank"
DB_USE_MYSQL=true DB_HOST=localhost DB_PORT=3306 DB_NAME=creatives DB_USER=root DB_PASS=17475354 mvn -q exec:java -Dexec.mainClass="App"
```

This will connect to your existing MySQL database and load all your old account data!
