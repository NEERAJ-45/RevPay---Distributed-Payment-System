package com.neeraj.upi.user.repository;

import com.neeraj.upi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhone(String phone);

    Optional<User> findByUpiId(String upiId);

    boolean existsByPhone(String phone);

    boolean existsByUpiId(String upiId);
}
