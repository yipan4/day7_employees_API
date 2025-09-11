package com.oocl.demo.repository;

import com.oocl.demo.model.Company;

import java.util.List;

public interface CompanyRepository{
    public void resetData();

    public int getNextUniqueId();

    public Company createCompany(Company company);

    public Company findCompanyById(long id);

    public List<Company> getAllCompanies();

    public Company updateCompany(long id, Company updateCompany);

    public Company deleteCompany(long id);

    public List<Company> queryCompanyWithPagination(int page, int size);
}
