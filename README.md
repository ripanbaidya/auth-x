# 📌 Authentication with Email Validation using Spring Boot, Spring Security, and JWT

This project implements a **secure authentication system** using **Spring Boot**, **Spring Security**, and **JWT (JSON Web Tokens)**.
It includes a complete flow for **user registration, email verification, and login**, ensuring that only verified users can access protected APIs.

## 🔑 Features

* 📝 **User Registration** – Sign up with email & password
* 📧 **Email Verification** – Token-based verification to activate accounts
* 🔐 **Login with JWT** – Access secured APIs with generated tokens
* 🔒 **Secure Passwords** – Stored with **BCrypt hashing**
* 👤 **Role-Based Authorization** – Supports roles like `USER`, `ADMIN`
* 🛡️ **Spring Security Integration** – Filters & guards for all endpoints
* ⏱️ **JWT Expiry & Refresh** – Configurable expiration with refresh support
* 📨 **Email Service** – Verification emails sent using **JavaMailSender**
* 🗄️ **Database** – PostgreSQL (via Docker Compose)


## 🛠️ Tech Stack

* **Backend**: Spring Boot (REST APIs)
* **Security**: Spring Security, JWT
* **Database**: PostgreSQL (Docker Compose)
* **Email Service**: JavaMailSender (SMTP)
* **Build Tool**: Maven

## 🚀 Setup & Run

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/ripanbaidya/AuthX.git
cd AuthX
```

### 2️⃣ Start PostgreSQL with Docker

Make sure **Docker** is installed & running. Then run:

```bash
docker-compose up
```

This will pull the required PostgreSQL image and start the container.

### 3️⃣ Run the Application

Run the Spring Boot application (default port: `8080`).

### 4️⃣ Check Email Verification

A mock mail server (**MailDev**) is included and runs on:
👉 [http://localhost:1080/](http://localhost:1080/)

Here you’ll receive verification emails.


## 📡 API Endpoints

### 🔹 Register User

**POST** `http://localhost:8080/api/v1/auth/register`

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "johndoe@gmail.com",
  "password": "123456"
}
```

### 🔹 Login (Authenticate)

**POST** `http://localhost:8080/api/v1/auth/authenticate`

```json
{
  "email": "johndoe@gmail.com",
  "password": "123456"
}
```

### 🔹 Activate Account

**GET** `http://localhost:8080/api/v1/auth/activate-account?token=775536`

## 🎉 Congratulations!

Your authentication module is ready. You can now integrate it into your next project.

If this repo helped you, ⭐ **give it a star** – it costs nothing but motivates me to share more!

