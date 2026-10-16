package com.smartbank.entity;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.Instant;
@Entity @Table(name="bank_transactions") public class BankTransaction {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,unique=true) public String reference;
 @Column(nullable=false) public String type;
 @Column(nullable=false,precision=19,scale=2) public BigDecimal amount;
 @Column(nullable=false) public String status="COMPLETED";
 public String description;
 @Column(name="created_at",insertable=false,updatable=false) public Instant createdAt;
 @ManyToOne @JoinColumn(name="account_id",nullable=false) public BankAccount account;
 @ManyToOne @JoinColumn(name="related_account_id") public BankAccount relatedAccount;
}
