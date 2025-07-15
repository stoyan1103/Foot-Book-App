package com.example.exception;

public class UsernameAlreadyExistsException extends DomainException {

    public UsernameAlreadyExistsException() {
        super();
    }

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
