-- ═══════════════════════════════════════════════════════
--  PostgreSQL initialization script
--  Creates 3 separate databases for each service
--  Runs automatically on first container start
-- ═══════════════════════════════════════════════════════

CREATE DATABASE upi_users;
CREATE DATABASE upi_wallets;
CREATE DATABASE upi_transactions;

-- Grant all privileges to the admin user
GRANT ALL PRIVILEGES ON DATABASE upi_users TO upi_admin;
GRANT ALL PRIVILEGES ON DATABASE upi_wallets TO upi_admin;
GRANT ALL PRIVILEGES ON DATABASE upi_transactions TO upi_admin;
