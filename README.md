# Kata App

A Spring Boot application for managing products, shopping carts, and wishlists, secured with JWT authentication.

## Features
- **Authentication**: User registration and login with JWT token generation.
- **Product Management**: Create, update, delete, and list products.
- **Shopping Cart**: Add, remove, and clear products from a shopping cart.
- **Wishlist**: Add and remove products from a personal wishlist.
- **Security**: JWT-based authentication and role-based authorization.
- **Database Migrations**: Managed with Liquibase.
- **Testing**: Unit & Integration tests using Spock Framework.

## Running Locally

To run the application locally, follow these steps:

```bash
git clone https://github.com/Amine-BELLA/Product-management-app.git
cd trial
docker-compose up --build -d
```

## Database Management
- PostgreSQL is used by default.
- Liquibase manages database schema migrations.
- Docker Compose automatically sets up the database along with the application.

## Testing
- Unit and Integration tests are written using Spock and cover the core functionalities

## API Documentation
- API endpoints are documented using JavaDoc comments within the codebase, providing detailed descriptions of parameters, and expected behaviors.

## Postman Collection
To make it easier to test the API, a Postman collection is provided inside the project repository.

**Location: /postman/collection.json**

### How to Import and Use
1. Open Postman. 
2. Click Import in the top-left corner. 
3. Select the file: postman/collection.json. 
4. Once imported, you will see a collection named Kata APIs. 
5. Start by calling the **/account** endpoint to register a user. 
6. Then call the **/token** endpoint to login and retrieve a JWT token. 
7. Add the JWT token in the Authorization tab (Bearer Token) for protected endpoints (like creating products, adding to cart, etc.).

> **Note**: The app must be running locally at http://localhost:8080 (default).
You can run it using Docker Compose or from your IDE.

