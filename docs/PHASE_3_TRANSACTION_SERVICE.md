# Phase 3: Transaction Orchestration & Idempotency

The **Transaction Service** is the brain of the payment operation. It coordinates the payment by verifying rules (fraud limits) and delegating the actual money movement to the Wallet Service via internal REST calls (OpenFeign).

## 📌 Key Architectural Concepts

### 1. Idempotency (Redis)
To prevent network retries from double-charging a user, the client sends a unique `requestId` (UUID). 
The service uses Redis `SETNX` (Set if Not Exists). If the key already exists, the service halts processing and returns the cached result.

### 2. Velocity / Fraud Checks
The service queries the database to see how much money the sender has transferred successfully today. If `Today's Transferred Amount + Current Request > ₹10,000`, the transaction is blocked.

## 📊 Sequence Diagram: The 6-Step Payment Flow

```mermaid
sequenceDiagram
    participant C as Client (Postman)
    participant TS as Transaction Service (8083)
    participant R as Redis (Idempotency)
    participant DB as PostgreSQL (upi_transactions)
    participant WS as Wallet Service (8082)
    participant K as Kafka Broker
    
    C->>TS: POST /transactions/pay { requestId, amount, toUpiId }
    activate TS
    
    %% Step 1
    TS->>R: 1. SETNX idempotency:{requestId}
    R-->>TS: OK (Not seen before)
    
    %% Step 2
    TS->>DB: 2. Save Txn (Status: PENDING)
    
    %% Step 3
    TS->>DB: 3. FraudEngine Check (Daily Limit < 10k)
    DB-->>TS: OK
    
    %% Step 4
    TS->>WS: 4. FeignCall: POST /internal/transfer
    WS-->>TS: 200 OK (Atomic transfer complete)
    
    %% Step 5
    TS->>DB: 5. Update Txn (Status: SUCCESS)
    
    %% Step 6
    TS->>K: 6. Publish Event (Topic: txn.completed)
    
    TS-->>C: 200 OK (TxnId, SUCCESS)
    deactivate TS
```
