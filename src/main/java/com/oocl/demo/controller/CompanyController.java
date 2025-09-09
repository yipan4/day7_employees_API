package com.oocl.demo.controller;

import com.oocl.demo.model.Company;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CompanyController {
    private final List<Company> companies = new ArrayList<>();
    private int currentUniqueId = 1;

    @PostMapping("/companies")
    public Map<String, Object> createCompany(@RequestBody Company company) {
        company.setId(currentUniqueId);
        currentUniqueId++;
        companies.add(company);
        return Map.of("id", company.getId());
    }

    @GetMapping("/companies/{id}")
    public Company getCompany(@PathVariable long id) {
        return companies.stream().filter(company -> company.getId() == id).findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }
}
