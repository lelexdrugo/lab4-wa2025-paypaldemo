# PayPal Demo Project

A comprehensive Spring Boot application demonstrating PayPal payment integration with a shopping cart system, Kafka event processing, and Kubernetes deployment.

## Overview

This project is a demonstration of how to integrate PayPal payment processing into a Spring Boot application with Kotlin. It includes:

- Product catalog management
- Shopping cart functionality
- PayPal payment processing (using PayPal Sandbox)
- Order management
- Kafka event processing
- Kubernetes deployment with Helm

## Architecture

The application follows a microservice architecture with the following components:

- **Backend**: Spring Boot application with Kotlin
- **Frontend**: React application
- **Database**: PostgreSQL for data persistence
- **Message Broker**: Kafka for asynchronous event processing
- **Monitoring**: Kafka UI for monitoring Kafka topics and messages

## Prerequisites

- JDK 21
- Gradle (wrapper included)
- PostgreSQL database
- Kafka (for event processing)
- Docker and Kubernetes (for containerized deployment)
- PayPal Developer Account (for API credentials)

## Setup and Configuration

### Database Configuration

#### Using Docker for PostgreSQL

You can quickly start a PostgreSQL database using Docker:

```bash
docker run -d \
  --name my-postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=mysecretpassword \
  -e POSTGRES_DB=postgres \
  -p 5432:5432 \
  postgres
```

This command will:
- Create a container named `my-postgres`
- Set up PostgreSQL with the username `postgres` and password `mysecretpassword`
- Create a database named `postgres`
- Map port 5432 from the container to port 5432 on your host machine

#### Database Connection

Configure the connection in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=mysecretpassword
```

Additional JPA/Hibernate settings:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### Kafka Configuration

The application uses Kafka for asynchronous event processing. Configure Kafka in `application.properties`:

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=paypal-demo
```

#### Using Docker for Kafka

You can start Kafka using Docker Compose:

```yaml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"
  
  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

### PayPal Configuration

The application integrates with PayPal Sandbox for testing. Configure your PayPal credentials in `application.properties`:

```properties
paypal.client-id=YOUR_CLIENT_ID
paypal.client-secret=YOUR_CLIENT_SECRET
paypal.currency=EUR
paypal.return-url=http://localhost:8080/api/cart/paypal/capture
paypal.cancel-url=http://localhost:8080/api/cart/paypal/cancel
```

To obtain PayPal API credentials:
1. Create a PayPal Developer account at [developer.paypal.com](https://developer.paypal.com)
2. Create a new app in the Developer Dashboard
3. Copy the Client ID and Secret for the Sandbox environment

### Building the Application

To build the application:

```bash
./gradlew build
```

### Running the Application

To run the application:

```bash
./gradlew bootRun
```

The application will be available at `http://localhost:8080`.

## API Documentation

### Product API

| Endpoint | Method | Description | Parameters | Response |
|----------|--------|-------------|------------|----------|
| `/api/products/` | GET | Get all products | None | List of ProductDTO |
| `/api/products/{id}` | GET | Get product by ID | `id` (path variable) | ProductDTO |
| `/api/products/` | POST | Add a new product | ProductDTO (request body) | Product ID (Long) |

### Cart API

| Endpoint | Method | Description | Parameters | Response |
|----------|--------|-------------|------------|----------|
| `/api/cart/add` | POST | Add product to cart | `productId` (Long), `quantity` (Int, default=1) | "Added to cart" |
| `/api/cart/` | GET | View current cart | None | Cart object |
| `/api/cart/` | DELETE | Clear cart | None | None |

### PayPal Integration API

| Endpoint | Method | Description | Parameters | Response |
|----------|--------|-------------|------------|----------|
| `/api/cart/paypal/create` | POST | Create PayPal order | None | PayPal checkout URL |
| `/api/cart/paypal/capture` | GET | Capture PayPal payment | `token` (String), `PayerID` (String) | Redirect to order page or "payment-failed" |
| `/api/cart/paypal/cancel` | GET | Cancel PayPal order | `token` (String) | Redirect to order page |
| `/api/cart/paypal/order/{token}` | GET | Get order by token | `token` (path variable) | Cart object |

### Web Controller

| Endpoint | Method | Description | Parameters | Response |
|----------|--------|-------------|------------|----------|
| `/`, `/order/**` | GET | Forward to React frontend | None | Frontend application |

## Error Handling

The application uses a global exception handler for cart-related errors:

- `CartNotFoundException`: Returns HTTP 404 with an error message
- `PayPalException`: Custom exception for PayPal-related errors

## Frontend Development

### Running the Frontend in Development Mode

To run the frontend in development mode:

1. Navigate to the frontend directory:
   ```bash
   cd paypal-demo-frontend
   ```

2. Install dependencies (if not already installed):
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

The frontend development server will start and be available at `http://localhost:5173` by default.

### Vite Proxy Configuration

The frontend uses Vite as the build tool and development server. The Vite server is configured with a proxy to forward API requests to the backend server:

```javascript
// vite.config.ts
export default defineConfig({
  plugins: [react(), tsconfigPaths()],
  server: {
    proxy: {
      '/api': 'http://localhost:8080',
      '/images': 'http://localhost:8080',
    },
  },
})
```

This configuration means:
- All requests to paths starting with `/api` are forwarded to the backend at `http://localhost:8080`
- All requests to paths starting with `/images` are forwarded to the backend at `http://localhost:8080`

This allows the frontend to make API calls as if they were on the same domain, avoiding CORS issues during development.

### Building for Production

To build the frontend for production:

```bash
npm run build
```

This will create a production-ready build in the `dist` directory, which can be served by the Spring Boot application.

## Kubernetes Deployment

The project includes a Helm chart for deploying the application to Kubernetes.

### Prerequisites

- Kubernetes cluster
- Helm 3.x
- kubectl configured to access your cluster

### Deploying with Helm

1. Update the values in `paypal-demo-chart/values.yaml` to match your environment

2. Install the chart:
   ```bash
   helm install paypal-demo ./paypal-demo-chart
   ```

3. To upgrade an existing deployment:
   ```bash
   helm upgrade paypal-demo ./paypal-demo-chart
   ```

4. To uninstall:
   ```bash
   helm uninstall paypal-demo
   ```

### Helm Chart Components

The Helm chart includes:

- Deployment for the backend application
- PostgreSQL database (using a dependency chart)
- Kafka and Zookeeper (using a dependency chart)
- Kafka UI for monitoring Kafka topics
- Ingress for accessing the application and Kafka UI

## Monitoring

### Kafka UI

The project includes Kafka UI for monitoring Kafka topics and messages. When deployed with Helm, Kafka UI is available at the configured ingress host.

## License

This project is for demonstration purposes only.