# 📍 LocationSharingApp
**A real-time Android social application for sharing and visualizing locations.**

Building a bridge between Firebase Cloud services and Google Maps API to help users stay connected through real-time coordinate sharing.

---

## 🚀 Features
- **Secure Authentication:** Email/Password registration and login via Firebase Auth.
- **Real-time Location:** Detects device coordinates using `FusedLocationProviderClient`.
- **Cloud Synchronization:** Automatically saves user profiles and coordinates to **Cloud Firestore**.
- **Social Map View:** Visualize all registered users as interactive markers on a Google Map.
- **Profile Management:** View and update display names with instant database syncing.
- **Friend List:** Efficiently browse all users via an optimized `RecyclerView`.

---

## 🛠 Tech Stack & Architecture
This project is built using the **MVVM (Model-View-ViewModel)** pattern to ensure a separation of concerns and easy testability.



- **Language:** Kotlin
- **UI:** XML Layouts & Material Design
- **Database:** Firebase Cloud Firestore
- **Auth:** Firebase Authentication
- **Location:** Google Play Services (Maps & Location)
- **Asynchronous Work:** Kotlin Coroutines & LiveData

---

## 📸 App Preview
| Auth / Login | Friend List | Google Map View |
| :---: | :---: | :---: |
| ![Auth](https://via.placeholder.com/200x400) | ![List](https://via.placeholder.com/200x400) | ![Map](https://via.placeholder.com/200x400) |

---

## ⚙️ Setup & Installation
To run this project locally, follow these steps:

1. **Clone the Repo:**
   ```bash
   git clone [https://github.com/yourusername/LocationSharingApp.git](https://github.com/yourusername/LocationSharingApp.git)
