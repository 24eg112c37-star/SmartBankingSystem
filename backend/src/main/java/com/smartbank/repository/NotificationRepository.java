package com.smartbank.repository;
import com.smartbank.entity.Notification; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface NotificationRepository extends JpaRepository<Notification,Long> { List<Notification> findTop50ByUserIdOrderByCreatedAtDesc(Long userId); Optional<Notification> findByIdAndUserId(Long id,Long userId); }
