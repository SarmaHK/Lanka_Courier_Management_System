# Lanka Courier Management System

<p align="left">
  <img src="https://img.shields.io/badge/Java-17-orange?style=flat&logo=java" />
  <img src="https://img.shields.io/badge/Build-Apache%20Maven-C71A36?style=flat&logo=apachemaven" />
  <img src="https://img.shields.io/badge/Database-MySQL%208.0-4479A1?style=flat&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/UI-Java%20Swing-6A5ACD?style=flat" />
  <img src="https://img.shields.io/badge/Platform-Desktop-blue?style=flat" />
  <img src="https://img.shields.io/badge/License-MIT-green?style=flat" />
</p>

A **Java Swing–based courier management system** designed to manage parcel operations, shipment tracking, branch coordination, and role-based access control using a centralized relational database.

This project emphasizes **clean architecture**, **maintainability**, and **practical desktop application development** with Java.

---

## ✨ Features

### 📦 Parcel Operations
- Parcel registration with input validation
- Automatic shipping cost calculation (weight tiers, zones)
- Cash on Delivery (COD) support
- PDF shipping label generation with barcode / QR code

### 🔍 Shipment Tracking
- End-to-end shipment lifecycle  
  `Received → Processing → In Transit → Delivered`
- Timestamped tracking history
- Branch and staff association for each update

### 👤 User & Access Management
- Role-based access control:
  - Admin
  - Clerk
  - Staff / Driver
- Secure authentication using BCrypt password hashing

### 🏢 Branch Management
- Multi-branch support across districts
- Centralized MySQL database for real-time consistency

---

## 🧱 Architecture

The application follows a **layered architecture** to separate responsibilities and improve maintainability.
```bash
UI (Java Swing)
└── Service Layer (Business Logic)
└── DAO Layer (JDBC / SQL)
└── MySQL Database
```

---

## 🛠️ Technology Stack

| Area | Technology |
|----|----|
| Language | Java 17 |
| UI | Java Swing + FlatLaf |
| Database | MySQL 8.0 |
| Persistence | JDBC |
| Build Tool | Apache Maven |
| Security | BCrypt |
| Reporting | Apache PDFBox |

---

## 🚀 Getting Started

### Prerequisites
- Java JDK 17+
- Apache Maven
- MySQL Server (running)


### Database Setup
Import the SQL script provided in the project root:

```bash
mysql -u root -p < Courier_Management_System.sql
Configuration
Update database credentials in:

swift
Copy code
src/main/java/com/lankacourier/DB/dbconnection.java
⚠️ Do not commit real database credentials to public repositories.

Build & Run
bash
Copy code
mvn clean install
mvn exec:java -Dexec.mainClass="com.lankacourier.UI.LoginFrame"
```
📂 Project Structure
```bash

com.lankacourier
├── UI        # Swing UI components
├── Service   # Business logic
├── DAO       # Database access
├── Model     # Entity classes
└── DB        # Database configuration
```
🧪 Testing
Manual functional testing

Input validation checks

Role-based access verification

Database consistency verification

⚠️ Limitations
Desktop-only application

No REST or web interface

No automated test suite

🗺️ Roadmap
Add automated unit testing

Improve reporting and analytics

Externalize configuration

Optional web or API layer

🖼️ Screenshots
Visual overview of the application interface and core workflows.

🔐 Authentication
<img alt="Authentication" src="https://raw.githubusercontent.com/SarmaHK/Lanka_Courier_Management_System/main/screenshots/Authentication.png" />

📊 Dashboard
<img alt= "Dashboard" src="https://raw.githubusercontent.com/SarmaHK/Lanka_Courier_Management_System/main/screenshots/Dashboard.png" />

📦 Parcel Management
<img alt="Parcel Management" src="https://raw.githubusercontent.com/SarmaHK/Lanka_Courier_Management_System/main/screenshots/Parcel Management.png" />
<img alt="Register" src="https://raw.githubusercontent.com/SarmaHK/Lanka_Courier_Management_System/main/screenshots/Register.png" />


🏢 Branch Management
<img alt="Branch" src="https://raw.githubusercontent.com/SarmaHK/Lanka_Courier_Management_System/main/screenshots/Branch.png" />

📄 License
This project is licensed under the MIT License.
See the LICENSE file for details.

🤝 Contributing
This project is maintained as a learning and system-design reference.
Contributions and improvements are welcome.
