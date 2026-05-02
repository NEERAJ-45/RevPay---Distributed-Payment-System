-- ═══════════════════════════════════════════════════
--  Flyway V1 — Create wallets and ledger_entries tables
-- ═══════════════════════════════════════════════════

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS wallets (
    id          UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID           NOT NULL UNIQUE,   -- cross-service ref (no FK)
    upi_id      VARCHAR(50)    NOT NULL,
    balance     NUMERIC(15,2)  NOT NULL DEFAULT 0.00,
    created_at  TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP      NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_wallets_upi_id ON wallets(upi_id);

-- Every debit/credit is recorded here for a full audit trail
CREATE TABLE IF NOT EXISTS ledger_entries (
    id             UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    wallet_id      UUID          NOT NULL REFERENCES wallets(id),
    transaction_id UUID,                          -- cross-service ref to transactions table
    type           VARCHAR(10)   NOT NULL CHECK (type IN ('CREDIT', 'DEBIT')),
    amount         NUMERIC(15,2) NOT NULL,
    balance_after  NUMERIC(15,2) NOT NULL,
    note           VARCHAR(255),
    created_at     TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_ledger_wallet_id ON ledger_entries(wallet_id);
