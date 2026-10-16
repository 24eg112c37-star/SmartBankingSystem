package com.smartbank.repository;
import com.smartbank.entity.BankTransaction; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface TransactionRepository extends JpaRepository<BankTransaction,Long> { List<BankTransaction> findTop100ByAccountIdOrderByCreatedAtDesc(Long accountId); List<BankTransaction> findTop100ByOrderByCreatedAtDesc(); }
