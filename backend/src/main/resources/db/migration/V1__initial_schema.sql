CREATE TABLE users (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, full_name VARCHAR(120) NOT NULL, email VARCHAR(190) NOT NULL UNIQUE,
 phone VARCHAR(30) NOT NULL, password_hash VARCHAR(100) NOT NULL, role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
 active BOOLEAN NOT NULL DEFAULT TRUE, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE bank_accounts (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, account_number VARCHAR(24) NOT NULL UNIQUE, account_type VARCHAR(20) NOT NULL,
 status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,
 user_id BIGINT NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES users(id), INDEX idx_account_user (user_id),
 CONSTRAINT chk_account_balance CHECK (balance >= 0)
);
CREATE TABLE bank_transactions (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, reference VARCHAR(40) NOT NULL UNIQUE, type VARCHAR(24) NOT NULL,
 amount DECIMAL(19,2) NOT NULL, status VARCHAR(20) NOT NULL, description VARCHAR(255),
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, account_id BIGINT NOT NULL,
 related_account_id BIGINT NULL, CONSTRAINT fk_tx_account FOREIGN KEY (account_id) REFERENCES bank_accounts(id),
 CONSTRAINT fk_tx_related FOREIGN KEY (related_account_id) REFERENCES bank_accounts(id), INDEX idx_tx_account_date (account_id, created_at),
 CONSTRAINT chk_tx_amount CHECK (amount > 0)
);
