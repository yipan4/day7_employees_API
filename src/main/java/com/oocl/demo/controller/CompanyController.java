package com.oocl.demo.controller;

import com.oocl.demo.model.Company;
import com.oocl.demo.service.CompanyService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    public void resetData() {
        companyService.resetData();
    }

    @PostMapping("/companies")
    public Map<String, Object> createCompany(@RequestBody Company company) {
        return companyService.createCompany(company);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @GetMapping("/companies/all")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<Company> updateCompanyInfo(@PathVariable long id, @RequestBody Company updateCompany) {
        return ResponseEntity.ok(companyService.updateCompanyInfo(id, updateCompany));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Company> deleteCompany(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.deleteCompany(id));
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getCompaniesPagination(@RequestParam Integer page, @RequestParam Integer size) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompaniesPagination(page, size));
    }
}
