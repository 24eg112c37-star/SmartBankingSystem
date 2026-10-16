CREATE TABLE password_reset_tokens (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 token_hash CHAR(64) NOT NULL UNIQUE,
 user_id BIGINT NOT NULL,
 expires_at TIMESTAMP NOT NULL,
 used_at TIMESTAMP NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_reset_user FOREIGN KEY (user_id) REFERENCES users(id),
 INDEX idx_reset_user (user_id), INDEX idx_reset_expiry (expires_at)
);
