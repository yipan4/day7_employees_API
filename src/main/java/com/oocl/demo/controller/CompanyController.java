package com.oocl.demo.controller;

import com.oocl.demo.model.Company;
import com.oocl.demo.model.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
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

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Company> deleteCompany(@PathVariable long id) {
        Iterator<Company> iter = companies.iterator();
        while (iter.hasNext()) {
            Company deletedCompany = iter.next();
            if (deletedCompany.getId() == (id)) {
                iter.remove();
                return ResponseEntity.status(HttpStatus.OK).body(deletedCompany);
            }
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
