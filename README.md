# Task Management System

Tools & Technologies
- Java Spring Boot Framework
- RESTful API Design
- Self-Signed Certificates for HTTPS (TLS)
- Swagger for API documentation
- Google Authentication for secure login
- JWT (JSON Web Tokens) for secure API access
- Postman for API testing and validation
- GitHub Actions for CI/CD and automated testing
- SQL Database for data persistence
- JUnit and Mockito for unit testing
- Jacoco Code Coverage (Done using GitHub Actions)
- Spring Security for authentication and authorization
- Spring Data JPA for database interactions
- GitHub for version control and team collaboration

Aim of the System
The goal of this project is to design and develop a comprehensive **Task Management System** that allows users to create, manage, and track tasks efficiently. The system ensures secure access through Google Authentication and JWT-based security.
JWT tokens are on only valid, a refresh token is used to generate a new jwt access token. Old tokens are black listed & cannot be used to validate.
 Key Features
- **Task Creation:** Users can create new tasks with details such as title, description, and due date.
- **Task Management:** Users can update, delete, and mark tasks as complete.
- **User Authentication:** Secure login using Google Authentication.
- **API Documentation:** API documentation generated using Swagger.
- **Secure Access:** JWT-based security for API endpoints.
- **Automated Testing using GitHub Actions:** Unit tests for functionalities using JUnit and Mockito.
- **Code Coverage:** Jacoco code coverage reports generated through GitHub Actions.

Security Features
- **JWT Token Authentication:** Users receive a JWT token upon successful login, which is used for subsequent API requests.
- **Google Authentication (One Time Passcode):** Users can log in using their Google accounts, simplifying the authentication process.
- **Self-Signed Certificates (TLS):** The application uses self-signed certificates for secure HTTPS communication.

REST API Design & Testing
The REST API follows best practices for RESTful architecture. Key endpoints include:
- `GET /tasks` — Retrieve a list of all tasks.
- `POST /tasks` — Create a new task.
- `GET /tasks/` — Retrieve details of a specific task.
- `PUT /tasks/` — Update a specific task.
- `DELETE /tasks/` — Delete a specific task.

Testing is conducted via Postman to ensure the API behaves as expected.

Testing - Automated with GitHub Actions, Unit Testing & Code Coverage
The project includes testing using JUnit and Mockito. Continuous Integration/Continuous Deployment (CI/CD) is configured via GitHub Actions, including automated unit testing and Jacoco code coverage reporting.

What This Project Demonstrates
This project showcases the ability to design and develop secure, scalable RESTful APIs using the Java Spring Boot framework. It highlights skills in implementing authentication and authorization mechanisms, API documentation, and automated testing.
