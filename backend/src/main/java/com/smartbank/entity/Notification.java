package com.smartbank.entity;
import jakarta.persistence.*; import java.time.Instant;
@Entity @Table(name="notifications") public class Notification {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false) public String title;
 @Column(nullable=false) public String message;
 @Column(name="is_read",nullable=false) public boolean read=false;
 @Column(name="created_at",insertable=false,updatable=false) public Instant createdAt;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id",nullable=false) public User user;
}
