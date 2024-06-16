package com.example.restaurantsoftware.model.customExceptions;

public class ExistingProductException extends RuntimeException {
    public ExistingProductException(String message) {
        super(message);
    }
}