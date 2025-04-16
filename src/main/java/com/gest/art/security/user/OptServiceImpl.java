package com.gest.art.security.user;


import org.springframework.stereotype.Service;

@Service
public class OptServiceImpl implements OptService {
    private static final Integer EXPIRE_MIN = 5;

    // LoadingCache<String, Integer> otpCache;
    @Override
    public String generateOTP(String phoneNumber) {

        return "";
    }

    @Override
    public boolean validateOTP(String phoneNumber, String otp) {
        return false;
    }
}
