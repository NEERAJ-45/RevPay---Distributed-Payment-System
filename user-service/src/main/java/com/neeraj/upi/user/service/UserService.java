package com.neeraj.upi.user.service;

import com.neeraj.upi.user.dto.*;
import com.neeraj.upi.user.entity.User;
import com.neeraj.upi.user.repository.UserRepository;
import com.neeraj.upi.user.kafka.UserEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository     userRepository;
    private final PasswordEncoder    passwordEncoder;
    private final JwtService         jwtService;
    private final UpiIdGenerator     upiIdGenerator;
    private final UserEventPublisher eventPublisher;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        // TODO: check duplicate phone, generate UPI ID, hash PIN, save user, publish Kafka event, return JWT
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        // TODO: find user by phone, verify BCrypt PIN, generate JWT
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getByUserId(UUID userId) {
        // TODO: find user by ID, map to UserProfileResponse
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getByUpiId(String upiId) {
        // TODO: find user by UPI ID, map to UserProfileResponse
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
