package com.smartbank.entity;
import jakarta.persistence.*; import java.time.Instant;
import org.hibernate.annotations.JdbcTypeCode; import org.hibernate.type.SqlTypes;
@Entity @Table(name="password_reset_tokens") public class PasswordResetToken {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @JdbcTypeCode(SqlTypes.CHAR) @Column(name="token_hash",nullable=false,unique=true,length=64) public String tokenHash;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id",nullable=false) public User user;
 @Column(name="expires_at",nullable=false) public Instant expiresAt;
 @Column(name="used_at") public Instant usedAt;
 @Column(name="created_at",insertable=false,updatable=false) public Instant createdAt;
}
