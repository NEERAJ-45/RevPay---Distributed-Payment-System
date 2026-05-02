package com.neeraj.upi.transaction.repository;

import com.neeraj.upi.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByRequestId(String requestId);

    /** Paginated history — all txns where user is sender OR receiver */
    @Query("""
        SELECT t FROM Transaction t
        WHERE t.senderUpiId = :upiId OR t.receiverUpiId = :upiId
        ORDER BY t.createdAt DESC
    """)
    Page<Transaction> findHistoryByUpiId(@Param("upiId") String upiId, Pageable pageable);

    /** Velocity check — total amount sent successfully today */
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t
        WHERE t.senderUpiId = :upiId
          AND t.status = 'SUCCESS'
          AND t.createdAt >= :startOfDay
    """)
    BigDecimal sumSuccessfulAmountSince(
            @Param("upiId") String upiId,
            @Param("startOfDay") Instant startOfDay);
}
