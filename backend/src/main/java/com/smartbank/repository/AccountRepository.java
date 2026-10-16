package com.smartbank.repository;
import com.smartbank.entity.BankAccount; import jakarta.persistence.LockModeType; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.util.*;
public interface AccountRepository extends JpaRepository<BankAccount,Long> {
 List<BankAccount> findByUserIdOrderByIdAsc(Long userId); Optional<BankAccount> findByAccountNumber(String number); long countByStatus(String status);
 @EntityGraph(attributePaths="user") @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select a from BankAccount a where a.id=:id") Optional<BankAccount> findLockedById(@Param("id") Long id);
 @EntityGraph(attributePaths="user") @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select a from BankAccount a where a.accountNumber=:number") Optional<BankAccount> findLockedByNumber(@Param("number") String number);
}
