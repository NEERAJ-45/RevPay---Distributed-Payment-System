# Phase-Wise Implementation Summary for Mini UPI Simulator

This file provides a concise, actionable checklist for each development phase to help you complete the project modularly.

## Phase 0: Infrastructure Setup
- [ ] Run `docker-compose up -d` in the root `upi/` directory
- [ ] Verify PostgreSQL, Redis, Zookeeper, and Kafka are running via Docker Desktop
- [ ] Verify Kafka UI is accessible at `http://localhost:8090`

## Phase 1: User Service & Identity (Port 8081)
### Step 1.1: Core Logic
- [ ] Implement `UpiIdGenerator.java::generate()` - Extract first name from fullName, append last 4 digits of phone, add `@miniupi`
- [ ] Implement `UserService.java::register()`:
  - [ ] Check `userRepository.existsByPhone()`
  - [ ] Hash PIN using `new BCryptPasswordEncoder(12).encode(pin)`
  - [ ] Save the User
  - [ ] Generate JWT using `JwtService`
  - [ ] Publish `UserCreatedEvent` to Kafka
- [ ] Implement `UserService.java::login()`:
  - [ ] Find user by phone
  - [ ] Check if `passwordEncoder.matches(request.getPin(), user.getPinHash())`
  - [ ] If valid, generate new JWT
- [ ] Implement `JwtService.java`:
  - [ ] Create `generateToken(String upiId, UUID userId)` using JJWT
  - [ ] Create `isTokenValid(String token)` using JJWT

### Step 1.2: Security Filter
- [ ] Implement `JwtAuthFilter.java`:
  - [ ] Extract Authorization header
  - [ ] If starts with `Bearer `, validate using `JwtService`
  - [ ] If valid, parse userId and set in `SecurityContextHolder`
- [ ] Configure `SecurityConfig.java`:
  - [ ] Disable CSRF
  - [ ] Set session management to STATELESS
  - [ ] Permit `/auth/**` and `/swagger-ui/**`
  - [ ] Add `JwtAuthFilter` before `UsernamePasswordAuthenticationFilter`

### Step 1.3: Testing Phase 1
- [ ] Run `UserServiceApplication`
- [ ] Open Swagger at `http://localhost:8081/swagger-ui.html`
- [ ] Test `POST /auth/register` - Ensure 201 Created with JWT and UPI ID
- [ ] Check Kafka UI (`http://localhost:8090`) - Verify `user.created` event published

## Phase 2: Wallet Service & Ledgers (Port 8082)
### Step 2.1: Wallet Creation
- [ ] Implement `UserCreatedListener.java::onUserCreated`:
  - [ ] Parse Kafka event
  - [ ] Call `walletService.createWallet(userId, upiId)`
- [ ] Implement `WalletService.java::createWallet()`:
  - [ ] Ensure idempotent (don't crash if wallet already exists)

### Step 2.2: Money Mechanics
- [ ] Implement `WalletService.java::addMoney()`:
  - [ ] Find wallet
  - [ ] Add amount
  - [ ] Save wallet
  - [ ] Create and save `LedgerEntry` of type `CREDIT`
- [ ] Implement `WalletService.java::transfer()`:
  - [ ] Annotate with `@Transactional`
  - [ ] Fetch sender wallet
  - [ ] Throw `InsufficientFundsException` if `balance < amount`
  - [ ] Subtract amount from sender, add to receiver
  - [ ] Save `DEBIT` LedgerEntry for sender
  - [ ] Save `CREDIT` LedgerEntry for receiver

### Step 2.3: Testing Phase 2
- [ ] Run `WalletServiceApplication`
- [ ] Register a *new* user via User Service Swagger
- [ ] Check `upi_wallets` database - New wallet with `balance = 0.00` should exist
- [ ] Use Wallet Swagger (`http://localhost:8082/swagger-ui.html`) to test `POST /wallet/add-money`
- [ ] Verify balance updated and row inserted into `ledger_entries`

## Phase 3: Transaction Service & Idempotency (Port 8083)
### Step 3.1: Idempotency & Fraud
- [ ] Implement `IdempotencyService.java`:
  - [ ] Use `redisTemplate.opsForValue()`
  - [ ] Set key `idempotency:{requestId}` with 24-hour TTL
- [ ] Implement `FraudEngine.java`:
  - [ ] Check if `senderUpiId.equals(receiverUpiId)` -> Throw Exception
  - [ ] Check if `amount > maxPerTxn` -> Throw Exception
  - [ ] Call `transactionRepository.sumSuccessfulAmountSince(startOfDay)`
  - [ ] If total + current amount > `dailyLimit`, throw `FraudVelocityException`

### Step 3.2: Orchestration Flow
- [ ] Implement `TransactionService.java::pay()` using exact 6-step flow:
  - [ ] Check Idempotency
  - [ ] Save PENDING txn
  - [ ] Run Fraud Checks
  - [ ] Call `WalletFeignClient.transfer()`
  - [ ] Update to SUCCESS
  - [ ] Publish `txn.completed` event to Kafka
- [ ] Wrap Feign call in try/catch:
  - [ ] If Feign throws error:
    - [ ] Catch it
    - [ ] Update txn status to `FAILED`
    - [ ] Save failure reason
    - [ ] Publish `txn.failed` event

### Step 3.3: Testing Phase 3
- [ ] Run `TransactionServiceApplication`
- [ ] Create two users and give User A ₹5000
- [ ] Call `POST /transactions/pay` to send ₹100 from User A to User B
- [ ] Test Idempotency:
  - [ ] Call exact same API request again with same `requestId`
  - [ ] Should get 200 OK immediately with old `txnId` (no new money moved)
- [ ] Test Fraud:
  - [ ] Try sending ₹15,000 (above daily limit)
  - [ ] Should fail with 429 status

## Phase 4: API Gateway & Notifications
### Step 4.1: API Gateway (Port 8080)
- [ ] Implement `JwtAuthFilter.java`:
  - [ ] Implement reactive JWT validation
  - [ ] If path not in `PUBLIC_PATHS`, block requests without valid token
  - [ ] Return `401 UNAUTHORIZED`
- [ ] Implement `GatewayConfig.java`:
  - [ ] Implement `KeyResolver` to extract client IP for Redis token bucket rate limiter
- [ ] Test Gateway:
  - [ ] Stop hitting individual services
  - [ ] Route all Postman/Swagger requests through `http://localhost:8080`

### Step 4.2: Notifications (Port 8084)
- [ ] Implement `NotificationService.java`:
  - [ ] Add `log.info` mock statements for SMS messages
- [ ] Implement `TransactionEventListener.java`:
  - [ ] Consume `txn.completed` Kafka event
  - [ ] Call `NotificationService.sendDebitAlert()` for sender
  - [ ] Call `NotificationService.sendCreditAlert()` for receiver
- [ ] Test Notifications:
  - [ ] Run the service
  - [ ] Make successful payment via Gateway
  - [ ] Watch Notification Service console logs for mock SMS alerts

## Completion Checklist
- [ ] All services running on their respective ports:
  - User Service: 8081
  - Wallet Service: 8082
  - Transaction Service: 8083
  - API Gateway: 8080
  - Notification Service: 8084
- [ ] End-to-end flow working:
  1. Register user via API Gateway
  2. Login to get JWT
  3. Add money to wallet
  4. Transfer money to another user
  5. Receive notification alerts
- [ ] Verify all events are properly published to Kafka and consumed by respective services
- [ ] Check database tables for correct data population:
  - users
  - upi_wallets
  - ledger_entries
  - transactions

🎉 Once all phases are complete, you will have built a fully functional, production-ready microservices architecture mimicking a real-world UPI system!