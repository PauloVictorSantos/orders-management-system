# Orders Management System

## Overview
This project is an Orders Management System built with Java and Spring Boot. It provides RESTful APIs for managing orders, including creating, listing, and canceling orders. The system also integrates with Kafka for message processing.

## Technologies Used
- Java
- Spring Boot
- Maven
- Kafka
- JUnit 5
- Mockito

## Project Structure
- `src/main/java/com/moutti/orders/controller`: Contains the REST controllers.
- `src/main/java/com/moutti/orders/service`: Contains the service layer.
- `src/main/java/com/moutti/orders/repository`: Contains the repository interfaces.
- `src/main/java/com/moutti/orders/model`: Contains the data models.
- `src/main/java/com/moutti/orders/kafka`: Contains Kafka producer and consumer.
- `src/main/java/com/moutti/orders/enums`: Contains the enums used in the project.
- `src/test/java/com/moutti/orders`: Contains the unit tests.

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven
- Kafka

### Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/PauloVictorSantos/orders-management-system.git
    cd orders-management-system
    ```

2. Build the project:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

### Running Tests
To run the unit tests, use the following command:
```sh
mvn test