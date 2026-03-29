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

# API
see: OpenAPI: [openapi-garden.yaml](src/main/resources/openapi/openapi-garden.yaml)
