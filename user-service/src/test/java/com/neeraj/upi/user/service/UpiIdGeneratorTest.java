package com.neeraj.upi.user.service;

import com.neeraj.upi.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpiIdGeneratorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpiIdGenerator upiIdGenerator;

    // Match the actual DOMAIN from UpiIdGenerator
    private static final String DOMAIN = "@miniupi";

    @Test
    void generate_shouldCreateUpiIdFromFirstWord() {
        when(userRepository.existsByUpiId(anyString())).thenReturn(false);
        String upi = upiIdGenerator.generate("John Doe");
        assertEquals("john" + DOMAIN, upi);
    }

    @Test
    void generate_shouldStripSpecialCharacters() {
        when(userRepository.existsByUpiId(anyString())).thenReturn(false);
        String upi = upiIdGenerator.generate("Jack & Jill");
        assertEquals("jack" + DOMAIN, upi);
    }

    @Test
    void generate_shouldAddSuffixWhenConflict() {
        when(userRepository.existsByUpiId("john" + DOMAIN)).thenReturn(true);
        when(userRepository.existsByUpiId("john2" + DOMAIN)).thenReturn(false);
        String upi = upiIdGenerator.generate("John");
        assertEquals("john2" + DOMAIN, upi);
    }

    @Test
    void generate_shouldIncrementSuffixUntilUnique() {
        when(userRepository.existsByUpiId("john" + DOMAIN)).thenReturn(true);
        when(userRepository.existsByUpiId("john2" + DOMAIN)).thenReturn(true);
        when(userRepository.existsByUpiId("john3" + DOMAIN)).thenReturn(true);
        when(userRepository.existsByUpiId("john4" + DOMAIN)).thenReturn(false);
        String upi = upiIdGenerator.generate("John");
        assertEquals("john4" + DOMAIN, upi);
    }

    @Test
    void generate_shouldThrowExceptionWhenNameHasNoAlphanumeric() {
        assertThrows(IllegalArgumentException.class,
                () -> upiIdGenerator.generate("!!!"));
    }

    @Test
    void generate_shouldFallbackToFullSanitizedNameWhenFirstWordHasNoLetters() {
        when(userRepository.existsByUpiId(anyString())).thenReturn(false);
        // First word "!!!" has no alphanumeric → fallback to fully sanitized "john"
        String upi = upiIdGenerator.generate("!!! John");
        assertEquals("john" + DOMAIN, upi);
    }
}