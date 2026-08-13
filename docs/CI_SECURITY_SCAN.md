# GameFlix CI Security Scan

## Tool

The GitHub Actions workflow uses Aqua Security Trivy to scan the GameFlix
repository and its Java dependencies for known vulnerabilities. Trivy runs in
filesystem mode after Maven packages the application, so it can inspect the
project definition and built Java artifacts.

## Pipeline behavior

The workflow performs these steps for every push or pull request targeting
`main`:

1. Checks out the repository.
2. Configures Java 17 and Maven dependency caching.
3. Builds the project and runs all automated tests with the isolated H2 test
   database configured in `src/test/resources/application.properties`.
4. Runs a Trivy vulnerability scan.
5. Uploads the JSON security report as a workflow artifact.
6. Builds the GameFlix Docker image.

The security step reports critical vulnerabilities and fails the workflow when
it finds a critical issue that has an available fix. Unfixed findings remain a
review concern but do not make the classroom pipeline permanently fail when no
update is available.

## Report

The generated report is `trivy-report.json`. GitHub Actions uploads it under the
artifact name `trivy-security-report`, including when the scan causes the
workflow to fail.

## Why the scanner was changed

The first version used OWASP Dependency-Check. Its initial unauthenticated NVD
sync attempted to retrieve approximately 376,000 vulnerability records and
failed in GitHub Actions before it produced a report. Trivy provides the same
required automated dependency-security check without requiring a separate NVD
API key for this project.

## Test database separation

The CI test profile uses H2 so tests remain repeatable and independent of
persistent data. Railway and local Docker Compose still use MySQL at runtime.
