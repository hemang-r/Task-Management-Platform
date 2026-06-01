# Task Management Platform (TMP)

## Overview

Task Management Platform (TMP) is a production-oriented microservices-based backend system built using Spring Boot. The platform allows users to manage projects and tasks securely using JWT authentication and role-based authorization.

The system is designed using a microservice architecture and includes:

* User Authentication Service
* Task Management Service
* API Gateway
* Service Registry (Eureka)

---

## Features

### Authentication & User Management

* User Registration
* User Login
* JWT Token-Based Authentication
* Role-Based Authorization (ADMIN, USER)
* Get Current User Profile
* Get User By ID
* Get All Users (ADMIN Only)

### Project Management

* Create Project
* Get All Projects
* Get Project By ID
* Update Project
* Delete Project

### Task Management

* Create Task
* Get All Tasks of a Project
* Get Task By ID
* Update Task
* Assign/Reassign Task
* Update Task Status
* Delete Task
* Get Current User Assigned Tasks

### Security

* JWT Authentication
* Spring Security
* Role-Based Access Control
* Protected APIs

### Microservice Features

* Service Discovery using Eureka
* API Gateway Routing
* OpenFeign for Inter-Service Communication

---

## Microservice Architecture

```text
                        +------------------+
                        |    API Gateway   |
                        |      :8083       |
                        +--------+---------+
                                 |
        ------------------------------------------------
        |                                              |
        |                                              |
+-------v--------+                         +-----------v----------+
| UserAuth       |                         | TaskManagement       |
| Service        |                         | Service              |
| :8081          |                         | :8082                |
+-------+--------+                         +-----------+----------+
        |                                              |
        ------------------------------------------------
                                 |
                       +---------v---------+
                       | Eureka Registry   |
                       | :8761             |
                       +-------------------+
```

---

## Technology Stack

| Technology           | Version                     |
| -------------------- | --------------------------- |
| Java                 | 17                          |
| Spring Boot          | 3.5.14                      |
| Spring Security      | Latest                      |
| Spring Data JPA      | Latest                      |
| PostgreSQL           | Latest                      |
| Maven                | Build Tool                  |
| JWT                  | Authentication              |
| OpenFeign            | Inter-Service Communication |
| Eureka Server        | Service Discovery           |
| Spring Cloud Gateway | API Gateway                 |

---

## Services

### Service Registry

Application Name:

```text
TMP-Service-Registry
```

Port:

```text
8761
```

---

### User Authentication Service

Application Name:

```text
TMP-UserAuth-Service
```

Port:

```text
8081
```

Responsibilities:

* Registration
* Login
* JWT Generation
* User Management
* Role Management

---

### Task Management Service

Application Name:

```text
TMP-TaskManagement-Service
```

Port:

```text
8082
```

Responsibilities:

* Project Management
* Task Management
* Task Assignment
* Task Status Updates

---

### API Gateway

Application Name:

```text
TMP-API-Gateway
```

Port:

```text
8083
```

Responsibilities:

* Single Entry Point
* Request Routing
* Service Discovery Integration

---

## Database Setup

PostgreSQL is used as the database.

Create the databases manually before running the application:

```sql
CREATE DATABASE authdb;

CREATE DATABASE taskdb;
```

---

## Required Environment Variables

### UserAuth Service

| Variable       | Value                                                       |
| -------------- | ----------------------------------------------------------- |
| SERVER_PORT    | 8081                                                        |
| DB_URL         | jdbc:postgresql://localhost:5432/authdb                     |
| DB_USERNAME    | postgres                                                    |
| DB_PASSWORD    |                                                             |
| JWT_SECRET     |                                                             |
| JWT_EXPIRATION | 86400(24 hours can change from application.properties file) |

### Task Management Service

| Variable       | Value                                   |
| -------------- | --------------------------------------- |
| SERVER_PORT    | 8082                                    |
| DB_URL         | jdbc:postgresql://localhost:5432/taskdb |
| DB_USERNAME    | postgres                                |
| DB_PASSWORD    |                                         |
| JWT_SECRET     |                                         |
| JWT_EXPIRATION | 86400                                   |

### Service Registry

| Variable    | Value |
| ----------- | ----- |
| SERVER_PORT | 8761  |

### API Gateway

| Variable    | Value |
| ----------- | ----- |
| SERVER_PORT | 8083  |

---

## Running the Application

### Step 1: Clone the Repository

```bash
git clone <repository-url>

cd <project-folder>
```

---

### Step 2: Import into STS

Open Spring Tool Suite (STS).

Import all microservices as Open Projects from File System.

Need to add application.properties files manually. 

---

### Step 3: Configure PostgreSQL

Create databases:

```sql
CREATE DATABASE authdb;

CREATE DATABASE taskdb;
```

Update database credentials if required.

---

### Step 4: Start Services

Start services in the following order:

```text
1. TMP-Service-Registry
2. TMP-UserAuth-Service
3. TMP-TaskManagement-Service
4. TMP-API-Gateway
```

---

### Step 5: Verify Eureka Registration

Open:

http://localhost:8761

Verify that all services are registered successfully.

---

## Authentication

Authentication is implemented using JWT Bearer Tokens.

### Roles

```text
ADMIN
USER
```

### Authorization Header

```http
Authorization: Bearer <JWT_TOKEN>
```

---

# User Authentication APIs

Base URL

```text
http://localhost:8083/tmp-userauth-service
```

| Method | Endpoint               | Access             |
| ------ | ---------------------- | ------------------ |
| POST   | /api/v1/auth/register  | Public             |
| POST   | /api/v1/auth/login     | Public             |
| GET    | /api/v1/users/me       | Authenticated User |
| GET    | /api/v1/users/{userId} | Authenticated User |
| GET    | /api/v1/users          | ADMIN Only         |

---

# Project APIs

Base URL

```text
http://localhost:8083/tmp-taskmanagement-service
```

| Method | Endpoint                     | Access             |
| ------ | ---------------------------- | ------------------ |
| POST   | /api/v1/projects             | ADMIN Only         |
| GET    | /api/v1/projects             | Authenticated User |
| GET    | /api/v1/projects/{projectId} | Authenticated User |
| PUT    | /api/v1/projects/{projectId} | ADMIN Only         |
| DELETE | /api/v1/projects/{projectId} | ADMIN Only         |

---

# Task APIs

Base URL

```text
http://localhost:8083/tmp-taskmanagement-service
```

| Method | Endpoint                                           | Access             |
| ------ | -------------------------------------------------- | ------------------ |
| POST   | /api/v1/projects/{projectId}/tasks                 | Authenticated User |
| GET    | /api/v1/projects/{projectId}/tasks                 | Authenticated User |
| GET    | /api/v1/projects/{projectId}/tasks/{taskId}        | Authenticated User |
| PUT    | /api/v1/projects/{projectId}/tasks/{taskId}        | Authenticated User |
| PATCH  | /api/v1/projects/{projectId}/tasks/{taskId}/assign | ADMIN Only         |
| PATCH  | /api/v1/projects/{projectId}/tasks/{taskId}/status | Authenticated User |
| DELETE | /api/v1/projects/{projectId}/tasks/{taskId}        | ADMIN Only         |
| GET    | /api/v1/projects/tasks/my-tasks                    | Authenticated User |

---

## Task Status Rules

Valid status transitions:

```text
TODO -> IN_PROGRESS

IN_PROGRESS -> DONE

DONE -> IN_PROGRESS
```

Any other transition will return:

```http
400 BAD REQUEST
```

---

## Swagger Documentation

### UserAuth Service

```text
http://localhost:8081/swagger-ui/index.html
```

### Task Management Service

```text
http://localhost:8082/swagger-ui/index.html
```

---

## Inter-Service Communication

OpenFeign is used for communication between services.

Example:

```text
Task Management Service
            |
            |
            v
User Authentication Service
```

Used for:

* User Validation
* Assignee Validation
* User Information Retrieval

---

## Future Improvements

* Refresh Tokens
* Docker Support
* Kubernetes Deployment
* Centralized Logging
* Distributed Tracing
* Circuit Breaker (Resilience4j)
* Config Server
* CI/CD Pipeline

---

## Author

Hemang Raninga

Task Management Platform (TMP)
Spring Boot Microservices Project
