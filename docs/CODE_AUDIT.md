# GameFlix AI-Assisted Code Audit

## Scope

This audit reviews the AI-assisted registration, login, JWT authentication,
subscription, and catalog code in the current GameFlix prototype. I did not
accept the generated behavior based only on whether it compiled. I reviewed the
request flow, tested the application in the browser, ran JUnit tests, and sent
authorized and unauthorized requests through Postman. The review identified
the following issues and improvement opportunities.

## 1. Password handling needed stronger protection

An early implementation risk was storing or comparing passwords as plain text.
That would expose every account if the database were viewed or leaked. The
current `UserService` uses `BCryptPasswordEncoder` when registering a user and
uses `matches` when logging in. Only the BCrypt hash is stored. Login also
returns the same `Invalid username or password` message whether the username is
missing or the password is wrong, which avoids revealing which usernames are
registered.

I verified registration and login in the browser and tested a successful login
through Postman. A future improvement would be to configure the password
encoder as an injected Spring bean instead of constructing it inside the
service. That would make the strength setting and test configuration easier to
manage.

**Status:** Corrected for the prototype; dependency injection could be cleaner.

## 2. A successful login did not initially authorize later actions

The first frontend integration could display `Login successful` while plan
selection still returned `Login required`. The message alone did not prove that
the token was saved and attached to later requests. This was a subtle integration
problem because login and plan selection each appeared reasonable when reviewed
separately.

The frontend was corrected to retain the JWT returned by `/api/login` and send
it as `Authorization: Bearer <token>` when saving a plan or changing the game
catalog. The interceptor now distinguishes a missing bearer token from an
invalid or expired one. I verified the corrected flow by logging in, selecting
the Pro plan, and receiving `PRO plan saved`. The Postman collection also saves
the login token and reuses it for protected requests.

**Status:** Corrected and verified through the browser and Postman.

## 3. The JWT signing key does not survive an application restart

`JwtService` generates a new HS256 signing key in memory when Spring Boot
starts. This avoids committing a secret to the repository, which is safer than
a hardcoded key, but it also invalidates every existing login token whenever the
application restarts. That behavior is acceptable for a classroom prototype
but would be disruptive in a deployed subscription service.

For production, I would load a sufficiently strong signing key from an
environment variable, Docker secret, or hosted secret manager. The same secure
key would then be available to every application instance. Key rotation would
also need an intentional policy rather than occurring on every restart.

**Status:** Documented limitation; planned deployment improvement.

## 4. Authentication is implemented with a custom interceptor

The interceptor protects subscription requests and non-GET catalog requests,
but it is a small custom security layer rather than a complete Spring Security
configuration. Any authenticated user can currently add or remove catalog
records. There is no administrator role even though the project description
mentions administrative tools. The code also catches a general exception when
token parsing fails, which is simple but less precise for logging and debugging.

The current behavior was made explicit: public users can read the catalog, while
only logged-in users can modify it or access a subscription. Integration tests
verify the protected route, and the attack log records missing- and invalid-token
responses. A stronger version would use Spring Security filters, authorities,
and an `ADMIN` role for catalog changes.

**Status:** Adequate for the prototype; role-based authorization remains future
work.

## 5. Input validation and API models can be improved

The catalog controller accepts the persistence entity directly as its request
body. The service checks for a title, genre, reasonable release year, and a
case-insensitive duplicate title. These checks prevent the most obvious invalid
catalog records, and service tests exercise valid, duplicate, and deletion
behavior. However, binding directly to an entity can expose database fields as
the model grows, and the upper release-year limit of 2100 is a fixed business
rule rather than a date-aware rule.

I retained the explicit service checks because they are readable and appropriate
for the current prototype. A future revision should introduce a `GameRequest`
DTO with Jakarta Bean Validation annotations and return structured validation
errors. The internal `Movie` class and `/api/movies` path should also eventually
be renamed to `Game` and `/api/games` to match the product language.

**Status:** Core validation added; DTO validation and legacy renaming remain
known improvements.

## 6. Development credentials are visible in configuration

The Docker Compose and CI files contain a simple MySQL password. This makes the
local classroom setup easy to reproduce, but the same approach would be unsafe
for a public deployment. The Spring properties already support environment
variables, and Docker Compose passes the database URL and credentials to the
application container rather than baking them into the JAR.

Before deployment, I would replace repository values with platform secrets and
use a non-root database user with only the permissions GameFlix needs. I would
also disable SQL logging and review the `ddl-auto=update` setting for production.

**Status:** Acceptable only as local development configuration; must change for
deployment.

## Verification summary

The Maven test suite currently contains 13 passing tests covering application
startup, catalog service behavior, subscription behavior, and protected-route
authentication. The Postman runner produced four passing assertions and zero
failures, including HTTP 401 responses for missing and invalid JWTs. Browser
testing confirmed registration, login, subscription selection, catalog add, and
catalog removal. These checks found integration problems that a successful
compile alone would not have revealed.
