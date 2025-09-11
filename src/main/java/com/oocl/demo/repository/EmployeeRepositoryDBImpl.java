package com.oocl.demo.repository;

import com.oocl.demo.model.Employee;
import com.oocl.demo.repository.dao.EmployeeJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepositoryDBImpl implements EmployeeRepository {
    @Autowired
    private EmployeeJpaRepository repository;

    @Override
    public void resetData() {

    }

    @Override
    public int getNextUniqueId() {
        return 0;
    }

    @Override
    public boolean isEmployeeDuplicatedByNameAndGender(Employee employee) {
        return false;
    }

    @Override
    public void createEmployee(Employee employee) {
        repository.save(employee);
    }

    @Override
    public Employee findEmployeeById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return List.of();
    }

    @Override
    public Employee updateEmployee(long id, Employee employeeToBeUpdated) {
        return null;
    }

    @Override
    public Employee deleteEmployee(long id) {
        return null;
    }

    @Override
    public List<Employee> queryEmployeeByPagination(int page, int size) {
        return List.of();
    }
}
