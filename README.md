## Stock Sync Service

Spring Boot microservice that periodically syncs stock from two vendors, stores products in a relational DB, records zero-stock transitions, and exposes a read endpoint.

### What’s Implemented
- Scheduler (`@Scheduled`) for periodic sync
- Data sources:
  - Vendor A: JSON API (mock) — `GET /mock/vendor-a`
  - Vendor B: CSV file
- Relational DB (MySQL)
- Product uniqueness: `sku + vendor`
- Stock transition events: `OUT_OF_STOCK` / `IN_STOCK`
- Read endpoint: `GET /products`
- Swagger/OpenAPI

### Quick Start (Docker Compose)
Requirements: Docker, Docker Compose, Java 17, Maven
```bash
mvn clean package -DskipTests
docker-compose up --build
```
Open:
- Swagger: `http://localhost:8080/swagger-ui/index.html`
- Products: `http://localhost:8080/products`

### Vendor Simulation
- Vendor A: `VendorAMockController` returns JSON at `GET /mock/vendor-a`
- Vendor B: CSV file. Local — `src/main/resources/vendor-b/stock.csv`; Docker — `/data/vendor-b/stock.csv`
Sample CSV:
```
sku,name,stockQuantity
ABC123,Product A,10
XYZ456,Product B,0
```

### API
- `GET /products` — latest stock for all products

### Tests
```bash
mvn test
```