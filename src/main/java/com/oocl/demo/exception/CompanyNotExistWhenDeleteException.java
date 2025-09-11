package com.oocl.demo.exception;

public class CompanyNotExistWhenDeleteException extends RuntimeException {
    public CompanyNotExistWhenDeleteException(String message) {
        super(message);
    }
}
