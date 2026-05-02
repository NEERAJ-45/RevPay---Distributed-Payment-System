-- ═══════════════════════════════════════════
--  Flyway Migration V1 — Create users table
-- ═══════════════════════════════════════════

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS users (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name   VARCHAR(100) NOT NULL,
    phone       VARCHAR(15)  NOT NULL UNIQUE,
    email       VARCHAR(100) UNIQUE,
    upi_id      VARCHAR(50)  NOT NULL UNIQUE,
    pin_hash    VARCHAR(255) NOT NULL,
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_users_phone  ON users(phone);
CREATE INDEX IF NOT EXISTS idx_users_upi_id ON users(upi_id);
