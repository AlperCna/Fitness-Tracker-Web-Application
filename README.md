# Fitness Tracker — Web Application (Backend)

A **fitness tracking web application** built with **Java** and **Spring Boot**, offering a secure, server-rendered interface with JWT-based authentication.

> This repository is the **backend**. The frontend lives in [Fitness-Tracker-Web-Application-Frontend](https://github.com/AlperCna/Fitness-Tracker-Web-Application-Frontend).

## ✨ Features

- 🔐 Authentication & authorization with **Spring Security** + **JWT**
- 🗄️ Persistence with **Spring Data JPA** and **MySQL**
- 🖥️ Server-side rendered views with **Thymeleaf**
- ✅ Request validation
- 🐳 **Docker** support

## 🛠️ Tech Stack

- Java · **Spring Boot**
- Spring Data JPA · Spring Security · Thymeleaf · Bean Validation
- MySQL · JWT (jjwt) · Lombok
- Maven · Docker

## 🚀 Getting Started

```bash
# Configure your MySQL connection in src/main/resources/application.properties
./mvnw spring-boot:run
```

Or with Docker:

```bash
docker build -t fitness-tracker .
docker run -p 8080:8080 fitness-tracker
```

The app starts on `http://localhost:8080`.

## 📄 License

Open source, for educational purposes.
