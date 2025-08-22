# Stock Sync Service

Spring Boot service syncing product stock from:
- Vendor A: REST API
- Vendor B: CSV file (`src/main/resources/vendor-b/stock.csv`)

Features:
- Scheduled sync (`@Scheduled`)
- H2 DB persistence
- Detect out-of-stock transitions
- REST: `GET /products`
- Swagger UI

Run:
```bash
mvn clean install
mvn spring-boot:run