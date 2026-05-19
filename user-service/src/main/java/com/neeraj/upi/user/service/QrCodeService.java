    package com.neeraj.upi.user.service;

    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;

    @Service
    @Slf4j
    public class QrCodeService {

        private static final int QR_SIZE = 300;

        /**
         * Generates a Base64-encoded PNG QR code for the given UPI ID.
         * UPI URI format: upi://pay?pa={upiId}&pn={name}&cu=INR
         */
        public String generateQrCodeBase64(String upiId, String name) {
            // TODO: use ZXing QRCodeWriter → BitMatrix → MatrixToImageWriter → Base64
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }
