package com.oocl.demo.exception;

public class EmployeeAgeAboveAndSalaryBelowThresholdException extends RuntimeException {
    public EmployeeAgeAboveAndSalaryBelowThresholdException(String message) {
        super(message);
    }
}
