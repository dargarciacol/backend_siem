package com.nova.nova_siem_backend.siem.common.exception;

/**
 * Thrown when login credentials are wrong or the account is disabled.
 * Mapped to HTTP 401 (the frontend already treats 401 as "invalid credentials").
 */
public class InvalidCredentialsException extends BusinessException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
