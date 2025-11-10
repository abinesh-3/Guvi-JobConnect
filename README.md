# JobConnect (updated)

Features added:
- JWT-based authentication for REST APIs under `/api/**`
- REST controllers for authentication and jobs
- Switched default DB config to MySQL (update credentials in `application.properties`)
- Improved UI using Bootstrap CDN
- Additional tests (MockMvc + security)

## Run with MySQL

1. Create a database:
   ```sql
   CREATE DATABASE jobconnect CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. Update `src/main/resources/application.properties` with your MySQL username/password.
3. Run:
   ```
   mvn spring-boot:run
   ```

## REST API examples

Register (REST):
```
POST /api/auth/register
Body: { "email": "a@b.com", "password": "pass", "fullName": "A User" }
```

Login:
```
POST /api/auth/login
Body: { "email": "a@b.com", "password": "pass" }
Response: { "token": "...", "tokenType": "Bearer" }
```

List jobs (public):
```
GET /api/jobs
```

Create job (Bearer token required):
```
POST /api/jobs
Headers: Authorization: Bearer <token>
Body: { "title": "Dev", "description": "..." }
```

## Tests

Run:
```
mvn test
```

