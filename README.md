Student Scoring API – README

Overview:
This project is a Spring Boot 4 application designed to collect student scores across five subjects, store them in a PostgreSQL database, and generate a detailed report showing:

Individual subject scores

Mean score

Median score

Mode score

The application exposes RESTful APIs for creating students, recording scores, and retrieving score reports.

Assumptions

To implement the project requirements, the following assumptions were made:

Each student is uniquely identified by an auto-generated student ID (Long).

Each student can have only one score per subject.
To enforce this, the Score entity includes a unique constraint on (student_id, subject).

The required subjects are:

MATHS

ENGLISH

BIOLOGY

CHEMISTRY

PHYSICS

Median, mean, and mode calculations are done per student, based on the five subject scores.

When creating a student, scores for all five subjects must be provided, as the report requires all subjects.

For integration testing, an H2 in-memory database is used for speed and isolation.

The production environment runs using PostgreSQL 16, configured via application.yml or Docker Compose.

Design Decisions
1. Domain Model

Two main entities:

Student

id (Long)

name (String)

scores (OneToMany Relationship)

Score

id (Long)

subject (Enum)

score (Integer 0–100)

student (ManyToOne relationship)

Unique constraint to prevent duplicate subjects per student.

2. Separation of Concerns

Controller → Exposes REST APIs

Service Layer → Business logic & report generation

Repository Layer → Database access

DTOs → Used to shape request/response bodies

3. Statistical Calculations

The ReportService handles:

Mean: arithmetic average

Median: middle value after sorting

Mode: most frequently occurring scores

These calculations are isolated to allow easy unit testing.

4. Error Handling

Invalid or duplicate subject entries automatically throw descriptive errors due to:

Validation annotations

Unique constraint in database

Tech Stack

Java 17+

Spring Boot 4

PostgreSQL 16

Hibernate/JPA

Docker & Docker Compose

JUnit 5 + Spring Boot Test

Maven

API Endpoints
1. Create a Student with Scores
   POST /api/students

Request Body
{
"name": "Frank",
"scores": [
{"subject": "MATHS", "score": 100},
{"subject": "ENGLISH", "score": 97},
{"subject": "BIOLOGY", "score": 80},
{"subject": "CHEMISTRY", "score": 100},
{"subject": "PHYSICS", "score": 100}
]
}

2. Get Student Report
   GET /api/students/{id}

Response Example
{
"studentId": 1,
"name": "Frank",
"scores": {
"MATHS": 100,
"ENGLISH": 97,
"BIOLOGY": 80,
"CHEMISTRY": 100,
"PHYSICS": 100
},
"mean": 95.4,
"median": 100,
"mode": [100]
}

Running the Project Locally (No Docker)
1. Configure PostgreSQL in application.yml
   spring:
   datasource:
   url: jdbc:postgresql://localhost:5431/studentdb
   username: userManagement
   password: diogo

jpa:
hibernate:
ddl-auto: update
show-sql: true

2. Start Spring Boot
   mvn spring-boot:run

Running with Docker Compose
docker-compose.yml
version: "3.9"
services:
postgres:
image: postgres:16
container_name: student_postgres
restart: always
environment:
POSTGRES_DB: studentdb
POSTGRES_USER: userManagement
POSTGRES_PASSWORD: diogo
ports:
- "5436:5432"
volumes:
- pgdata:/var/lib/postgresql/data

scoring-api:
build: .
container_name: scoring_api
depends_on:
- postgres
environment:
SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/studentdb
SPRING_DATASOURCE_USERNAME: userManagement
SPRING_DATASOURCE_PASSWORD: diogo
ports:
- "8080:8080"

volumes:
pgdata:

Run Application
docker compose up --build


Application starts at:

http://localhost:8080

Testing
Unit Tests

Validate business logic (mean, median, mode)

Test calculation service with mock repositories

Run:

mvn test

Integration Tests

Use @SpringBootTest with MockMvc

Uses H2 in-memory database via application-test.yml

Project Structure
src/main/java/com/app/ScoringApi/
controller/
dto/
entity/
repository/
service/

src/test/java/com/app/ScoringApi/
unit/
integration/

CREATED BY OLIVIA CHIDIOGO IKEJIUBA
