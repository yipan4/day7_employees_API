package com.oocl.demo.exception;

public class EmployeeNotAmongLegalAgeException extends RuntimeException {
    public EmployeeNotAmongLegalAgeException(String message) {
        super(message);
    }
}
