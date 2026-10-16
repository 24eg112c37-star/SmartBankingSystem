# SmartBank

A responsive banking management **simulation** built with React, Vite, Spring Boot, Spring Security, JWT, Flyway, and MySQL. It does not provide real banking, payment processing, interbank settlement, or regulated financial services.

## Features

- Customer registration and email/password sign-in with BCrypt password hashing and signed JWT access tokens.
- Backend-created savings accounts with unique account numbers.
- Customer-only account access, transaction history, CSV statement export, and dashboard totals derived from persisted API data.
- Deposits, withdrawals, and account-to-account transfers use database transactions and pessimistic account locks. Amounts use `BigDecimal` and are limited to INR 1,000,000 per operation.
- Role-protected administration dashboard, customer list, account status controls, and recent transaction view.
- Password recovery with random one-time email tokens stored as SHA-256 hashes and expiring after 15 minutes; customer profile and password updates.
- Flyway-managed MySQL schema; database credentials and JWT secrets are supplied through environment variables.

## Prerequisites

- Java 21+
- Maven 3.9+
- Node.js 20+
- MySQL 8+

## Configure and run

1. Create a MySQL database (or allow the JDBC URL to create `smartbank`):

   ```sql
   CREATE DATABASE smartbank;
   ```

2. From the project root, create the backend's private environment file and enter the password for your local MySQL account in `DB_PASSWORD`. Replace `JWT_SECRET` with a random value of at least 32 characters and keep it in this ignored file so sessions remain valid across server restarts. The file is loaded automatically when the API starts from `backend`.

   ```powershell
   Copy-Item backend/.env.example backend/.env
   # Open backend/.env and replace DB_PASSWORD and JWT_SECRET with your local values.
   ```

3. Optionally create the first administrator without a public registration route. Add these entries to `backend/.env` for a controlled first startup; the account is created only if that email does not already exist.

   ```dotenv
   BOOTSTRAP_ADMIN_EMAIL=admin@example.test
   BOOTSTRAP_ADMIN_PASSWORD=use-a-unique-password-of-16-characters-or-more
   ```

   Start the API from `backend` with `mvn -s maven-settings.xml spring-boot:run`. Flyway applies the schema at startup. Remove the bootstrap variables after the administrator has been created. Password reset messages require valid SMTP settings in `backend/.env`.

4. From `frontend`, copy `.env.example` to `.env.local` if the API URL differs from `http://localhost:8080/api`, then install and start Vite:

   ```powershell
   cd frontend
   Copy-Item .env.example .env.local
   npm install
   npm run dev
   ```

   Open `http://localhost:5173`.

## Environment variables

| Variable | Default | Purpose |
| --- | --- | --- |
| `DB_URL` | Local MySQL `smartbank` URL | JDBC connection URL |
| `DB_USERNAME` | `root` | Database user |
| `DB_PASSWORD` | empty | Database password |
| `JWT_SECRET` | none | HMAC signing secret; at least 32 characters, required in the `prod` profile; local development generates an ephemeral key if absent |
| `JWT_EXPIRATION_MS` | `3600000` | Access token lifetime |
| `FRONTEND_ORIGIN` | `http://localhost:5173` | Allowed browser origin for CORS |
| `MAIL_HOST`, `MAIL_PORT` | empty, `587` | SMTP server for password recovery |
| `MAIL_USERNAME`, `MAIL_PASSWORD` | empty | SMTP credentials |
| `MAIL_FROM` | `no-reply@smartbank.local` | Password recovery sender address |
| `FRONTEND_URL` | `http://localhost:5173` | Base URL used in recovery emails |
| `PORT` | `8080` | Backend listening port |
| `BOOTSTRAP_ADMIN_EMAIL` | empty | Optional first-admin email |
| `BOOTSTRAP_ADMIN_PASSWORD` | empty | Optional first-admin password (16+ characters) |
| `VITE_API_URL` | `http://localhost:8080/api` | Frontend API base URL |

## API overview

Customer routes except `/api/auth/**` require a valid bearer token. Admin routes require `ADMIN`.

| Method | Route | Access |
| --- | --- | --- |
| `POST` | `/api/auth/register` | Public; always creates `CUSTOMER` |
| `POST` | `/api/auth/login` | Public |
| `POST` | `/api/auth/forgot-password` | Public; generic response avoids email enumeration |
| `POST` | `/api/auth/reset-password` | Public; requires a valid emailed token |
| `GET`, `PUT` | `/api/me` | Authenticated |
| `PUT` | `/api/me/password` | Authenticated; requires current password |
| `GET` | `/api/accounts` | Authenticated; caller's accounts only |
| `GET` | `/api/transactions` | Authenticated; caller's transactions only |
| `GET` | `/api/notifications` | Authenticated; caller's notifications only |
| `PATCH` | `/api/notifications/{id}/read` | Authenticated; owned notification |
| `POST` | `/api/accounts/{id}/deposit` | Authenticated; owned account |
| `POST` | `/api/accounts/{id}/withdraw` | Authenticated; owned account |
| `POST` | `/api/accounts/{id}/transfer` | Authenticated; owned account |
| `GET` | `/api/admin/summary` | Admin |
| `GET` | `/api/admin/customers` | Admin |
| `PATCH` | `/api/admin/customers/{id}/status` | Admin |
| `GET` | `/api/admin/transactions` | Admin |

Request validation errors return a JSON `message`; transaction amounts must be positive and have at most two decimal places. Without SMTP configuration, forgot-password requests still return the same generic response, but no email is sent. Refresh-token cookies/revocation, audit logs, idempotency keys, flagged-transaction review, server pagination, and PDF statements are not implemented. JWT access tokens are held in browser local storage; production deployment should use a hardened refresh-token cookie/session design and review CSRF, XSS, HTTPS, secret rotation, and rate limits.
## Deployment preparation

For Vercel, set the project root directory to `frontend` and set `VITE_API_URL` to the deployed API URL plus `/api`. For Render, create a Web Service using the Docker runtime, set the root directory to `backend`, and use `Dockerfile` as the Dockerfile path. Configure `SPRING_PROFILES_ACTIVE=prod`, `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `FRONTEND_ORIGIN`, and `PORT`; also set SMTP variables if password recovery email is needed. The database URL must point to a reachable MySQL service. The app refuses to start in `prod` without a strong JWT secret. Use HTTPS and secure cookie settings if refresh-token cookies are added. No hosting resources are created by this project.

## Checks

```powershell
# backend
cd backend
mvn -s maven-settings.xml test

# frontend
cd frontend
npm install
npm run build
```


