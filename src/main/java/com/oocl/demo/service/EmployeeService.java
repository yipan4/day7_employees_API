package com.oocl.demo.service;

import com.oocl.demo.model.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    private final List<Employee> employees = new ArrayList<>();
    private int currentUniqueId = 1;

    public void resetData() {
        currentUniqueId = 1;
        employees.clear();
    }

    public Map<String, Long> createEmployee(Employee employee) {
        employee.setId(currentUniqueId);
        currentUniqueId++;
        employees.add(employee);
        return Map.of("id", employee.getId());
    }

    public Employee getEmployeeById(long id) {
        return employees.stream().filter(employee -> employee.getId() == id).findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    public List<Employee> getAllEmployee() {
        return employees;
    }

    public Employee updateEmployeeInfo(long id, Employee employeeToBeUpdated) {
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

    public List<Employee> getEmployeesByGender(String gender) {
            return employees.stream().filter(employee -> employee.getGender().
                    equalsIgnoreCase(gender)).toList();
    }

    public List<Employee> getEmployeesWithPagination(int page, int size) {
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
