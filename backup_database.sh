#!/usr/bin/env zsh
# Bank Database Backup Script

BACKUP_DIR="$HOME/BankData/backups"
DB_FILE="$HOME/BankData/bank.db"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="$BACKUP_DIR/bank_backup_$TIMESTAMP.db"

# Create backup directory if not exists
mkdir -p "$BACKUP_DIR"

# Check if database exists
if [ ! -f "$DB_FILE" ]; then
    echo "❌ Error: Database file not found at $DB_FILE"
    exit 1
fi

# Create backup
cp "$DB_FILE" "$BACKUP_FILE"

if [ $? -eq 0 ]; then
    echo "✅ Backup successful!"
    echo "📁 Backup file: $BACKUP_FILE"
    
    # Show database stats
    echo "\n📊 Database Statistics:"
    sqlite3 "$DB_FILE" "
        SELECT '   Accounts: ' || COUNT(*) FROM accounts;
        SELECT '   ATM Cards: ' || COUNT(*) FROM atm_cards;
        SELECT '   Transactions: ' || COUNT(*) FROM transactions;
    "
    
    # Show backup size
    SIZE=$(du -h "$BACKUP_FILE" | cut -f1)
    echo "💾 Backup size: $SIZE"
    
    # Keep only last 10 backups
    echo "\n🗑️  Cleaning old backups (keeping last 10)..."
    ls -t "$BACKUP_DIR"/bank_backup_*.db | tail -n +11 | xargs rm -f 2>/dev/null
    
    REMAINING=$(ls -1 "$BACKUP_DIR"/bank_backup_*.db 2>/dev/null | wc -l)
    echo "📦 Total backups: $REMAINING"
else
    echo "❌ Backup failed!"
    exit 1
fi
