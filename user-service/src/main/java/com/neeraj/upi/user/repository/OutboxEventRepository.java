package com.neeraj.upi.user.repository;

import com.neeraj.upi.user.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    /**
     * Find all unprocessed outbox events, ordered by creation time.
     */
    List<OutboxEvent> findByProcessedFalseOrderByCreatedAtAsc();
}
