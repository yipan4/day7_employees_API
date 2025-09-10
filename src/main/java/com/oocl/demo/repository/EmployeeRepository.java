package com.oocl.demo.repository;

import com.oocl.demo.model.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();
    private int currentUniqueId = 1;

    public void resetData() {
        currentUniqueId = 1;
        employees.clear();
    }

    public int getNextUniqueId() {
        return currentUniqueId++;
    }

    public void createEmployee(Employee employee) {
        employees.add(employee);
    }

    public Employee findEmployeeById(long id) {
        return employees.stream().filter(employee -> employee.getId() == id).findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public Employee updateEmployee(long id, Employee employeeToBeUpdated) {
        Employee updatedEmployee = employees.stream().filter(employee ->
                        employee.getId() == id).findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
        updatedEmployee.setAge(employeeToBeUpdated.getAge());
        updatedEmployee.setSalary(employeeToBeUpdated.getSalary());
        return updatedEmployee;
    }

    public Employee deleteEmployee(long id) {
        Iterator<Employee> iter = employees.iterator();
        while (iter.hasNext()) {
            Employee deletedEmployee = iter.next();
            if (deletedEmployee.getId() == (id)) {
                iter.remove();
                return deletedEmployee;
            }
        }
        return null;
    }

    public List<Employee> queryEmployeeByPagination(int page, int size) {
        List<Employee> paginationResult = new ArrayList<>();
        int startingIndex = size*(page - 1);
        for (int i = startingIndex; i < startingIndex + size; i++) {
            if (i >= employees.size()) {
                break;
            }
            paginationResult.add(employees.get(i));
        }
        return paginationResult;
    }
}
