# CampuseHive - Senior Project Handoof Guide

> ### Purpose
>This README is written for a new development team taking over CampusHive.  It contains everything you need to build, run, and extend the project in local, staging, and production environments.  Where external services are required, placeholders are provided – create your own accounts and supply credentials via environment variables.

## 1. Project Snapshot
| Layer  | Tech | Notes |
| ------------- | ------------- | ------------- |
| Frontend | Vue 3 (Vite) + Vuetify 3  | Single-Page App (SPA) hosted on Netlify |
| Backend | Spring Boot 3 (Java 21)  | REST API secured with JWT; deployed on Heroku |
| Database | PostgreSQL (Heroku) and H2 (local)  | JPA |
| Storage | Local `SeniorProject/uploads/events` (directory) | Used to store event images |
| Email | JavaMail Sender | Uses SMTP |
| CI/CD | GitHub Actions | None |

### Main features
- User signup / login (JWT) - roles: USER, ADMIN
- CRUD for Events with image upload (multipart)
- Join / un-join events (many-to-many)
- Comments with admin-side deletion + email notice to author
- Email notifications for event deletion

## 2. Local Development
### 2.1 Prerequisites
| Tool  | Version |
| ------------- | ------------- |
| JDK | 21 |
| Maven | 4.0 |
| Node | 24 |
| npm | 11.3 |
| Git | latest |


### 2.2 Clone & Run
```
# 1 - backend:
cd SeniorProject
mvn spring-boot:run

# 2 - frontend
cd ../frontend
npm install
npm run dev
```

### 2.3 Environment Variables
Set your environment varaibles for both front end, back end, and database


Happy hacking!  – CampusHive Core Team
