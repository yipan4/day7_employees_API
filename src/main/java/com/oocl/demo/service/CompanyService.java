package com.oocl.demo.service;

import com.oocl.demo.model.Company;
import com.oocl.demo.repository.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CompanyService {
    private CompanyRepository companyRepository = new CompanyRepository();

    public void resetData() {
        companyRepository.resetData();
    }

    public Map<String, Object> createCompany(Company company) {
        company.setId(companyRepository.getNextUniqueId());
        companyRepository.createCompany(company);
        return Map.of("id", company.getId());
    }

    public Company getCompanyById(long id) {
        return companyRepository.findCompanyById(id);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.getAllCompanies();
    }

    public Company updateCompanyInfo(long id, Company updateCompany) {
        return companyRepository.updateCompany(id, updateCompany);
    }

    public Company deleteCompany(long id) {
        return companyRepository.deleteCompany(id);
    }

    public List<Company> getCompaniesPagination(int page, int size) {
        return companyRepository.queryCompanyWithPagination(page, size);
    }
}
