# Bank Management System - Integrated ATM Module

## Overview
This is a comprehensive Bank Management System with integrated ATM functionality. The system combines employee banking operations with customer ATM services.

## New Features Added

### 1. **Customer Service Module**
   - Added "Customer Service" option in Employee Dashboard
   - Provides ATM card issuance functionality

### 2. **ATM Card Issue System**
   - Employee can issue ATM cards to existing account holders
   - Generates unique 16-digit card number
   - Creates secure 4-digit PIN
   - Stores card details in customer's account profile
   - Displays card number and PIN for customer record

### 3. **ATM Login System**
   - New "ATM Login" button on main login page
   - Customers can authenticate using:
     - ATM Card Number
     - PIN
   - Direct access to ATM transaction menu

### 4. **ATM Transaction Menu**
   - **Deposit**: Add funds to account
   - **Cash Withdrawal**: Withdraw money (with balance check)
   - **Balance Enquiry**: View current balance
   - **Mini Statement**: View last 10 transactions
   - **PIN Change**: Update ATM PIN securely
   - **Exit**: Return to login screen

## Database Structure

### Updated `accounts` Table
Added two new columns:
- `atm_card_number` VARCHAR(20) - Stores the 16-digit ATM card number
- `atm_pin` VARCHAR(10) - Stores the 4-digit PIN

## How to Use

### For Employees:

1. **Login as Admin**
   - Use credentials:
     - Admin Password: `DhakaUniversity`
     - Bank Number: `192117475354`

2. **Navigate to Customer Service**
   - Click "Customer Service" button
   - Select "ATM Card Issue"

3. **Issue ATM Card**
   - Enter Account Number
   - Enter Customer Name
   - Enter Mobile Number
   - Click "Issue ATM Card"
   - System generates and displays Card Number and PIN
   - Save these details for the customer

### For Customers (ATM Users):

1. **Access ATM**
   - Click "ATM Login" on main login page

2. **Authenticate**
   - Enter your 16-digit Card Number
   - Enter your 4-digit PIN
   - Click "SIGN IN"

3. **Perform Transactions**
   - **Deposit**: Enter amount to add to your account
   - **Withdrawal**: Enter amount to withdraw (checks balance)
   - **Balance Enquiry**: View your current balance
   - **Mini Statement**: See your last 10 transactions
   - **PIN Change**: Update your PIN (requires old PIN)
   - **Exit**: Log out and return to main screen

## Database Configuration

### MySQL Database
- **Database Name**: `creatives`
- **Username**: `root`
- **Password**: `17475354`
- **Host**: `localhost`
- **Port**: `3306`

## Running the Application

### Prerequisites
- Java 8 or higher
- MySQL 9.0 or higher
- MySQL Connector/J (included in lib/)

### Steps to Run

1. **Ensure MySQL is running**:
   ```bash
   brew services start mysql
   ```

2. **Set environment variable for MySQL**:
   ```bash
   export DB_USE_MYSQL=true
   ```

3. **Compile the project**:
   ```bash
   cd "/Users/md.arifulislam/Documents/Object_Oriented Programing /Project/Bank"
   javac -d bin -cp "lib/*:." src/*.java
   ```

4. **Run the application**:
   ```bash
   java -cp "bin:lib/*" App
   ```

## Project Structure

```
Project/Bank/
├── src/
│   ├── App.java                    # Main application entry point
│   ├── LoginFrame.java             # Main login screen (Updated)
│   ├── EmployeeFrame.java          # Employee dashboard (Updated)
│   ├── CustomerServiceFrame.java   # NEW: Customer service menu
│   ├── ATMCardIssueFrame.java      # NEW: ATM card issuance form
│   ├── ATMLoginFrame.java          # NEW: ATM authentication screen
│   ├── ATMTransactionFrame.java    # NEW: ATM transaction menu
│   ├── DBHelper.java               # Database operations (Updated)
│   ├── Account.java                # Account model
│   ├── TransactionRecord.java      # Transaction model
│   └── ... (other existing files)
├── lib/
│   └── mysql-connector-j.jar       # MySQL JDBC driver
├── bin/                            # Compiled class files
├── Icon/                           # UI images and icons
└── setup_database.sql              # Database initialization script

```

## Security Features

1. **PIN Security**: 4-digit PIN required for all ATM operations
2. **Account Verification**: System verifies account exists before issuing card
3. **Balance Checks**: Withdrawal prevented if insufficient funds
4. **PIN Change**: Requires old PIN verification before updating
5. **Session Management**: Secure login/logout flow

## Testing the System

### Test Flow:

1. **Create an Account** (as Employee)
   - Login as admin
   - Click "New Account"
   - Fill in customer details
   - Account created with account number

2. **Issue ATM Card** (as Employee)
   - Go to "Customer Service" → "ATM Card Issue"
   - Enter the account number
   - Get card number and PIN

3. **Test ATM Operations** (as Customer)
   - Click "ATM Login"
   - Enter card number and PIN
   - Test deposit, withdrawal, balance check
   - Verify mini statement shows transactions

## Troubleshooting

### Common Issues:

1. **MySQL Connection Error**
   - Ensure MySQL is running: `brew services list`
   - Check password is set: `17475354`
   - Verify database exists: `mysql -u root -p17475354 -e "SHOW DATABASES;"`

2. **ATM Card Already Issued**
   - Each account can only have one ATM card
   - Check existing card details in database

3. **Images Not Loading**
   - Application works without images
   - Place images in `/Icon/` folder if needed

## Future Enhancements

- Fast cash options
- Transfer between accounts
- Mobile banking integration
- Email alerts for transactions
- Card blocking/unblocking
- Transaction limits and daily caps

## Credits

Developed for Dhaka University Bank Management System
Integrated ATM Module - December 2025
