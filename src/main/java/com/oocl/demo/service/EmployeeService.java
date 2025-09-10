package com.oocl.demo.service;

import com.oocl.demo.model.Employee;
import com.oocl.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public void resetData() {
        employeeRepository.resetData();
    }

    public Map<String, Long> createEmployee(Employee employee) {
        int age = employee.getAge();
        double salary = employee.getSalary();
        if (age < 18 || age > 65) {
            throw new EmployeeNotAmongLegalAgeException("Employee's age not valid");
        }
        if (age >= 30 && salary < 20000.0) {
            throw new EmployeeAgeAboveAndSalaryBelowThresholdException("Employee's age over 30 but salary below 20000.0");
        }
        employee.setId(employeeRepository.getNextUniqueId());
        employee.setStatus(true);
        employeeRepository.createEmployee(employee);
        return Map.of("id", employee.getId());
    }

    public Employee getEmployeeById(long id) {
        Employee employee = employeeRepository.findEmployeeById(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee with ID %s not found.".formatted(id));
        }
        return employee;
    }

    public List<Employee> getAllEmployee() {
        return employeeRepository.getAllEmployees();
    }

    public Employee updateEmployeeInfo(long id, Employee employeeToBeUpdated) {
        return employeeRepository.updateEmployee(id, employeeToBeUpdated);
    }

    public Employee deleteEmployee(long id) {
        return employeeRepository.deleteEmployee(id);
    }

    public List<Employee> getEmployeesByGender(String gender) {
            return employeeRepository.getAllEmployees().stream().filter(employee -> employee.getGender().
                    equalsIgnoreCase(gender)).toList();
    }

    public List<Employee> getEmployeesWithPagination(int page, int size) {
        return employeeRepository.queryEmployeeByPagination(page, size);
    }
}
