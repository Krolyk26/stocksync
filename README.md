# Stock Sync Service API

## Project Description

The **Stock Sync Service** is a REST API built with **Spring Boot** designed to automatically synchronize product stock data from various vendors. The application runs in the background, periodically fetching up-to-date product information and updating a local database. It also focuses on tracking critical changes: the service logs a separate database event each time a product runs out of stock.

## Technologies Used

* **Java 17** & **Spring Boot 3.x** as the core framework
* **Spring Data JPA** for efficient data access
* **Lombok** to minimize boilerplate code (getters, setters, constructors)
* **MySQL 8.0** as the main relational database
* **Docker** & **Docker Compose** for easy containerized deployment
* **JUnit 5** & **Mockito** for reliable testing
* **Maven** for build automation and dependency management

---

## Architecture and Implementation

The project is built on the principles of clean architecture, using a classic three-tier approach: **Controller - Service - Repository**.

* **Controllers (`ProductController`, `VendorAMockController`)** act as the application's "facade." They handle HTTP requests, validate incoming data (using DTOs), and return standardized responses (`ResponseEntity`). They are kept "thin," containing no business logic.

* **Services (`StockSyncService`, `ProductService`, `VendorAClient`, `VendorBClient`)** encapsulate the core business logic. The `StockSyncService` is the project's heart, responsible for scheduling and executing the synchronization process. Clients (`VendorAClient`, `VendorBClient`) handle interactions with external data sources (a REST API and a CSV file) and transform the data into a unified internal DTO format.

* **Repositories (`ProductRepository`, `StockEventRepository`)** provide an abstraction layer for database operations, offering methods for standard CRUD (Create, Read, Update, Delete) operations and more complex queries.

### Key Architectural Decisions

* **Unified Data Flow**: Data from both vendors, despite their different formats (JSON and CSV), is transformed into a single internal format (`ProductDTO`), which simplifies the processing logic within the `StockSyncService`.
* **Asynchronous Synchronization**: The use of the **`@Scheduled`** annotation allows the synchronization process to run automatically at defined intervals without blocking the main application thread.
* **Robust Error Handling**: A centralized exception handler using **`@ControllerAdvice`** provides a uniform response format for exceptions like validation errors or internal server errors.
* **Atomic Operations**: The synchronization method is annotated with **`@Transactional`**, ensuring that all related database operations are executed as a single, atomic transaction. This prevents data corruption in case of failures.

---

## Getting Started

The easiest way to run the project is by using **Docker Compose**. Make sure you have Docker installed on your system.

1.  **Build the Project**: Open your terminal in the project's root directory and run the Maven command to build the project and create the executable JAR file.

    ```bash
    mvn clean package
    ```

2.  **Run Containers**: After a successful build, start the application and the database by executing the Docker Compose command.

    ```bash
    docker-compose up --build
    ```

3.  **Verification**: The application will be accessible at `http://localhost:8080`.
    * **API Documentation**: `http://localhost:8080/swagger-ui.html`
    * **Products Endpoint**: `http://localhost:8080/products`

---

## Testing

The project includes both unit and integration tests. To run all tests, execute the following command:

```bash
mvn test