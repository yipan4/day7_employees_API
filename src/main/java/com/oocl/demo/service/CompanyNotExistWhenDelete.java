package com.oocl.demo.service;

public class CompanyNotExistWhenDelete extends RuntimeException {
    public CompanyNotExistWhenDelete(String message) {
        super(message);
    }
}
