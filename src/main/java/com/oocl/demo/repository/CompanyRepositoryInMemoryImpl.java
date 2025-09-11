package com.oocl.demo.repository;

import com.oocl.demo.model.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompanyRepositoryInMemoryImpl {
    private final List<Company> companies = new ArrayList<>();
    private int currentUniqueId = 1;

    public void resetData() {
        currentUniqueId = 1;
        companies.clear();
    }

    public int getNextUniqueId() {
        return currentUniqueId++;
    }

    public Company createCompany(Company company) {
        companies.add(company);
        return company;
    }

    public Company findCompanyById(long id) {
        return companies.stream().filter(company -> company.getId() == id).findAny()
                .orElse(null);
    }

    public List<Company> getAllCompanies() {
        return companies;
    }

    public Company updateCompany(long id, Company updateCompany) {
        Company updatedCompany = companies.stream().filter(company ->
                        company.getId() == id).findAny()
                .orElse(null);
        if (updatedCompany == null) {
            return null;
        }
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
        if (startingIndex < 0 ) {
            return null;
        }
        for (int i = startingIndex; i < startingIndex + size; i++) {
            if (i >= companies.size()) {
                break;
            }
            paginationResult.add(companies.get(i));
        }
        return paginationResult;
    }
}
