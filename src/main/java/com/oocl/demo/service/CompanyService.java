package com.oocl.demo.service;

import com.oocl.demo.exception.CompanyNotExistWhenDeleteException;
import com.oocl.demo.exception.CompanyNotFoundException;
import com.oocl.demo.exception.CompanyPaginationQueryRangeExceedException;
import com.oocl.demo.model.Company;
import com.oocl.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public void resetData() {
        companyRepository.resetData();
    }

    public Map<String, Object> createCompany(Company company) {
        company.setId(companyRepository.getNextUniqueId());
        companyRepository.createCompany(company);
        return Map.of("id", company.getId());
    }

    public Company getCompanyById(long id) {
        Company company = companyRepository.findCompanyById(id);
        if (company == null) {
            throw new CompanyNotFoundException("Company with ID %s not found".formatted(id));
        }
        return company;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.getAllCompanies();
    }

    public Company updateCompanyInfo(long id, Company updateCompany) {
        if (companyRepository.findCompanyById(id) == null) {
            throw new CompanyNotFoundException("Company with ID %s not found".formatted(id));
        }
        return companyRepository.updateCompany(id, updateCompany);
    }

    public Company deleteCompany(long id) {
        Company deletedCompany = companyRepository.deleteCompany(id);
        if (deletedCompany == null) {
            throw new CompanyNotExistWhenDeleteException("Company with ID %s not found".formatted(id));
        }
        return deletedCompany;
    }

    public List<Company> getCompaniesPagination(Integer page, Integer size) {
        if (page == null || size == null) {
            throw new CompanyPaginationQueryRangeExceedException("Page and size cannot be null");
        }
        List<Company> paginationResult = companyRepository.queryCompanyWithPagination(page, size);
        if (paginationResult == null ) {
            throw new CompanyPaginationQueryRangeExceedException("Page and size cannot be negative");
        }
        return paginationResult;
    }
}
