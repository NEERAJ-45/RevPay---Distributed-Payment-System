package com.neeraj.upi.user.service;

import com.neeraj.upi.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UpiIdGenerator {

    private static final String DOMAIN = "@miniupi";

    private final UserRepository userRepository;

    public UpiIdGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Generates a unique UPI ID (VPA) from the user's full name.
     * Format: firstword@miniupi  e.g. neeraj@miniupi
     * If taken, appends numeric suffix: neeraj2@miniupi
     */
    public String generate(String fullName) {
        // TODO: sanitize name → lowercase first word → check uniqueness → add suffix if needed
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
