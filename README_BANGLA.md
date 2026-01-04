# Bank Management System - চালানোর নিয়ম

## প্রজেক্ট চালানোর উপায়:

### Option 1: Run Script দিয়ে (সবচেয়ে সহজ)

#### macOS/Linux এ:
```bash
chmod +x run.sh
./run.sh
```

#### Windows এ:
```
run.bat
```

### Option 2: Maven দিয়ে (যদি Maven installed থাকে)
```bash
mvn clean compile exec:java
```

### Option 3: Direct Java দিয়ে
```bash
# Compile করুন
javac -cp "lib/*" -d target/classes src/*.java

# Run করুন
java -cp "target/classes:lib/*" App
```

## প্রয়োজনীয় Software:
- Java 11 বা তার উপরের version
- Maven (optional, কিন্তু recommended)

## ফোল্ডার Structure:
- `src/` - সব Java code এখানে
- `lib/` - External libraries (MySQL, SQLite drivers)
- `Icon/` - Application icons
- `pom.xml` - Maven configuration file
- `run.sh` / `run.bat` - Quick run scripts

## Database:
এই project SQLite database ব্যবহার করে যেটা automatically create হয়ে যাবে প্রথমবার run করলে।

## Login Credentials:
Default admin login:
- Employee ID: 1001
- Password: admin123

(Details README.md file এ আছে)
