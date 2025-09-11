package com.oocl.demo.repository;

import com.oocl.demo.model.DeleteEmployeeReq;
import com.oocl.demo.model.Employee;
import com.oocl.demo.model.UpdateEmployeeReq;
import com.oocl.demo.repository.dao.EmployeeJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepositoryDBImpl implements EmployeeRepository {
    @Autowired
    private EmployeeJpaRepository repository;

    @Override
    public void resetData() {
        repository.deleteAll();
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
    public Employee createEmployee(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public Employee findEmployeeById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @Override
    public Employee updateEmployee(long id, UpdateEmployeeReq employeeToBeUpdated) {
        Employee foundEmployee = repository.findById(id).orElse(null);
        if (foundEmployee == null) {
            return null;
        }
        foundEmployee.setName(employeeToBeUpdated.getName());
        foundEmployee.setAge(employeeToBeUpdated.getAge());
        foundEmployee.setSalary(employeeToBeUpdated.getSalary());
        repository.save(foundEmployee);
        return foundEmployee;
    }

    @Override
    public Employee deleteEmployee(long id, DeleteEmployeeReq employee) {
        Employee foundEmployee = repository.findById(id).orElse(null);
        if (foundEmployee == null) {
            return null;
        }
        foundEmployee.setStatus(employee.getStatus());
        return repository.save(foundEmployee);
    }

    @Override
    public List<Employee> queryEmployeeByPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        return repository.findAll(pageable).toList();
    }

    public List<Employee> findByGender(String gender) {
        return repository.findByGender(gender);
    }
}
