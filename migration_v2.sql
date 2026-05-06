-- ==========================================================================
-- Labas E-Commerce — Database Migration Script
-- Apply these statements once on your MySQL database (labas_db)
-- ==========================================================================

-- 1. Add avatar_url column to clients table (for profile photo)
ALTER TABLE clients
    ADD COLUMN IF NOT EXISTS avatar_url VARCHAR(512) NULL DEFAULT NULL;

-- 2. Extend password column to hold BCrypt hashes (60 chars minimum)
ALTER TABLE users
    MODIFY COLUMN password VARCHAR(255) NOT NULL;

-- 3. Ensure categories table exists with correct structure
CREATE TABLE IF NOT EXISTS categories (
    id   INT          AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- 4. Ensure subcategories table exists
CREATE TABLE IF NOT EXISTS subcategories (
    id          INT          AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    category_id INT          NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- 5. Ensure products table has category FK columns
ALTER TABLE products
    ADD COLUMN IF NOT EXISTS category_id    INT NULL DEFAULT NULL,
    ADD COLUMN IF NOT EXISTS subcategory_id INT NULL DEFAULT NULL;

-- Add FK if not already present (safe — fails silently if exists)
-- Run these separately if needed:
-- ALTER TABLE products ADD CONSTRAINT fk_product_category    FOREIGN KEY (category_id)    REFERENCES categories(id)    ON DELETE SET NULL;
-- ALTER TABLE products ADD CONSTRAINT fk_product_subcategory FOREIGN KEY (subcategory_id) REFERENCES subcategories(id) ON DELETE SET NULL;

-- 6. Create security_log table for audit trail persistence (optional — AuditLogger uses java.util.logging by default)
CREATE TABLE IF NOT EXISTS security_log (
    id         BIGINT        AUTO_INCREMENT PRIMARY KEY,
    event_type VARCHAR(50)   NOT NULL,
    detail     VARCHAR(1000) NULL,
    ip_address VARCHAR(50)   NULL,
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================================================
-- End of migration
-- ==========================================================================
