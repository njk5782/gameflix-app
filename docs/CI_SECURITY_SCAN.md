# GameFlix CI Security Scan

## Tool

The GitHub Actions workflow uses OWASP Dependency-Check to inspect the Maven
dependencies used by the GameFlix Spring Boot application. The scan compares
the project's libraries with published vulnerability data.

## Pipeline behavior

The workflow performs these steps for every push or pull request targeting
`main`:

1. Checks out the repository.
2. Configures Java 17 and Maven dependency caching.
3. Builds the project and runs all automated tests with the isolated H2 test
   database configured in `src/test/resources/application.properties`.
4. Runs OWASP Dependency-Check.
5. Uploads HTML and JSON security reports as a workflow artifact.
6. Builds the GameFlix Docker image.

The security step is configured to fail when it finds a vulnerability with a
CVSS score of 8.0 or higher. This threshold catches high- and
critical-severity findings while allowing the report to document lower-risk
items for later review.

## Reports

The generated reports are:

- `target/dependency-check-report.html`
- `target/dependency-check-report.json`

GitHub Actions uploads both files under the artifact name
`dependency-check-report`, even when the scan causes the workflow to fail.

## Why this matters

Application code can be secure while still depending on an unsafe library.
Automating this scan helps detect known problems whenever dependencies or
application code are pushed. A reported vulnerability must still be reviewed
to determine whether it affects GameFlix and whether the dependency should be
updated, removed, or documented as a false positive.

The CI test profile does not override its H2 driver with production MySQL
connection values. Railway and local Docker Compose still use MySQL at runtime;
H2 keeps automated tests repeatable and independent of persistent data.
