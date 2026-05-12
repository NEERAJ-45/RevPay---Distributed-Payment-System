package com.neeraj.upi.user.service;

import com.neeraj.upi.user.dto.*;
import com.neeraj.upi.user.entity.User;
import com.neeraj.upi.user.event.UserCreatedEvent;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UpiIdGenerator upiIdGenerator;
    private final UserEventPublisher eventPublisher;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByPhone(req.getPhone())) {
            throw new IllegalArgumentException("Phone number already registered");
        }
        
        String upiId = upiIdGenerator.generate(req.getFullName());
        String hashedPin = passwordEncoder.encode(req.getPin());

        User user = User.builder().fullName(req.getFullName()).phone(req.getPhone()).email(req.getEmail()).upiId(upiId).pinHash(hashedPin).isActive(true).build();

        User savedUser = userRepository.save(user);
        log.info("User Registered Successfully userId={} , upiId={}", savedUser.getId(), savedUser.getUpiId());

        String token = jwtService.generateToken(savedUser.getId(), savedUser.getUpiId(), savedUser.getPhone());
        UserCreatedEvent event = UserCreatedEvent.builder().userId(savedUser.getId()).upiId(savedUser.getUpiId()).fullName(savedUser.getUpiId()).phone(savedUser.getPhone()).createdAt(savedUser.getCreatedAt());
        eventPublisher.publishUserCreated(event);
        return AuthResponse.of(token, savedUser.getUpiId(), savedUser.getFullName());
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
