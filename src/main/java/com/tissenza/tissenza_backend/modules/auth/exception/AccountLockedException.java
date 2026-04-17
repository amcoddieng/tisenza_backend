package com.tissenza.tissenza_backend.modules.auth.exception;

public class AccountLockedException extends AuthenticationException {
    
    public AccountLockedException(String message) {
        super(message, "ACCOUNT_LOCKED");
    }
    
    public AccountLockedException(String message, Throwable cause) {
        super(message, "ACCOUNT_LOCKED", cause);
    }
}
