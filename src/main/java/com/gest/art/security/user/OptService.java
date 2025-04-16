package com.gest.art.security.user;

public interface OptService {
    String generateOTP(String phoneNumber);

    boolean validateOTP(String phoneNumber, String otp);
}
