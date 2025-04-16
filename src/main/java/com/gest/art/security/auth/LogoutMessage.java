package com.gest.art.security.auth;

public class LogoutMessage {
    private String message;

    public LogoutMessage(String ok) {
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
