# Money App [![Build Status](https://travis-ci.org/JuniorMiqueletti/money-app-api.svg?branch=master)](https://travis-ci.org/JuniorMiqueletti/money-app-api)

## Specs

* /money-api - Server Side Application

### Technologies

* Java 8
* Spring Framework _(Boot, Data, Security, Oauth, DevTools, Web)_
* Mysql
* Flyway Db Migrator
* Jackson
* Apache Commons
* JWT

Avaliable profiles:

* "dev" with H2 database to run localy.
* "prod" with Mysql database.
* "basic-security" with basic authentication.
* "oauth-security" with Oauth authentication.

You can user activate two profiles of diferents types.

Ex: "Develpoment and oahth-security"

    -Dspring.profiles.active=dev,oauth-security

#### How to use

##### Postman tests

1. Get client **access-token**_.

    ``` POST: localhost:8080/oauth/token

        BODY > x-www-form-urlencoded

        client: angular
        username: admin
        password: admin
        grant_type > password

2. Send in new requests with **HEADER Key** _"Authorization"_ with bearer token ("bearer xxxxxnnnnxxxnnnxxx-xnxnnx-")

3. Get **Refresh-token**

    ```POST: localhost:8080/oauth/token

    Authorization > Basic Auth
    username: angular
    password: @ngul@r

    BODY > x-www-form-urlencoded

    grant_type: refresh_token
    refresh_token: "refresh_token value of access-token"