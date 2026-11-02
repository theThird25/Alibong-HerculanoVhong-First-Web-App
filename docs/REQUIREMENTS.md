# UserHub: Requirements and Implemented Features

## Part A: Core Features

| #   | Feature                    | Description                                                                     | Expected Behavior                                                                                                                                                | Endpoint                   | Status      |
| --- | -------------------------- | ------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------- | ----------- |
| 1   | User Registration          | New users sign up with full name, email, and password.                          | Valid data creates a USER account and returns 201. Invalid data (blank name, bad email, password under 8 characters) returns 400. A duplicate email returns 409. | `POST /api/auth/register`  | Implemented |
| 2   | Password Hashing           | Passwords are never stored or returned as plain text.                           | Stored as a SHA-256 hash. API responses never contain the password or hash.                                                                                      | n/a (service layer)        | Implemented |
| 3   | User Login                 | Users authenticate with email and password.                                     | Correct credentials return 200 with a token and user info. Wrong email or password returns 401 with the same message for both.                                   | `POST /api/auth/login`     | Implemented |
| 4   | Token-Based Authentication | Login issues a random UUID token, sent as `Authorization: Bearer <token>`.      | A missing token returns 401 "Please log in". An unknown or expired token returns 401 "Session expired". Tokens expire after 30 minutes.                          | all protected endpoints    | Implemented |
| 5   | Role-Based Authorization   | Two roles, USER and ADMIN, control access.                                      | A USER calling an admin endpoint gets 403 "Access denied".                                                                                                       | `GET /api/dashboard/users` | Implemented |
| 6   | Protected Dashboard        | Personal page for logged-in users.                                              | Returns full name, email, role, member-since date, and total user count. Without a valid token it returns 401.                                                   | `GET /api/dashboard`       | Implemented |
| 7   | Exception Handling         | Errors are converted into clean HTTP responses.                                 | 400 for validation, 401 for auth, 403 for role, 409 for conflict, all with a JSON body `{"error": "..."}`.                                                       | all endpoints              | Implemented |
| 8   | H2 and JPA Integration     | Data is stored in an H2 in-memory database through Spring Data JPA.             | The `users` table is created from the `User` entity. Emails are unique.                                                                                          | n/a                        | Implemented |
| 9   | Layered Architecture       | Code is separated into Entity, Repository, Service, Controller, and DTO layers. | Each layer has one responsibility.                                                                                                                               | n/a                        | Implemented |

## Part B: Frontend Features

| #   | Feature                 | Description                              | Expected Behavior                                                                                   | Route                                    | Status      |
| --- | ----------------------- | ---------------------------------------- | --------------------------------------------------------------------------------------------------- | ---------------------------------------- | ----------- |
| 10  | Registration Page       | Form for full name, email, and password. | Success redirects to login. Errors such as "Email already used" are shown.                          | `/register`                              | Implemented |
| 11  | Login Page              | Form for email and password.             | Success stores the token and redirects to the dashboard. Failure shows "Invalid email or password". | `/login`                                 | Implemented |
| 12  | User Dashboard          | Shows the logged-in user's summary.      | Displays name, role badge, member since, and total users.                                           | `/dashboard`                             | Implemented |
| 13  | Protected Routes        | Pages that require login.                | No token redirects to `/login`. A non-admin visiting an admin route redirects to `/dashboard`.      | `/dashboard`, `/profile`, `/admin/users` | Implemented |
| 14  | Axios API Client        | One shared client for all requests.      | Base URL is set once and the token is attached automatically through an interceptor.                | n/a                                      | Implemented |
| 15  | React Router Navigation | URL-based page navigation.               | Unknown paths redirect to `/login`.                                                                 | all routes                               | Implemented |
| 16  | Logout                  | Ends the session.                        | Clears the stored token and returns to login.                                                       | Dashboard button                         | Implemented |

## Part C: Additional Features (50 points)

| #   | Feature            | Points | Description                                                      | Expected Behavior                                                                                      | Endpoint / Route                                 | Status      |
| --- | ------------------ | ------ | ---------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------ | ------------------------------------------------ | ----------- |
| 17  | Seed an ADMIN User | 10     | A default admin is created at startup using `CommandLineRunner`. | `admin@bsit.edu` / `admin12345` can log in with role ADMIN. It is not duplicated on restart.           | startup (`DataSeeder`)                           | Implemented |
| 18  | Admin Users Page   | 15     | An ADMIN-only page lists all registered users.                   | Admin sees a table of ID, name, email, and role. A normal user is redirected, and the API returns 403. | `GET /api/dashboard/users`, route `/admin/users` | Implemented |
| 19  | Server-Side Logout | 10     | The server invalidates the token.                                | Returns 204. Reusing the same token afterwards returns 401 "Session expired".                          | `POST /api/auth/logout`                          | Implemented |
| 20  | Edit My Profile    | 15     | Authenticated users can change their full name.                  | Returns the updated user. A blank name returns 400. The new name shows on the dashboard.               | `PUT /api/users/me`, route `/profile`            | Implemented |

## Additional Design Notes

- **OOP:** `User` is encapsulated (private fields, getters, a `rename()` method), and `Role` is an enum.
- **Abstraction:** `UserService` depends on the `PasswordHasher` and `SessionStore` interfaces rather than on specific implementations.
- **Inheritance:** `AuthException`, `ForbiddenException`, and `ConflictException` extend `ApiException`.
- **Data structures:** sessions are stored in a `ConcurrentHashMap` for O(1) token lookup, with expiry checked on lookup.

## Known Limitations

- Sessions and the database are in memory, so they reset when the backend restarts.
- SHA-256 hashing is for learning only. Production would use BCrypt.
- The token is stored in `localStorage`. Production would use HttpOnly cookies.
