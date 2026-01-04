@echo off
echo Starting Bank Management System...

REM Check if Maven is available
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Using Maven to run the project...
    mvn clean compile exec:java
) else (
    echo Maven not found. Using javac...
    
    REM Create classes directory
    if not exist target\classes mkdir target\classes
    
    REM Compile all Java files
    echo Compiling Java files...
    javac -cp "lib/*" -d target/classes src/*.java
    
    REM Run the application
    echo Running Bank Application...
    java -cp "target/classes;lib/*" App
)

pause
