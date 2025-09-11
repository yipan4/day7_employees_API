package com.oocl.demo.repository;

import com.oocl.demo.model.Company;
import com.oocl.demo.repository.dao.CompanyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CompanyRepositoryDBImpl implements CompanyRepository {
    @Autowired
    private CompanyJpaRepository repository;

    @Override
    public void resetData() {
        repository.deleteAll();
    }

    @Override
    public int getNextUniqueId() {
        return 0;
    }

    @Override
    public Company createCompany(Company company) {
        return repository.save(company);
    }

    @Override
    public Company findCompanyById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Company> getAllCompanies() {
        return repository.findAll();
    }

    @Override
    public Company updateCompany(long id, Company updateCompany) {
        return repository.save(updateCompany);
    }

    @Override
    public Company deleteCompany(long id) {
        Company deletedCompany = repository.findById(id).orElse(null);
        if (deletedCompany == null) {
            return null;
        }
        repository.deleteById(id);
        return deletedCompany;
    }

    @Override
    public List<Company> queryCompanyWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        return repository.findAll(pageable).toList();
    }
}
