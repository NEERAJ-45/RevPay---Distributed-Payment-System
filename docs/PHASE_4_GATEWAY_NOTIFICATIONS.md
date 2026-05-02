# Phase 4: API Gateway & Notification Service

The final phase introduces the **API Gateway** (the unified perimeter) and the **Notification Service** (the background worker).

## 📌 Components

### 1. Spring Cloud Gateway (`api-gateway`)
All external traffic hits Port `8080`.
*   **Routing:** `/auth/**` goes to user-service. `/transactions/**` goes to transaction-service.
*   **Reactive JWT Filter:** Before forwarding the request, the gateway runs reactive logic to check the JWT token.
*   **Redis Rate Limiting:** A `RequestRateLimiter` restricts the client IP to a specific number of requests per minute (Token Bucket Algorithm).

### 2. Notification Service (`notification-service`)
This service is completely decoupled. It listens to the Kafka topics (`user.created` and `txn.completed`) and acts upon them. If this service goes down, the core payment flow is NOT affected (Eventual Consistency).

## 📊 Sequence Diagram: Global Architecture Flow

```mermaid
sequenceDiagram
    participant C as Client (Postman)
    participant GW as API Gateway (8080)
    participant RED as Redis
    participant CORE as Core Services (User/Txn)
    participant K as Kafka Broker
    participant NS as Notification Svc
    
    %% Gateway Flow
    C->>GW: POST /transactions/pay
    activate GW
    
    GW->>RED: Rate Limit Check (IP)
    RED-->>GW: Allowed
    
    GW->>GW: JWT Validation Filter
    Note right of GW: If JWT valid, forward
    
    GW->>CORE: Proxy Request
    activate CORE
    CORE-->>GW: Response
    
    %% Asynchronous Flow Triggered by Core
    CORE-)K: Publish txn.completed
    deactivate CORE
    
    GW-->>C: 200 OK
    deactivate GW
    
    %% Notification Worker
    Note over K, NS: Asynchronous Processing (Non-blocking)
    K->>NS: Consume: txn.completed
    activate NS
    NS->>NS: Log SMS: "₹500 debited"
    deactivate NS
```
