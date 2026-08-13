# GameFlix Deployment Guide

## Recommended platform

GameFlix can be deployed on Railway as two services in one project:

1. The Spring Boot application built from this repository's Dockerfile.
2. A managed MySQL database.

Railway does not run the local `docker-compose.yml` directly. It replaces the
Compose application and database services with separate Railway services that
communicate over its private network.

## Prerequisites

- The current code is committed and pushed to the GitHub repository.
- A Railway account is connected to GitHub.
- The Railway project contains a managed MySQL service.

## Dashboard setup

1. In Railway, create a new empty project.
2. Select **New**, choose **Database**, and add **MySQL**.
3. Select **New**, choose **GitHub Repo**, and select `gameflix-app`.
4. Railway should detect the root `Dockerfile` and build the Spring Boot app.
5. In the application service's **Variables** tab, add these variables using
   references to the MySQL service:

   ```text
   SPRING_DATASOURCE_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}
   SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQLUSER}}
   SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQLPASSWORD}}
   ```

   If the database service has a different name, replace `MySQL` in the
   references with its displayed Railway service name.
6. Redeploy the application after saving the variables.
7. Open the application service's **Settings** page. Under **Networking**, click
   **Generate Domain**.
8. Open the generated HTTPS URL and test registration, login, plan selection,
   catalog addition, and catalog removal.

## Deployment verification record

- **Public URL:** https://gameflix-app-production.up.railway.app
- **Deployment platform:** Railway
- **Application status:** Deployed and publicly accessible
- **Database status:** Connected to Railway MySQL
- **Verified on:** August 12, 2026
- **Verified features:** Homepage, public catalog, registration, JWT login,
  subscription persistence, catalog addition, and catalog removal

The homepage and public catalog each returned HTTP 200. A disposable account
successfully logged in, saved the Player subscription, added a temporary game,
and removed that game. This confirmed both public access and authenticated
database writes on the deployed application.

## Known deployment limitation

The JWT signing key is currently generated in memory when the application
starts. A redeploy or restart invalidates existing login tokens, so users need
to log in again. A production version should load a stable signing key from a
Railway secret variable.
