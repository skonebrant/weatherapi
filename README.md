# Weather API

This application is a simple Weather API. It allows users to query current weather data and forecasts.

## Features

- Get current weather information for a specific location.
- Get weather forecasts for the day at hand.
- Supports multiple locations.

### Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher
- An internet connection to fetch weather data from external APIs

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/your-username/weather-api.git
    ```
2. Navigate to the project directory:
    ```sh
    cd weather-api
    ```

### Getting Started

To get started with the Weather API, follow these steps:

1. Build the application using Maven (which will also run the few tests added):
    ```sh
    mvn clean install
    ```

2. Run the application:
    ```sh
    mvn spring-boot:run
    ```

3. Access the API endpoint in your browser or via a tool like `curl` or Postman:
    ```
    https://localhost:8080/api/v1/weather?cityName={city}
    ```

Replace `{city}` with the name of the city you want to query.