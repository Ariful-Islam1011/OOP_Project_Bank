#!/bin/bash

# Dhaka University Bank - Database Information Script

echo "🔍 DHAKA UNIVERSITY BANK - DATABASE INFORMATION"
echo "=============================================="
echo ""

# Check SQLite Database
echo "📊 SQLite Database (bank.db):"
echo "-------------------------------------"
if [ -f "bank.db" ]; then
    echo "✅ File exists at: $(pwd)/bank.db"
    echo "File size: $(du -h bank.db | cut -f1)"
    echo ""
    echo "To view data in SQLite:"
    echo "  sqlite3 bank.db '.tables'"
    echo "  sqlite3 bank.db 'SELECT COUNT(*) FROM accounts;'"
    echo "  sqlite3 bank.db 'SELECT account_number, name, balance FROM accounts;'"
else
    echo "❌ File not found (will be created on first run)"
fi

echo ""
echo ""

# MySQL Database Info
echo "🗄️  MySQL Database (creatives):"
echo "-------------------------------------"
echo "Connection Details:"
echo "  Host: localhost"
echo "  Port: 3306"
echo "  Database: creatives"
echo "  User: root"
echo "  Password: 17475354"
echo ""
echo "To connect and check data:"
echo "  mysql -h localhost -u root -pcreatives -e 'USE creatives; SHOW TABLES; SELECT COUNT(*) FROM accounts;'"
echo ""
echo "Or run application with MySQL:"
echo "  DB_USE_MYSQL=true DB_PASS=17475354 mvn -q exec:java -Dexec.mainClass=\"App\""

echo ""
echo ""
echo "✨ Application Commands:"
echo "=============================================="
echo ""
echo "SQLite Mode (Default):"
echo "  runbank"
echo "  (or: DB_USE_MYSQL=false mvn -q exec:java -Dexec.mainClass=\"App\")"
echo ""
echo "MySQL Mode (with your creatives database):"
echo "  DB_USE_MYSQL=true mvn -q exec:java -Dexec.mainClass=\"App\""
echo ""
