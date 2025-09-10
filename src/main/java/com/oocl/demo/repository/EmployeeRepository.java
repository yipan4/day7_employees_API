package com.oocl.demo.repository;

import com.oocl.demo.model.Employee;
import org.springframework.stereotype.Repository;

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

    public boolean isEmployeeDuplicatedByNameAndGender(Employee newEmployee) {
        return employees.stream()
                .anyMatch(employee ->
                        employee.getName().equalsIgnoreCase(newEmployee.getName()) &&
                                employee.getGender().equalsIgnoreCase(newEmployee.getGender()));
    }

    public void createEmployee(Employee employee) {
        employees.add(employee);
    }

    public Employee findEmployeeById(long id) {
        return employees.stream().filter(employee -> employee.getId() == id).findAny()
                .orElse(null);
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public Employee updateEmployee(long id, Employee employeeToBeUpdated) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == employeeToBeUpdated.getId()) {
                employees.set(i, employeeToBeUpdated);
                return employees.get(i);
            }
        }
        return null;
    }

    public Employee deleteEmployee(long id) {
        Iterator<Employee> iter = employees.iterator();
        while (iter.hasNext()) {
            Employee deletedEmployee = iter.next();
            if (deletedEmployee.getId() == (id)) {
//                iter.remove();
                deletedEmployee.setStatus(false);
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
                return null;
            }
            paginationResult.add(employees.get(i));
        }
        return paginationResult;
    }
}
