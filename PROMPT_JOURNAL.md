# GameFlix Prompt Journal

This journal records the significant AI-assisted prompts that affected the
design or implementation of GameFlix. It was reconstructed from the retained
project conversation on August 12, 2026. The prompt summaries below are
paraphrases rather than exact transcripts.

## Tools used

- OpenAI ChatGPT/Codex for code suggestions, debugging, documentation, and test
  planning.
- IntelliJ IDEA for reviewing and editing the project.
- Maven, JUnit, Docker Compose, Postman, and GitHub Actions for independent
  verification.

## Significant prompts and decisions

### 1. Review the project and identify the remaining work

**Prompt:** Review the complete GameFlix assignment and help me finish every
remaining requirement step by step.

**AI output:** A staged plan covering the frontend, REST endpoints, JWT
authentication, subscriptions, testing, security scanning, documentation, and
the final demo.

**What I changed and why:** I clarified that GameFlix is primarily a video game
subscription service, not a movie service. I kept some existing backend class
and endpoint names such as `Movie` and `/api/movies` to avoid an unnecessary
database and API migration, but changed the user-facing language to games.

**Verification:** I compared the plan with the full project description and
worked through each requirement separately.

### 2. Add a readable frontend to the Spring Boot application

**Prompt:** Where is the frontend, and can it be added in a way that looks like
code I can read and explain rather than generated source?

**AI output:** A static HTML, CSS, and JavaScript frontend served by Spring Boot,
including account forms, membership choices, and a game catalog.

**What I changed and why:** I requested normally formatted source files instead
of compressed one-line files. I also chose the GameFlix wording, three plan
names, prices, and the simpler login message `Login successful` so the interface
matched my project idea and was easy to demonstrate.

**Verification:** I opened `http://localhost:8080`, tested the forms in Chrome,
and visually checked the responsive layout.

### 3. Connect catalog actions to the backend

**Prompt:** Add a feature that lets a user remove games from the game catalog.

**AI output:** A DELETE endpoint, service method, remove buttons, authorization
headers, and frontend refresh behavior.

**What I changed and why:** I kept catalog reading public but required a logged-in
user for changes. This allows visitors to browse while preventing anonymous
users from adding or deleting records.

**Verification:** I added and removed a game from the browser and tested service
success and missing-record cases with JUnit.

### 4. Diagnose and correct Docker Compose startup

**Prompt:** The `compose` command was not found when I ran it. How do I start the
application?

**AI output:** The correct modern command, `docker compose up --build -d`, plus
the required build order.

**What I changed and why:** I used `docker compose` rather than the nonexistent
standalone `compose` command. After source changes, I rebuilt the JAR before the
Docker image so the container would include the current application.

**Verification:** Docker reported that the MySQL container was healthy and the
Spring Boot container started. I then loaded the application on port 8080.

### 5. Add JWT authentication and protected subscription handling

**Prompt:** Complete the next security step by adding JWT login, protecting
changes, and saving the selected membership plan.

**AI output:** JWT creation and validation, an authentication interceptor,
protected subscription endpoints, token-aware frontend requests, and
subscription persistence on the user record.

**What I changed and why:** I chose to generate the signing key at application
startup instead of storing a secret in source control. I also required
authentication for subscription access and catalog modifications while leaving
catalog browsing public.

**Verification:** Integration tests confirmed that a valid token works and a
missing token returns HTTP 401. Subscription service tests covered valid plans,
invalid plans, missing users, and stored-plan retrieval.

**Known limitation:** Restarting the application invalidates existing tokens
because the signing key is generated in memory. A deployed version should load
the key securely from an environment variable or secret manager.

### 6. Debug the membership selection login problem

**Prompt:** Login says it is successful, but choosing another plan still says
`Login required`.

**AI output:** A correction to token handling and authorization headers in the
frontend.

**What I changed and why:** I tested the fix repeatedly in the actual browser and
requested that the success message remain simply `Login successful`. The plan
selection now sends the JWT and saves the selected plan to the backend.

**Verification:** After logging in, I selected the Pro plan and received `PRO
plan saved` instead of the earlier authentication error.

### 7. Create API security checks in Postman

**Prompt:** Help me run and verify the API requests in Postman, including failed
authentication attempts.

**AI output:** A Postman collection covering registration, login, JWT capture,
subscription save/read, public catalog access, a missing-token request, and an
invalid-token request.

**What I changed and why:** I corrected the collection's `invalidToken` variable
after the first runner attempt used an empty value and returned the missing-token
message. This made the second attack test exercise malformed-token validation
instead of duplicating the missing-token case.

**Verification:** The final Postman collection run showed four passing
assertions, zero failures, successful authorized requests, and HTTP 401 for both
attack cases.

### 8. Add a security-scanning step to CI

**Prompt:** Complete the CI security requirement and document how it works.

**AI output:** OWASP Dependency-Check in the GitHub Actions workflow, HTML and
JSON report uploads, and a CVSS failure threshold.

**What I changed and why:** I set the build to fail at CVSS 8 or higher so serious
dependency findings cannot pass unnoticed. Reports upload even if the scan
fails, making the cause available for review.

**Verification:** The workflow YAML and effective Maven configuration validated,
and all 13 JUnit tests still passed. The initial local vulnerability database
download was intentionally stopped because an unauthenticated first sync is
very large; the configured GitHub Actions run will perform the complete scan.

### 9. Prepare and verify public deployment

**Prompt:** Deploy GameFlix from my GitHub repository on Railway and confirm the
public application works with MySQL.

**AI output:** A multi-stage Dockerfile that builds from source, cloud-port
support, Railway MySQL variable instructions, and deployment documentation.

**What I changed and why:** I connected my GitHub repository to a new Railway
project, added the managed MySQL service, configured its referenced credentials,
and generated the public domain. The Dockerfile was changed because the earlier
version expected a local `target` folder that does not exist in a clean GitHub
deployment.

**Verification:** The production image built locally, and the public homepage
and catalog returned HTTP 200. A deployed test completed registration, JWT
login, subscription saving, game addition, and game removal against Railway
MySQL. The temporary catalog record was removed after verification.

**CI correction:** The first deployment commit exposed a test configuration
conflict: CI supplied a MySQL URL while the test profile selected the H2 driver.
I reproduced the failure locally and removed the unused CI MySQL overrides so
the 13 automated tests use their isolated H2 database. Railway continues to use
MySQL for the deployed application.

## Overall review and accountability

AI accelerated scaffolding, repetitive code, test ideas, and troubleshooting,
but I reviewed the files, chose the product behavior, tested the application in
the browser, ran backend tests, ran the Postman attack cases, and corrected
problems found during integration. The most important example of misleading or
incomplete AI-assisted behavior was the login flow initially displaying success
without making the JWT available to plan selection. Browser testing exposed the
problem, and the token flow was corrected before the feature was accepted.
