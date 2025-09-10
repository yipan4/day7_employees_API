package com.oocl.demo.service;

public class EmployeeDuplicateCreationException extends RuntimeException {
    public EmployeeDuplicateCreationException(String message) {
        super(message);
    }
}
