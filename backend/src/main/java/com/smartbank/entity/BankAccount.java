package com.smartbank.entity;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.Instant;
@Entity @Table(name="bank_accounts") public class BankAccount {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(name="account_number",nullable=false,unique=true) public String accountNumber;
 @Column(name="account_type",nullable=false) public String accountType;
 @Column(nullable=false) public String status="ACTIVE";
 @Column(nullable=false,precision=19,scale=2) public BigDecimal balance=BigDecimal.ZERO;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id",nullable=false) public User user;
 @Column(name="created_at",insertable=false,updatable=false) public Instant createdAt;
}
