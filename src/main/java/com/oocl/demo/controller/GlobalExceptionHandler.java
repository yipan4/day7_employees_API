package com.oocl.demo.controller;

import com.oocl.demo.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmployeeDuplicateCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmployeeDuplicateCreationException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(EmployeeAgeAboveAndSalaryBelowThresholdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmployeeAgeAboveAndSalaryBelowThresholdException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleEmployeeNotFoundException(Exception e) {
    }

    @ExceptionHandler(EmployeeNotAmongLegalAgeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmployeeNotAmongLegalAgeException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(EmployeeInactiveException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String handleEmployeeInactiveException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(CompanyPaginationQueryRangeExceedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleCompanyPaginationQueryRangeExceedException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCompanyNotFoundException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(CompanyNotExistWhenDelete.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String handleCompanyNotExistWhenDelete(Exception e) {
        return e.getMessage();
    }
}
