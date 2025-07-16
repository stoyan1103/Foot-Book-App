package com.example.exception;

public class InvalidReservationHoursException extends DomainException {

    public InvalidReservationHoursException() {
    }

    public InvalidReservationHoursException(String message) {
        super(message);
    }
}