package com.oocl.demo.exception;

public class EmployeeDuplicateCreationException extends RuntimeException {
    public EmployeeDuplicateCreationException(String message) {
        super(message);
    }
}
