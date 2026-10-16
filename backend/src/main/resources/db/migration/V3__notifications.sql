CREATE TABLE notifications (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 title VARCHAR(120) NOT NULL,
 message VARCHAR(255) NOT NULL,
 is_read BOOLEAN NOT NULL DEFAULT FALSE,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 user_id BIGINT NOT NULL,
 CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(id),
 INDEX idx_notification_user_date (user_id, created_at)
);
