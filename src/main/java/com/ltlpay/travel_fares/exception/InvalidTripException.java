package com.ltlpay.travel_fares.exception;

public class InvalidTripException extends Exception {
    public InvalidTripException(String errorMessage) {
        super(errorMessage);
    }
}
