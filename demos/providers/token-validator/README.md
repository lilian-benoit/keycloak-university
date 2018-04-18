# Token Validator for Keycloak

Adds a realm resource that allows parsing and verifying tokens.

## Requirements 
Keycloak 4.0.0

## Usage

1. Deploy to Keycloak:

    mvn clean install wildfly:deploy

2. Open <Keycloak URL>/realms/<Realm Name>/token-validator. For example http://localhost:8080/auth/realms/master/jwt.

3. Paste token into token textarea and click submit

## Source 
https://github.com/stianst/keycloak-experimental/tree/master/token-validation