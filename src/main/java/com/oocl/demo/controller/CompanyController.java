package com.oocl.demo.controller;

import com.oocl.demo.model.Company;
import com.oocl.demo.model.Employee;
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

    @GetMapping("/companies/all")
    public List<Company> getAllCompanies() {
        return companies;
    }

    @PutMapping("/companies/{id}")
    public Company updateCompanyInfo(@PathVariable long id, @RequestBody Company updateCompany) {
        Company updatedCompany = companies.stream().filter(company ->
                        company.getId() == id).findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        updatedCompany.setName(updateCompany.getName());
        return updatedCompany;
    }
}
