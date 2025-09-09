package com.oocl.demo.controller;

import com.oocl.demo.model.Company;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
