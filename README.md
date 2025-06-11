# CampuseHive - Senior Project Handoof Guide

> ### Purpose
>This README is written for a new development team taking over CampusHive.  It contains everything you need to build, run, and extend the project in local, staging, and production environments.  Where external services are required, placeholders are provided – create your own accounts and supply credentials via environment variables.

## 1. Project Snapshot
| Layer  | Tech | Notes |
| ------------- | ------------- | ------------- |
| Frontend | Vue 3 (Vite) + Vuetify 3  | Single-Page App (SPA) hosted on Netlify |
| Backend | Spring Boot 3 (Java 21)  | REST API secured with JWT; deployed on Heroku |
| Database | PostgreSQL (Heroku) and H2 (local)  | JPA |
| Storage | Local `/static/images/events/` (directory) | Used to store event images |
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
| Layer  | Tech | Notes |
| ------------- | ------------- | ------------- |
| Frontend | Vue 3 (Vite) + Vuetify 3  | Single-Page App (SPA) hosted on Netlify |
| Backend | Spring Boot 3 (Java 21)  | REST API secured with JWT; deployed on Heroku |
| Database | PostgreSQL (Heroku) and H2 (local)  | JPA |
| Storage | Local `/static/images/events/` (directory) | Used to store event images |
| Email | JavaMail Sender | Uses SMTP |
| CI/CD | GitHub Actions | None |




## 3. Applications needed for development
### 3.1 Microsoft SSO(Login)

1. Create / Sign in to the Azure portal and create a new registration.

![](doc/images/Microsoft1.png)

2. Create a single-page application-use your own URIs.
    - We have two URIs, one for local development and the other for production.

![](doc/images/Microsoft2.png)

3. Allow implicit grant and hybrid flows.

![](doc/images/Microsoft3.png)

4. Allow only students to access this.

![](doc/images/Microsoft4.png)

5. Go to API permissions and add these, and grant admin consent after adding the API permissions.

![](doc/images/Microsoft5.png)

6. Create a Client Secret and save those credentials.

![](doc/images/Microsoft6.png)

7. Add your new ClientID and URI in a .env file for security(front-end).

![](doc/images/Microsoft7.png)

8. How to import these inside the front-end.

![](doc/images/Microsoft8.png)


### 3.2 Google SSO(Calendar Sync) Setup

1. Create an account / sign in to Cloud Console and create a project.

![](doc/images/Google1.png)

2. Go to APIs & Services and click on enable APIs and Services.

![](doc/images/Google2.png)

3. Search for the Google Calendar API and enable.

![](doc/images/Google3.png)

4. Click on OAuth client ID.

![](doc/images/Google4.png)

5. Fill out the project configuration.

![](doc/images/Google5.png)

6. Create OAuth clientID & add your own redirect URIs.

![](doc/images/Google6.png)

7. Add test users unless you have a website approved by Google.

![](doc/images/Google7.png)

8. Should look something like this / save the credentials somewhere for easy access.

![](doc/images/Google8.png)

9. Replace these credentials in the application properties with your own. The application properties are located in SeniorProject/src/main/resources/application.properties.

![](doc/images/Google9.png)

10. Add the clientId and URI to the front-end too.

![](doc/images/Google10.png)

### 3.3 Notifications using Google

1. Create a Google account / sign in 

2. Go to Google Account settings

![](doc/images/Notis2.png)

3. Click on security and enable 2-Step verification

![](doc/images/Notis3.png)

4. Click on App passwords

![](doc/images/Notis4.png)

5. Create an App password / save the code

![](doc/images/Notis5.png)

6. Go to Application properties and change the credentials for both username / password

![](doc/images/Notis6.png)