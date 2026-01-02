# 🗳️ Online Voting System (OVS) #

A full-stack, secure, and responsive web application built using Spring Boot, Thymeleaf, and MySQL. 
This system allows voters to register, login, and cast their votes for candidates 
while providing an administrative panel for election monitoring and management.

---

##  Features

### Voter Module ###
* Secure Authentication: User registration, login, and password recovery.
* Voter Integrity: System ensures "One Person, One Vote" logic using database flags.
* Interactive Dashboard: Clean UI to view candidates and cast votes.
* Live Feedback: Toast notifications and alerts for successful voting or errors.

### Admin Module ###
* User Management: View all registered voters and remove unauthorized accounts.
* Candidate Management: Add new candidates with name, party affiliation, and photo upload.
* Live Election Monitor: Real-time table and progress bars showing vote distribution.
* Election Reset: One-click functionality to wipe votes and reset voter status for a new election cycle.

---

## 🛠️ Technologies Used ##

- Backend: Java 17+, Spring Boot 3.x, Spring Data JPA
- Frontend: Thymeleaf, Bootstrap 5, Bootstrap Icons, JavaScript (ES6)
- Database: MySQL 8.0
- Security: Session-based authentication and role-based access logic.
- Tools: Maven for dependency management.

---

## Project Structure ##



```text
src/main/java
 └── in.vicky.main
     ├── controller      => Auth and Admin Route Handlers
     ├── entities        => User and Candidate JPA Entities
     ├── repository      => Database Access Interfaces (JPA)
     └── mailService     => Business logic for Registration/Recovery
src/main/resources
 ├── templates           => Thymeleaf HTML files
 └── static
     ├── images          => Uploaded Candidate photos
     └── css             => Custom styling
     

## DataBase creation

** CREATE DATABASE ovs;
** USE ovs;
     
** CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    gender VARCHAR(10),
    aadhar_no VARCHAR(12) UNIQUE NOT NULL,
    phone_number VARCHAR(15),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    has_voted BOOLEAN DEFAULT FALSE
);

** CREATE TABLE candidates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_name VARCHAR(100) NOT NULL,
    party_name VARCHAR(100) NOT NULL,
    photo_path VARCHAR(255), -- Stores the file path or URL of the image
    vote_count INT DEFAULT 0
);

** CREATE TABLE admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);  
     