# Smart Parking Building Management System 🅿️

A comprehensive parking management system built with **Spring Boot MVC** and **Firebase Firestore**.

## 🚀 Features

### 4 User Roles
- **System Administrator** - Full system management
- **Parking Manager** - Building, slots, pricing, reports management
- **Parking Staff** - Sessions, reservations, payments, incidents
- **Driver** - View info, reservations, payments, feedback

### 60+ Functions Including:
- ✅ Building Management (CRUD)
- ✅ Floor Management (CRUD)
- ✅ Zone Management (CRUD)
- ✅ Parking Slot Management (CRUD + Lock/Unlock)
- ✅ Vehicle Type Management (CRUD)
- ✅ Pricing Policy Management (CRUD)
- ✅ Parking Session Management (Start/End)
- ✅ Reservation Management (CRUD)
- ✅ Payment Processing (Pay/Refund)
- ✅ Incident Reporting (Lost Ticket, Wrong Plate, Overtime, etc.)
- ✅ User Management (CRUD + Role Assignment)
- ✅ Reports & Analytics Dashboard
- ✅ Revenue, Traffic, Occupancy Reports
- ✅ Beautiful Dark Theme UI with Animations

## 🛠️ Tech Stack
- **Backend:** Java 17, Spring Boot 3.2
- **Frontend:** Thymeleaf, HTML5, CSS3, Chart.js
- **Database:** Firebase Firestore (with in-memory fallback)
- **Security:** Spring Security with BCrypt
- **Architecture:** MVC Pattern

## 📦 Setup

### 1. Prerequisites
- Java 17+
- Maven 3.8+
- Firebase Project (optional - works with in-memory storage)

### 2. Firebase Setup (Optional)
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project
3. Go to Project Settings > Service Accounts
4. Generate New Private Key
5. Replace `src/main/resources/firebase-service-account.json` with downloaded file

### 3. Run
```bash
mvn spring-boot:run
```

### 4. Access
Open http://localhost:8080

### Demo Accounts:
| Role | Email | Password |
|------|-------|----------|
| Admin | admin@parking.com | admin123 |
| Manager | manager@parking.com | manager123 |
| Staff | staff@parking.com | staff123 |
| Driver | driver@parking.com | driver123 |

## 📁 Project Structure
```
src/main/java/com/parking/
├── ParkingApplication.java
├── config/
│   ├── FirebaseConfig.java
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── DashboardController.java
│   ├── BuildingController.java
│   ├── VehicleTypeController.java
│   ├── FloorController.java
│   ├── ZoneController.java
│   ├── ParkingSlotController.java
│   ├── PricingController.java
│   ├── SessionController.java
│   ├── ReservationController.java
│   ├── PaymentController.java
│   ├── IncidentController.java
│   ├── UserController.java
│   └── ReportController.java
├── model/
│   ├── Role.java, User.java, Building.java
│   ├── Floor.java, Zone.java, VehicleType.java
│   ├── Vehicle.java, ParkingSlot.java
│   ├── PricingPolicy.java, ParkingSession.java
│   ├── Reservation.java, Payment.java
│   └── IncidentReport.java
└── service/
    ├── FirebaseService.java
    └── CustomUserDetailsService.java
```

## 📄 License
This project is for educational purposes (SWP Course).
