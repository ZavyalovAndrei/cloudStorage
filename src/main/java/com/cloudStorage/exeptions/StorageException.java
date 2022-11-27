package com.cloudStorage.exeptions;

import org.springframework.security.core.AuthenticationException;

public class StorageException extends AuthenticationException {

    public StorageException(String message) {
        super(message);
    }
}