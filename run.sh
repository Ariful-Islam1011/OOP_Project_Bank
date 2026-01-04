#!/bin/bash

echo "Starting Bank Management System..."

# Check if Maven is available
if command -v mvn >/dev/null 2>&1; then
    echo "Using Maven to run the project..."
    mvn clean compile exec:java
else
    echo "Maven not found. Using javac..."
    
    # Create classes directory
    mkdir -p target/classes
    
    # Compile all Java files
    echo "Compiling Java files..."
    javac -cp "lib/*" -d target/classes src/*.java
    
    # Run the application
    echo "Running Bank Application..."
    java -cp "target/classes:lib/*" App
fi
