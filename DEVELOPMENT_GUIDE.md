# 🚀 Mini UPI Simulator — Phase-Wise Development Guide

This guide provides a step-by-step roadmap for implementing the business logic inside the provided stubs. Follow these phases in order, as each service builds upon the previous one.

---

## Phase 0: Infrastructure Setup
Before writing any code, ensure your environment is running.
1. Open a terminal in the root `upi/` directory.
2. Run `docker-compose up -d`.
3. Verify that PostgreSQL, Redis, Zookeeper, and Kafka are running via Docker Desktop.
4. Verify Kafka UI is accessible at `http://localhost:8090`.

---

## Phase 1: User Service & Identity (Port 8081)
**Goal:** Establish user registration, login, and JWT generation.

### Step 1.1: Core Logic
1. **`UpiIdGenerator.java`**: Implement `generate()`.
   * *Hint:* Extract the first name from `fullName`, append the last 4 digits of `phone`, and add `@miniupi`. (e.g., `neeraj1234@miniupi`).
2. **`UserService.java`**: 
   * **`register()`**: Check `userRepository.existsByPhone()`. Hash the PIN using `new BCryptPasswordEncoder(12).encode(pin)`. Save the `User`. Generate a JWT using `JwtService`. Publish `UserCreatedEvent` to Kafka.
   * **`login()`**: Find user by phone. Check if `passwordEncoder.matches(request.getPin(), user.getPinHash())`. If valid, generate a new JWT.
3. **`JwtService.java`**: Implement JJWT logic using the `jwt.secret` from your `application.yml`. Create `generateToken(String upiId, UUID userId)` and `isTokenValid(String token)`.

### Step 1.2: Security Filter
1. **`JwtAuthFilter.java`**: Extract the `Authorization` header. If it starts with `Bearer `, validate it using `JwtService`. If valid, parse the `userId` and set it in `SecurityContextHolder`.
2. **`SecurityConfig.java`**: Configure HTTP Security to disable CSRF, set session management to STATELESS, permit `/auth/**` and `/swagger-ui/**`, and add your `JwtAuthFilter` before the `UsernamePasswordAuthenticationFilter`.

### Step 1.3: Testing Phase 1
1. Run `UserServiceApplication`.
2. Open Swagger at `http://localhost:8081/swagger-ui.html`.
3. Test `POST /auth/register`. Ensure you get a 201 Created with a JWT and UPI ID.
4. Check Kafka UI (`http://localhost:8090`) to verify the `user.created` event was published.

---

## Phase 2: Wallet Service & Ledgers (Port 8082)
**Goal:** Automatically create wallets and handle atomic money transfers securely.

### Step 2.1: Wallet Creation
1. **`UserCreatedListener.java`**: In `onUserCreated`, parse the Kafka event and call `walletService.createWallet(userId, upiId)`.
2. **`WalletService.java`**: Implement `createWallet()`. Ensure it is idempotent (don't crash if the wallet already exists).

### Step 2.2: Money Mechanics
1. **`WalletService.java`**:
   * **`addMoney()`**: Find the wallet, add the amount, save the wallet. Crucially, create and save a `LedgerEntry` of type `CREDIT`.
   * **`transfer()`**: *(The most important method)*. Must be annotated with `@Transactional`. 
     * Fetch sender wallet. Throw `InsufficientFundsException` if `balance < amount`.
     * Subtract amount from sender, add to receiver.
     * Save `DEBIT` LedgerEntry for sender, `CREDIT` LedgerEntry for receiver.

### Step 2.3: Testing Phase 2
1. Run `WalletServiceApplication`.
2. Register a *new* user via User Service Swagger.
3. Check the `upi_wallets` database. A new wallet with `balance = 0.00` should exist.
4. Use Wallet Swagger (`http://localhost:8082/swagger-ui.html`) to test `POST /wallet/add-money`.
5. Verify the balance updated and a row was inserted into `ledger_entries`.

---

## Phase 3: Transaction Service & Idempotency (Port 8083)
**Goal:** Orchestrate payments, prevent double-spending on retries, and check fraud limits.

### Step 3.1: Idempotency & Fraud
1. **`IdempotencyService.java`**: Use `redisTemplate.opsForValue()`. Set the key `idempotency:{requestId}` with a 24-hour TTL.
2. **`FraudEngine.java`**: 
   * Check if `senderUpiId.equals(receiverUpiId)` -> Throw Exception.
   * Check if `amount > maxPerTxn` -> Throw Exception.
   * Call `transactionRepository.sumSuccessfulAmountSince(startOfDay)`. If total + current amount > `dailyLimit`, throw `FraudVelocityException`.

### Step 3.2: Orchestration Flow
1. **`TransactionService.java`**: Implement `pay()` using the exact 6-step flow described in the class comments:
   * Check Idempotency -> Save PENDING txn -> Run Fraud Checks -> Call `WalletFeignClient.transfer()` -> Update to SUCCESS -> Publish `txn.completed` event to Kafka.
   * Wrap the Feign call in a try/catch. If Feign throws an error, catch it, update txn status to `FAILED`, save failure reason, and publish `txn.failed` event.

### Step 3.3: Testing Phase 3
1. Run `TransactionServiceApplication`.
2. Create two users and give User A ₹5000.
3. Call `POST /transactions/pay` to send ₹100 from User A to User B.
4. **Test Idempotency**: Call the *exact same* API request again with the same `requestId`. You should get a 200 OK immediately with the old `txnId` (no new money should move).
5. **Test Fraud**: Try sending ₹15,000 (above the daily limit). It should fail with a 429 status.

---

## Phase 4: API Gateway & Notifications
**Goal:** Centralize routing, rate limiting, and asynchronous alerts.

### Step 4.1: API Gateway (Port 8080)
1. **`JwtAuthFilter.java`**: Implement reactive JWT validation. If the path is not in `PUBLIC_PATHS`, block requests without a valid token. Return `401 UNAUTHORIZED`.
2. **`GatewayConfig.java`**: Implement `KeyResolver` to extract the client IP for the Redis token bucket rate limiter.
3. **Testing Gateway**: Stop hitting individual services. Route all Postman/Swagger requests through `http://localhost:8080`.

### Step 4.2: Notifications (Port 8084)
1. **`NotificationService.java`**: Add `log.info` mock statements for SMS messages.
2. **`TransactionEventListener.java`**: Consume the `txn.completed` Kafka event. Call `NotificationService.sendDebitAlert()` for the sender and `sendCreditAlert()` for the receiver.
3. **Testing Notifications**: Run the service, make a successful payment via the Gateway, and watch the Notification Service console logs print the mock SMS alerts.

---

🎉 **Completion**: Once Phase 4 is done, you have built a fully functional, production-ready microservices architecture mimicking a real-world UPI system!
