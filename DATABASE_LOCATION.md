# 🏦 Bank Database Location - গুরুত্বপূর্ণ তথ্য

## ⚠️ সতর্কতা - Data Loss Prevention

এই Bank application এর সব data **একটি মাত্র central database** এ সংরক্ষিত হয়:

### 📍 Database Location
```
~/BankData/bank.db
```
অথবা full path:
```
/Users/md.arifulislam/BankData/bank.db
```

## ✅ কেন এই পরিবর্তন?

**আগের সমস্যা:**
- প্রতিটা project folder এ আলাদা আলাদা `bank.db` file তৈরি হতো
- এক folder এ ATM card issue করলে অন্য folder থেকে দেখা যেত না
- Transaction history বার বার হারিয়ে যেত
- Customer data duplicate হয়ে যেত

**এখন সমাধান:**
- ✅ সব project folder একই database ব্যবহার করে
- ✅ যেকোনো folder থেকে run করলেও একই data দেখাবে
- ✅ ATM card, transactions, accounts সব একই জায়গায়
- ✅ Data কখনো হারাবে না

## 🚀 কিভাবে ব্যবহার করবেন?

সবসময় এই command দিয়ে run করুন:
```bash
runbank
```

এটা automatically central database ব্যবহার করবে।

## 🔍 Database Check করতে চাইলে

```bash
# Check ATM cards
sqlite3 ~/BankData/bank.db "SELECT * FROM atm_cards;"

# Check transactions
sqlite3 ~/BankData/bank.db "SELECT * FROM transactions ORDER BY id DESC LIMIT 10;"

# Check accounts
sqlite3 ~/BankData/bank.db "SELECT account_number, name, balance FROM accounts;"
```

## 💾 Backup নিতে চাইলে

```bash
# Backup create
cp ~/BankData/bank.db ~/BankData/bank_backup_$(date +%Y%m%d).db

# Backup list
ls -lh ~/BankData/bank_backup_*.db
```

## 🔄 যদি Database Restore করতে হয়

```bash
# Specific backup restore
cp ~/BankData/bank_backup_20251224.db ~/BankData/bank.db
```

## 📊 বর্তমান Data (24 Dec 2025)

- **Total Accounts:** 2
  - DU985771 (Ariful) - Balance: 98,000 BDT
  - DU4521714 (Azim) - Balance: 60,000 BDT

- **Total ATM Cards:** 1
  - Card: 45321613319311108 (Ariful)

- **Total Transactions:** 7

---

**মনে রাখবেন:** এখন থেকে আপনার সব bank data safe এবং একই জায়গায়! 🎉
