# GameFlix Authentication Attack Log

## Purpose

I tested protected GameFlix endpoints with missing and invalid JSON Web
Tokens (JWTs). The goal was to verify that an unauthenticated request cannot
change a user's subscription or modify the game catalog.

## Test 1: Missing authentication token

- Request: `PUT /api/subscription`
- Authorization header: Not provided
- Request body: `{"plan":"FAMILY"}`
- Expected status: `401 Unauthorized`
- Expected response: `{"message":"Login required"}`
- Result: Passed

This test represents a user attempting to change a subscription without
logging in. The JWT interceptor checks protected requests before the
controller runs. Because the request has no Bearer token, it is rejected and
the database is not changed.

## Test 2: Invalid authentication token

- Request: `DELETE /api/movies/1`
- Authorization header: `Bearer this-is-not-a-valid-jwt`
- Expected status: `401 Unauthorized`
- Expected response: `{"message":"Invalid or expired token"}`
- Result: Passed

This test represents a user attempting to bypass authentication with a fake
token. The application tries to verify the JWT signature and structure. Token
validation fails, so the delete operation never reaches the catalog
controller and the game remains in the database.

## Valid authentication comparison

I also logged in through `POST /api/login`, saved the returned JWT as a
Postman collection variable, and sent it in the
`Authorization: Bearer {{jwtToken}}` header. The protected subscription
request succeeded with `200 OK`, confirming that valid and invalid requests
are handled differently.

## Known limitation

The current prototype creates its JWT signing key when the application starts.
This avoids storing a secret in source control, but restarting the application
invalidates existing tokens. A deployed version should load a strong signing
secret from a protected environment variable or secret manager.

## Evidence

The collection `postman/GameFlix.postman_collection.json` contains both
failed-auth requests and automated Postman assertions. The same two cases are
also covered by `AuthSecurityIntegrationTest`.
