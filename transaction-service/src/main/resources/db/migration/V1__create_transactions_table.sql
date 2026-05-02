-- ══════════════════════════════════════════════════════
--  Flyway V1 — Create transactions table
-- ══════════════════════════════════════════════════════

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS transactions (
    id               UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    request_id       VARCHAR(100)   NOT NULL UNIQUE,   -- client-generated idempotency key
    sender_upi_id    VARCHAR(50)    NOT NULL,
    receiver_upi_id  VARCHAR(50)    NOT NULL,
    amount           NUMERIC(15,2)  NOT NULL,
    note             VARCHAR(255),
    status           VARCHAR(10)    NOT NULL DEFAULT 'PENDING'
                                    CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED')),
    failure_reason   VARCHAR(255),
    created_at       TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP      NOT NULL DEFAULT NOW()
);

-- Index for history queries (most recent first per sender)
CREATE INDEX IF NOT EXISTS idx_txn_sender_upi_id   ON transactions(sender_upi_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_txn_receiver_upi_id ON transactions(receiver_upi_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_txn_request_id      ON transactions(request_id);
