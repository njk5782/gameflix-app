# GameFlix Dockerfile Explanation

GameFlix uses a multi-stage Dockerfile. The first stage builds the application;
the second stage contains only the runtime and finished JAR. This lets a hosting
platform build directly from GitHub without requiring the ignored local
`target` directory.

```dockerfile
FROM eclipse-temurin:17-jdk-jammy AS build
```

This begins a temporary build stage with the complete Java 17 development kit.
The stage is named `build` so the finished JAR can be copied from it later.

```dockerfile
WORKDIR /workspace
```

This selects `/workspace` as the directory used while building the project.

```dockerfile
COPY .mvn .mvn
COPY mvnw pom.xml ./
```

These instructions copy the Maven wrapper and project dependency definition
before the application source. Keeping dependency files in their own layer lets
Docker reuse downloaded dependencies when only source code changes.

```dockerfile
RUN chmod +x mvnw && ./mvnw --batch-mode dependency:go-offline
```

This makes the Maven wrapper executable and downloads dependencies needed for
the build. `--batch-mode` is appropriate for an automated container build.

```dockerfile
COPY src src
RUN ./mvnw --batch-mode package -DskipTests
```

These instructions copy the application source and package it as a Spring Boot
JAR. Tests are skipped inside the image build because the CI workflow runs the
complete test suite in its earlier Maven step.

```dockerfile
FROM eclipse-temurin:17-jre-jammy
```

This starts the smaller final stage with only the Java 17 runtime. Build tools
and temporary Maven files do not become part of the deployed image.

```dockerfile
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
```

This selects the runtime directory and copies the finished JAR from the build
stage under the stable name `app.jar`.

```dockerfile
EXPOSE 8080
```

This documents the port used by GameFlix. The application also reads the
hosting platform's `PORT` environment variable and defaults to 8080 locally.

```dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
```

This starts the packaged Spring Boot application when the container runs. The
JSON-array form passes each argument directly to Java.

## Docker Compose relationship

`docker-compose.yml` starts both the GameFlix application and MySQL for local
development. The health check prevents the application from starting before
the database is ready. The application connects to
`jdbc:mysql://mysql:3306/gameflixdb`; `mysql` is the Compose service name because
`localhost` inside the application container would refer back to that same
container. A named volume preserves database data between normal container
restarts.
