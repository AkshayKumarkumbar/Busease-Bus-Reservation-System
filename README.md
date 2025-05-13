# Busease - Bus Reservation System

A comprehensive bus reservation web application built with Java Spring Boot, designed for college projects. Busease offers a user-friendly interface for booking bus tickets with a simplified guest booking flow and an admin dashboard for managing buses and routes.

## Features

- Modern, responsive UI with gradient design
- Simplified guest booking flow (no login required)
- Search for bus routes by origin, destination, and date
- Interactive seat selection
- Admin dashboard for managing buses and routes
- Support for both H2 in-memory database and MySQL

## Project Structure

The application follows the standard Spring Boot MVC architecture:

- **Controllers**: Handle HTTP requests and responses
- **Services**: Contain business logic
- **Repositories**: Interface with the database
- **Models**: Define the data entities
- **Templates**: Thymeleaf HTML templates for the UI

## Running the Application

### In Replit

The application is configured to run on port 5000 in Replit:

1. Just press the "Run" button in Replit
2. The application will be available at the provided URL

### In a Local IDE (IntelliJ, Eclipse, etc.)

1. Clone the repository to your local machine
2. Open the project in your IDE
3. Run the application using the IDE's run feature or `./mvnw spring-boot:run`
4. Access the application at `http://localhost:8080`

### Using MySQL Database (for local development)

By default, the application uses an H2 in-memory database. To use MySQL:

1. Open `src/main/resources/application.properties`
2. Comment out the H2 database configuration section
3. Uncomment the MySQL configuration section
4. Update the MySQL credentials as needed
5. Make sure MySQL is running on your machine

## Database Schema

The application uses the following main entities:

- **User**: Application users (admin or customer)
- **Bus**: Bus details including capacity and type
- **Route**: Bus routes with origin, destination, dates, and fares
- **Booking**: Booking information linking passengers to routes
- **Seat**: Individual seats on buses with availability status
- **Passenger**: Guest passenger details for bookings

## Technologies Used

- **Backend**: Java 11, Spring Boot 2.7
- **Frontend**: HTML5, CSS3, JavaScript, Thymeleaf
- **Database**: H2 (in-memory) and MySQL support
- **Security**: Spring Security (simplified for learning)
- **Build Tool**: Maven

## Notes for Students

This project is designed to be a learning resource for college students. It intentionally:

1. Uses a simplified authentication system with guest bookings
2. Implements a clear, easy-to-understand code structure
3. Includes detailed comments throughout the code
4. Provides flexible database options (H2 in-memory for quick testing, MySQL for production-like environments)
5. Uses basic Spring Security configuration for easier understanding