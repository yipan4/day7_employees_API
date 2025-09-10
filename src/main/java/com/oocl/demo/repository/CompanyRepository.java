package com.oocl.demo.repository;

import com.oocl.demo.model.Company;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class CompanyRepository {
    private final List<Company> companies = new ArrayList<>();
    private int currentUniqueId = 1;

    public void resetData() {
        currentUniqueId = 1;
        companies.clear();
    }

    public int getNextUniqueId() {
        return currentUniqueId++;
    }

    public void createCompany(Company company) {
        companies.add(company);
    }

    public Company findCompanyById(long id) {
        return companies.stream().filter(company -> company.getId() == id).findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    public List<Company> getAllCompanies() {
        return companies;
    }

    public Company updateCompany(long id, Company updateCompany) {
        Company updatedCompany = companies.stream().filter(company ->
                        company.getId() == id).findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        updatedCompany.setName(updateCompany.getName());
        return updatedCompany;
    }

    public Company deleteCompany(long id) {
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

    public List<Company> queryCompanyWithPagination(int page, int size) {
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
