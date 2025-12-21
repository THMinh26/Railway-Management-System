# Railway Management System

## Overview

This repository contains an academic project implementation of a Railway Management System. The application provides a Spring Boot backend (REST APIs) and a static frontend served from the backend's resources. Functionality includes user authentication, train search, coach/seat selection, and an administrative module for managing trains, stations, schedules, users and bookings. Some booking flows are provided as frontend mocks; backend booking endpoints are partially implemented as noted in project documentation.

## Prerequisites

- Java Development Kit (JDK) 17 or later (Spring Boot 3.x requires Java 17+)
- Git (to clone the repository)
- Maven (the project includes the Maven wrapper `mvnw` / `mvnw.cmd` but a local Maven installation is optional)
- PostgreSQL (recommended for production or if the application is configured to use it)
  - Alternatively H2 may be used for development; check `src/main/resources/application.properties` for the active configuration
- (Optional) `pandoc` if you intend to convert Markdown documentation to other formats (used in the project report export)

## Repository Structure (selected)

- `pom.xml` — Maven build file
- `src/main/java/...` — Java source code (controllers, services, repositories, DTOs)
- `src/main/resources/static/` — Static frontend assets (HTML, CSS, JS)
- `src/main/resources/application.properties` — Application configuration
- `mvnw`, `mvnw.cmd` — Maven wrapper

## Configuration

1. Database
   - By default the application reads the JDBC connection and server port from `src/main/resources/application.properties` (or environment variables).
   - If using PostgreSQL, create a database (for example `railway`) and update the JDBC URL, username and password in `application.properties`.
   - If the project contains a `DataInitializer` component it may seed initial data; review `src/main/java/.../config/DataInitializer.java` if present.

2. Application properties
   - Edit `src/main/resources/application.properties` to adjust the following if necessary:
     - `spring.datasource.url`
     - `spring.datasource.username`
     - `spring.datasource.password`
     - `server.port` (the port on which the application listens)

## Build and Run

The project includes a Maven wrapper so you can run it without installing Maven system-wide.

On Windows (PowerShell/CMD):

```powershell
# Run the application in-place (development)
.\mvnw.cmd spring-boot:run

# Build a packaged executable JAR
.\mvnw.cmd -DskipTests package

# Run the packaged JAR (after packaging)
java -jar target\*.jar
```

On Unix / macOS:

```bash
# Run the application in-place (development)
./mvnw spring-boot:run

# Build a packaged executable JAR
./mvnw -DskipTests package

# Run the packaged JAR (after packaging)
java -jar target/*.jar
```

Notes:
- If `server.port` is configured to `8081` (or another port), include that port when opening the application in a browser (for example: `http://localhost:8081/`).
- If the port is already in use, stop the occupying process or change `server.port`.

## Frontend

- The frontend is implemented as static assets located in `src/main/resources/static/` and is served directly by Spring Boot when the application is running.
- Primary pages:
  - `index.html` — homepage and search interface
  - `booking.html` — booking page (linked through backend controller)
  - `admin.html` — administrative panel (requires ADMIN role)
- No Node.js build step is required to serve the static frontend; simply run the Spring Boot application and access the pages in a browser.

## Running Tests

Execute unit/integration tests using the Maven wrapper:

```bash
./mvnw test
# or on Windows
.\mvnw.cmd test
```

## API Endpoints (examples)

- `POST /api/auth/login` — authenticate user
- `GET /api/admin/bookings` — list all bookings (admin)
- `GET /api/booking` — redirects to booking.html with query parameters for pre-filling booking form
- `POST /api/booking` — (if implemented) create a booking

Refer to controller classes under `src/main/java` for the full set of endpoints and request/response DTOs.

## Admin Access

- Administrative pages require the user to have role `ADMIN`.
- If the repository seeds a default admin account, consult `DataInitializer` or the database seed scripts for credentials; otherwise create an admin user directly in the database.

## Troubleshooting

- Database connectivity errors: verify JDBC URL, username and password and that the database server is running.
- Port in use: change `server.port` in `application.properties` or stop the process using the configured port.
- CORS or browser errors when calling the API from the frontend: ensure the backend is running on the configured port and that `@CrossOrigin` is enabled on controllers (this project enables CORS on many controllers for development).
- If login does not reflect in the UI after redirect, clear browser storage (localStorage) and retry; static pages read session state from `localStorage`.

## Contributing

Contributions are welcome following academic and software-engineering best practices. Please open an issue describing the change and submit a pull request with tests and documentation for non-trivial changes.

## License

This project repository is provided for academic purposes. Include an explicit license file if you intend to publish or distribute the code.
