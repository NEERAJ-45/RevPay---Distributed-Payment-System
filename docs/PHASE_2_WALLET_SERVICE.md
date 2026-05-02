# Phase 2: Wallet Service & Ledgers

The **Wallet Service** is responsible for all money math. It does not handle passwords or JWT generation; it strictly manages account balances and maintains an immutable ledger of all transactions.

## 📌 Core Flows

### 1. Auto-Wallet Creation (Asynchronous)
When a user registers in Phase 1, the Wallet Service automatically creates a wallet for them.
*   **KafkaListener:** Listens to `user.created`.
*   **Action:** Inserts a new row in `upi_wallets` with `balance = 0.00`.

### 2. Atomic Transfers (Synchronous / Internal)
When money moves, it must be atomic (either both debit and credit succeed, or neither do).
*   The method is annotated with `@Transactional`.
*   If the sender's balance drops below ₹0, an `InsufficientFundsException` is thrown, instantly rolling back the entire database transaction.

## 📊 Sequence Diagram: Wallet Auto-Creation & Adding Money

```mermaid
sequenceDiagram
    participant K as Kafka Broker
    participant WS as Wallet Service (8082)
    participant DB as PostgreSQL (upi_wallets)
    participant C as Client (Postman)
    
    %% Auto Creation Flow
    Note over K, DB: 1. Auto-Wallet Creation (Background)
    K->>WS: Consume: user.created
    activate WS
    WS->>DB: INSERT INTO wallets (balance = 0)
    DB-->>WS: Success
    deactivate WS
    
    %% Add Money Flow
    Note over C, DB: 2. Add Money Flow (Mock Bank Top-up)
    C->>WS: POST /wallet/add-money (Amount: 500)
    activate WS
    WS->>DB: UPDATE wallets SET balance = balance + 500
    WS->>DB: INSERT INTO ledger_entries (type=CREDIT)
    DB-->>WS: Success
    WS-->>C: 200 OK (New Balance: 500)
    deactivate WS
```
