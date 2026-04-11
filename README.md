# bio_garden
Small backend app, that can be used to plan garden for next season.

Application will offer a REST API to manage plants, their needs and garden layout. 
It will provide a simple frontend to visualize the garden and plan planting in the future.

All Date will be stored in postgreSQL database, and the application will be using liquibase to manage database schema.

# Tech stack
- Java 17
- Spring Boot 3
- PostgreSQL
- Liquibase
- OpenAPI

# Database
Project need to have postgreSQL database running, and the connection details should be provided in `application.properties` file.
PostgeSQL version 15 or higher is recommended.
Schema need to be created before running the application, and liquibase will take care of creating tables and inserting initial data.

Password for database can be set in environment variable `DB_PASSWORD` or directly in `application.properties` file (not recommended for production).

# API
see: OpenAPI: [openapi-garden.yaml](src/main/resources/openapi/openapi-garden.yaml)

## SSL Keystore

Keystore is required to run the application with SSL enabled. You can generate a self-signed certificate using the following command:

```bash
keytool -genkeypair -alias garden -keyalg RSA -keysize 2048 \
  -storetype PKCS12 -keystore src/main/resources/keystore.p12 \
  -validity 365 -storepass your_password
```

Replace `your_password` with a secure password of your choice. This command will create a keystore file named `keystore.p12` in the `src/main/resources` directory, which the application will use for SSL configuration.

Password for keystore can be set in environment variable `SSL_KEY_PASSWORD` or directly in `application.properties` file (not recommended for production).
