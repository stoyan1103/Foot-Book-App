package com.example.exception;

public class FieldNotAvailableException extends DomainException {

    public FieldNotAvailableException() {
    }

    public FieldNotAvailableException(String message) {
        super(message);
    }
}