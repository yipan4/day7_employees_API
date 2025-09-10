package com.oocl.demo.service;

public class EmployeeIdNotFoundException extends RuntimeException {
    public EmployeeIdNotFoundException(String message) {
        super(message);
    }
}
