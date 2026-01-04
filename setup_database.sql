-- Create the 'creatives' database
CREATE DATABASE IF NOT EXISTS creatives;

-- Use the creatives database
USE creatives;

-- Create accounts table
CREATE TABLE IF NOT EXISTS accounts (
  id INT PRIMARY KEY AUTO_INCREMENT,
  account_number VARCHAR(64) UNIQUE,
  name VARCHAR(255),
  father VARCHAR(255),
  mother VARCHAR(255),
  dob VARCHAR(50),
  gender VARCHAR(20),
  mobile VARCHAR(50),
  address VARCHAR(512),
  nid VARCHAR(100),
  account_type VARCHAR(100),
  pin VARCHAR(100),
  signature_path VARCHAR(512),
  profile_image_path VARCHAR(512),
  balance DOUBLE DEFAULT 0,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create transactions table
CREATE TABLE IF NOT EXISTS transactions (
  id INT PRIMARY KEY AUTO_INCREMENT,
  account_number VARCHAR(64),
  type VARCHAR(100),
  amount DOUBLE,
  balance DOUBLE,
  timestamp DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
