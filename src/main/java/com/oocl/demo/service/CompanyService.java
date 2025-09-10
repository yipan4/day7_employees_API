package com.oocl.demo.service;

import com.oocl.demo.model.Company;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CompanyService {
    private final List<Company> companies = new ArrayList<>();
    private int currentUniqueId = 1;

    public void resetData() {
        currentUniqueId = 1;
        companies.clear();
    }

    public Map<String, Object> createCompany(Company company) {
        company.setId(currentUniqueId);
        currentUniqueId++;
        companies.add(company);
        return Map.of("id", company.getId());
    }

    public Company getCompanyById(long id) {
        return companies.stream().filter(company -> company.getId() == id).findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    public List<Company> getAllCompanies() {
        return companies;
    }

    public Company updateCompanyInfo(long id,Company updateCompany) {
        Company updatedCompany = companies.stream().filter(company ->
                        company.getId() == id).findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        updatedCompany.setName(updateCompany.getName());
        return updatedCompany;
    }

    public Company deleteCompany(@PathVariable long id) {
        Iterator<Company> iter = companies.iterator();
        while (iter.hasNext()) {
            Company deletedCompany = iter.next();
            if (deletedCompany.getId() == (id)) {
                iter.remove();
                return deletedCompany;
            }
        }
        return null;
    }

    public List<Company> getCompaniesPagination(@RequestParam int page, @RequestParam int size) {
        List<Company> paginationResult = new ArrayList<>();
        int startingIndex = size*(page - 1);
        for (int i = startingIndex; i < startingIndex + size; i++) {
            if (i >= companies.size()) {
                break;
            }
            paginationResult.add(companies.get(i));
        }
        return paginationResult;
    }
}
