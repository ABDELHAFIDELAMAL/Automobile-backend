# 🚗 Automotive Mechanical Intervention Management Application

A full-stack web application designed for automotive workshops to manage the complete lifecycle of vehicle interventions, from client reception to vehicle return. It features a strict business workflow, role-based access control, and a real-time workshop dashboard.

---

## 🛠️ Architecture & Tech Stack

The application follows a decoupled client-server architecture:

### Backend (REST API)
* **Core Framework:** Java 17 / Spring Boot 3
* **Security & Access:** Spring Security, JWT (JSON Web Tokens) Authentication, Role-Based Access Control (RBAC)
* **Data Persistence:** Spring Data JPA, Hibernate
* **Database:** PostgreSQL
* **Tools & Documentation:** Swagger OpenAPI, Lombok, Maven

### Frontend (Web Application)
* **Framework:** Angular
* **Design & Styling:** Tailwind CSS

---

## 🚀 Key Features

The backend exposes a highly secured REST API structured around the following modules:

### 📋 Business Logic & Workflow Management
* **Vehicle Management:** Full tracking and history of client automobiles.
* **Intervention Management:** Creation, assignment, and monitoring of mechanical work orders.
* **Mechanic Management:** Resource allocation based on workshop workload and availability.
* **Workshop Workflow:** Rigorous tracking of repair lifecycles with complete historical logging of every status change.

### 🔐 Security & Administration
* **Authentication & Authorization:** Secure endpoints enforced via JWT tokens.
* **Role Management:** Fine-grained access control based on user profiles (e.g., Admin, Receptionist, Mechanic).

### 📊 Analytics & Documentation
* **Workshop Dashboard:** Real-time Key Performance Indicators (KPIs) reflecting workshop status and metrics.
* **Interactive Documentation:** Swagger UI integration for seamless API discovery and testing.
