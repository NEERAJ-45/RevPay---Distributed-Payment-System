-- ═════════════════════════════════════════════════
--  Flyway Migration V2 — Create outbox_events table
-- ═════════════════════════════════════════════════

CREATE TABLE IF NOT EXISTS outbox_events (
    id             UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_id   VARCHAR(255) NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    event_type     VARCHAR(100) NOT NULL,
    payload        TEXT         NOT NULL,
    is_processed   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW(),
    processed_at   TIMESTAMP
);

-- Optimize polling query by placing an index on unprocessed events
CREATE INDEX IF NOT EXISTS idx_outbox_unprocessed ON outbox_events(is_processed) WHERE is_processed = FALSE;
