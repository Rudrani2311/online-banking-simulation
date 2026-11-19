package com.bankingsim.repository;

import com.bankingsim.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySourceAccount_IdOrTargetAccount_IdOrderByTimestampDesc(Long sourceId, Long targetId);
}
