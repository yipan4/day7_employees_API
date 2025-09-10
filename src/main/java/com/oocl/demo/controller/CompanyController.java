package com.oocl.demo.controller;

import com.oocl.demo.model.Company;
import com.oocl.demo.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
public class CompanyController {
    private CompanyService companyService = new CompanyService();

    public void resetData() {
        companyService.resetData();
    }

    @PostMapping("/companies")
    public Map<String, Object> createCompany(@RequestBody Company company) {
        return companyService.createCompany(company);
    }

    @GetMapping("/companies/{id}")
    public Company getCompany(@PathVariable long id) {
        return companyService.getCompanyById(id);
    }

    @GetMapping("/companies/all")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @PutMapping("/companies/{id}")
    public Company updateCompanyInfo(@PathVariable long id, @RequestBody Company updateCompany) {
        return companyService.updateCompanyInfo(id, updateCompany);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Company> deleteCompany(@PathVariable long id) {
        Company deletedCompany = companyService.deleteCompany(id);
        if (deletedCompany == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(deletedCompany);
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getCompaniesPagination(@RequestParam Integer page, @RequestParam Integer size) {
        if (page == null || size == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompaniesPagination(page, size));
    }
}
