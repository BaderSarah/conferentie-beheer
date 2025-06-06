# Conference Event Management Application

This is a Spring Boot web application for managing conference events, including the ability to create, view, update, delete, and mark events as favorites. It supports user interaction, event scheduling, and room availability checks.

## Features

- Create, retrieve, update, and delete events
- Assign and manage speakers to events
- Assign and manage event locations (rooms)
- Mark events as favorites (max 5 per user)
- Filter events by date
- Prevent overlapping events in the same room
- Avoid duplicate events with the same name and date

## Technologies

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- H2 / MySQL (configurable)
- Lombok
- JUnit & Mockito for testing
- Jackson (JSON processing)

## API Endpoints

### Event Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/rest/events` | Get all events |
| `GET` | `/rest/speakers` | Get all speakers |
| `GET` | `/rest/rooms` | Get all rooms |
| `GET` | `/rest/events/{id}` | Get a specific event by ID |
| `GET` | `/rest/speakers/{id}` | Get a specific speaker by ID |
| `GET` | `/rest/rooms/{id}` | Get a specific room by ID |
| `GET` | `/rest/events/bydate/{date}` | Get events by date (ISO format: yyyy-MM-dd) |
| `POST` | `/rest/events/create` | Create a new event |
| `POST` | `/rest/speakers/create` | Create a new speaker |
| `POST` | `/rest/rooms/create` | Create a new room |
| `DELETE` | `/rest/events/delete/{id}` | Delete an event by ID |
| `DELETE` | `/rest/speakers/delete/{id}` | Delete an speaker by ID |
| `DELETE` | `/rest/rooms/delete/{id}` | Delete an room by ID |

### Favorite Endpoints (within service layer)

- Add, remove, and clear favorite events per user
- Favorites are limited to 5 per user

## Running the App

1. Clone the repository:
 ```bash
 git clone https://github.com/your-username/conference-app.git
 cd conference-app
 ```
3. Build and run the application:
```
./mvnw spring-boot:run
```
3. Access the application at:
```
http://localhost:8080/
```

