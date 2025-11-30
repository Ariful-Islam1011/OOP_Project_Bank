# Dhaka University Bank - Java + SQLite (minimal)

This project is a simple desktop bank management prototype implemented in Java (Swing) and SQLite.

Files of interest (in `src/`):
- `App.java` - application launcher (starts `LoginFrame` and initializes DB)
- `DBHelper.java` - SQLite DB initialization and helper methods
- `Account.java`, `TransactionRecord.java` - data models
- `LoginFrame.java` - welcome + admin login
- `EmployeeFrame.java` - employee dashboard
- `NewAccountForm1.java` - signup form v1 (creates accounts)
- `ExistingAccountsFrame.java` - browse/search accounts
- `AccountDetailFrame.java` - personal details, transactions, deposit/withdraw, change PIN

Quick build & run (macOS / zsh):

1) Download `sqlite-jdbc` (one-time). From terminal:

```bash
cd ~/Downloads
curl -L -o sqlite-jdbc.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.42.0.0/sqlite-jdbc-3.42.0.0.jar
```

2) Compile the sources:

```bash
cd /Users/md.arifulislam/Documents/Project/Bank
mkdir -p out
javac -d out src/*.java
```

3) Run the app (include the SQLite JDBC jar on classpath at runtime):

```bash
cd /Users/md.arifulislam/Documents/Project/Bank
java -cp "out:~/Downloads/sqlite-jdbc.jar" App
```

Notes and next steps:
- Default admin credentials are `admin123` and bank number `DU-BANK-001` in `LoginFrame.java`.
- When creating an account you can select a signature image path; the UI displays it later but does not copy the file. Consider copying to a project `signatures/` folder if you want centralized storage.
- PINs are stored in plain text (for prototype). For production you must hash and salt PINs.
- The signup form you mentioned (you will provide 3 variants) is currently implemented as `NewAccountForm1.java`. I can add the other two forms and a switcher to the UI when you provide them.
- `bank.db` will be created in the project root on first run.

MySQL usage:

If you prefer to use MySQL instead of the bundled SQLite file, follow these steps:

- Install MySQL server (macOS: `brew install mysql` or download from dev.mysql.com).
- Create a database and user, or use the provided `create_tables_mysql.sql` to create schema after creating the DB (see file in project root `Bank/create_tables_mysql.sql`).
- Add MySQL Connector/J (the JDBC driver) to the project classpath. Either:
	- Add the JAR in IntelliJ: `File -> Project Structure -> Libraries -> + -> select mysql-connector-j-<version>.jar`.
	- Or place the JAR into the `lib/` folder and include it on the classpath when running from terminal.
- Edit `src/DBHelper.java` to set `USE_MYSQL = true` and update `MYSQL_USER`, `MYSQL_PASS`, `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_DB` to match your environment.

Example: create DB and run SQL (from terminal):

```bash
# create DB (replace <user> with your mysql admin user)
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS bankdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
# load tables
mysql -u root -p bankdb < Bank/create_tables_mysql.sql
```

Running in IntelliJ:

- Make sure the MySQL Connector/J jar is added to Project Libraries (Project Structure -> Libraries).
- Build the project (Build -> Build Project) and run the `App` main class.
- If you get "driver not found" errors, double-check that the Connector/J jar is on the runtime classpath of the run configuration.

If you want, I can convert this project to a Maven or Gradle project so dependency management (including MySQL driver) is handled automatically.

Maven quick start (recommended)

- I added a `pom.xml` to the project root so you can use Maven to download the JDBC drivers and run the app.
- Build and run with Maven from the `Bank/` folder:

```bash
cd /Users/md.arifulislam/Documents/Documents/Project/Bank
# download dependencies and compile
mvn -q compile
# run the App (reads DB config from environment variables)
mvn -q exec:java
```

Environment variables (optional)

Set these environment variables to configure DB connection instead of editing source:

- `DB_USE_MYSQL` (true/false) — toggle MySQL usage (default `true`)
- `DB_HOST` (default `localhost`)
- `DB_PORT` (default `3306`)
- `DB_NAME` (default `bankdb`)
- `DB_USER` (default `root`)
- `DB_PASS` (default `password`)
- `SQLITE_URL` (if you want to change SQLite URL; default `jdbc:sqlite:bank.db`)

Example (macOS / zsh):

```bash
export DB_USE_MYSQL=true
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=bankdb
export DB_USER=root
export DB_PASS=your_mysql_password
mvn -q exec:java
```

If you prefer Gradle instead of Maven tell me and I will create a `build.gradle`.

Images (logo and background)
-----------------------------
To show the Dhaka University building as the login header background and the university logo in the app headers,
place image files in one of these locations (the app will try them in order):

- Background candidates (put the building picture in one of these):
	- `~/Downloads/du_building.jpg`
	- `~/Downloads/dhaka_building.jpg`
	- `~/Downloads/DhakaUniversity_building.jpg`
	- classpath resource `/images/login_bg.jpg` (copy to `images/login_bg.jpg` under the project)

- Logo candidates (put a PNG/JPG next to the SVG or as a raster image):
	- `/Users/md.arifulislam/Downloads/Dhaka_University_logo.svg` (SVG is checked but may not render in Java ImageIO)
	- `/Users/md.arifulislam/Downloads/Dhaka_University_logo.png`
	- classpath resource `/images/logo.png` (copy to `images/logo.png` under the project)

Notes:
- Java's ImageIO does not support SVG by default. If you provide an SVG logo, the app will also try `.png` or `.jpg` variants with the same base name.
- For reliable rendering, provide PNG or JPG files and place them in `~/Downloads` or under the project's `images/` folder and re-run the app.
- If you want me to add the images into the repository and wire them as resources, attach the building JPG and a PNG logo here and I'll commit them under `Bank/images/`.

If you'd like, I can:
- Add the two additional signup forms you mentioned and wire a selector.
- Package everything into an executable JAR with the JDBC driver bundled.
- Implement PIN hashing and image file copying.

Tell me which next step you prefer.
## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
