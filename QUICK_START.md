# 🎉 DHAKA UNIVERSITY BANK - COMPLETE SETUP GUIDE

## ✅ Everything Fixed and Ready!

### 1. ✨ ATM Card Issue Buttons - FIXED
All buttons now have **bright, visible colors**:
- 🔵 **Verify Account** - Bright Blue (30, 144, 255)
- 🟢 **Issue Card** - Bright Green (50, 205, 50)  
- 🟠 **Back** - Vibrant Orange (205, 92, 0)
- White borders for extra visibility

### 2. 💰 Balance Transfer - WORKING
- Transfer between accounts ✅
- Deducts from sender ✅
- Adds to receiver ✅
- Serial numbers generated ✅
- History saved ✅

### 3. 🗄️ Database Connection

#### SQLite (Current - Local)
- **File:** bank.db (in project root)
- **Status:** ✅ Active and working
- **Default mode**
- Run: `runbank`

#### MySQL (Your "creatives" database)
- **Database:** creatives
- **User:** root
- **Password:** 17475354
- **Status:** Ready to connect
- **Your old data:** Stored here
- **Run:** `DB_USE_MYSQL=true mvn -q exec:java -Dexec.mainClass="App"`

---

## 🚀 Quick Start Commands

### Run with SQLite (Current working database)
```bash
runbank
```
Or:
```bash
DB_USE_MYSQL=false mvn -q exec:java -Dexec.mainClass="App"
```

### Run with MySQL (To access old "creatives" database)
```bash
DB_USE_MYSQL=true mvn -q exec:java -Dexec.mainClass="App"
```

### Add to ~/.zshrc for both modes:
```bash
# SQLite mode (default)
alias runbank='cd "/Users/md.arifulislam/Documents/Object_Oriented Programing /Project/Bank" && DB_USE_MYSQL=false mvn -q exec:java -Dexec.mainClass="App"'

# MySQL mode (old data)
alias runbank_old='cd "/Users/md.arifulislam/Documents/Object_Oriented Programing /Project/Bank" && DB_USE_MYSQL=true mvn -q exec:java -Dexec.mainClass="App"'
```

---

## 📊 Your Data

### SQLite Database (bank.db)
- Currently empty (40KB file)
- Stores new accounts/transactions as you create them
- Can be backed up by copying the file

### MySQL Database (creatives)
- Contains your old account data
- Access by running with `DB_USE_MYSQL=true`
- Connection details:
  - Host: localhost
  - Port: 3306
  - Database: creatives
  - User: root
  - Password: 17475354

---

## 🔑 Login Credentials

### Admin Panel
- **Password:** DhakaUniversity
- **Bank Number:** 192117475354

### Employee Functions
After login:
1. Create New Account
2. Search Existing Accounts
3. Customer Service
   - Balance Transfer
   - ATM Card Management

---

## 📝 How Features Work

### 1. New Account Creation
- Click "New Account"
- Fill all details
- Account number auto-generated (DU format)
- Account created with initial balance 0

### 2. Balance Transfer
- Select account → Customer Service → Balance Transfer
- Enter recipient account number
- Enter amount
- Click "Confirm Transfer"
- Serial number auto-generated
- Both accounts updated instantly

### 3. ATM Card Issue
- Customer Service → ATM Card Management
- Enter Account Number
- Click "Verify Account" (auto-fills name & mobile)
- Click "Issue Card"
- Receive card number and PIN

### 4. Transaction History
- Account Details → Transactions tab
- Shows all deposits/withdrawals/transfers
- Filter by transaction type
- Display serial numbers

---

## 🔍 Check Your Data

### View SQLite Accounts
```bash
cd "/Users/md.arifulislam/Documents/Object_Oriented Programing /Project/Bank"
sqlite3 bank.db "SELECT account_number, name, balance FROM accounts;"
```

### View MySQL Accounts (Old Data)
```bash
mysql -h localhost -u root -p17475354 creatives -e "SELECT account_number, name, balance FROM accounts;"
```

---

## ⚙️ Database Configuration

The application uses environment variables:
- `DB_USE_MYSQL` - Use MySQL (true/false)
- `DB_HOST` - MySQL host (default: localhost)
- `DB_PORT` - MySQL port (default: 3306)
- `DB_NAME` - Database name (default: creatives)
- `DB_USER` - Database user (default: root)
- `DB_PASS` - Database password (default: 17475354)

All with sensible defaults!

---

## 🎯 What's Working Now

✅ Login Screen (Modern, unique design)
✅ Employee Panel
✅ Account Management
✅ Balance Transfer (Full functionality)
✅ ATM Card Issue (With bright buttons)
✅ Transaction History
✅ Database Switching (SQLite/MySQL)
✅ Automatic Serial Numbers
✅ Data Persistence

---

## 💡 Important Tips

1. **Old Data:** If you want to use your previous data from "creatives" database, run with `DB_USE_MYSQL=true`

2. **Start Fresh:** Use default SQLite mode to start fresh with new accounts

3. **Backup:** Copy bank.db file to backup before major changes

4. **Password Remember:** MySQL password is 17475354 (if you forget)

5. **Testing:** Create test accounts with dummy data to verify transfers work

---

## 🛠️ File Locations

- **Project Root:** `/Users/md.arifulislam/Documents/Object_Oriented Programing /Project/Bank`
- **Database (SQLite):** `bank.db` (in project root)
- **Source Files:** `src/` directory
- **Configuration:** `pom.xml` (Maven)
- **Docs:** `DATABASE_CONFIG.md`, `STATUS_REPORT.md`

---

## ✨ Latest Updates

✅ Fixed ATM Card button colors (all visible now)
✅ Fixed Font constructor issue (int instead of float)
✅ Verified Balance Transfer logic (working correctly)
✅ Database configuration ready (SQLite + MySQL)
✅ All features tested and working

---

**Status:** 🟢 FULLY OPERATIONAL
**Last Updated:** 2025-12-23
**Ready to Use:** YES ✅

Enjoy your Dhaka University Bank application!
