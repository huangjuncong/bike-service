package com.coder520.mamabike.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by 黄俊聪
 */
public class BadCredentialException extends AuthenticationException {
    public BadCredentialException(String msg) {
        super(msg);
    }
}
