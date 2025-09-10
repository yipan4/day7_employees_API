package com.oocl.demo.service;

public class EmployeeAgeAboveAndSalaryBelowThresholdException extends RuntimeException {
    public EmployeeAgeAboveAndSalaryBelowThresholdException(String message) {
        super(message);
    }
}
