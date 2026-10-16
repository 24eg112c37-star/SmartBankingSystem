package com.smartbank.repository;
import com.smartbank.entity.PasswordResetToken; import org.springframework.data.jpa.repository.JpaRepository; import java.time.Instant; import java.util.*;
public interface PasswordResetRepository extends JpaRepository<PasswordResetToken,Long> {
 Optional<PasswordResetToken> findByTokenHashAndUsedAtIsNull(String hash);
 void deleteByUserId(Long userId); void deleteByExpiresAtBefore(Instant time);
}
