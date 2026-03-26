# bio_garden
Small backend app, that can be used to plan garden for next season.

Application will offer a REST API to manage plants, their needs and garden layout. 
It will provide a simple frontend to visualize the garden and plan planting in the future.

All Date will be stored in postgreSQL database, and the application will be using liquibase to manage database schema.

# API
- `GET /plants` - Get a list of all plants in the database.
- `POST /plants` - Add a new plant to the database.
- `GET /plants/{id}` - Get details of a specific plant by its ID.
- `PUT /plants/{id}` - Update details of a specific plant by its ID.
- `DELETE /plants/{id}` - Remove a specific plant from the database by its ID.
