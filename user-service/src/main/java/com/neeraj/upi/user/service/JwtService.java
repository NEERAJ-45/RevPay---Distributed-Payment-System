package com.neeraj.upi.user.service;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiry-ms}")
    private long expiryMs;

    /** Generate a signed JWT with userId, upiId, and phone as claims */
    public String generateToken(UUID userId, String upiId, String phone) {
        // TODO: use JJWT Jwts.builder(), set subject=userId, claims, expiry, sign with HS256
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** Parse and validate a JWT. Throws JwtException if invalid/expired */
    public Claims validateAndExtract(String token) {
        // TODO: use Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String extractUserId(String token) {
        // TODO: return validateAndExtract(token).getSubject()
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String extractUpiId(String token) {
        // TODO: return validateAndExtract(token).get("upiId", String.class)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean isTokenValid(String token) {
        // TODO: try validateAndExtract, catch Exception return false
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
