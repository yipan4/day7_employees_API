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
        if (age < 18 || age > 65) {
            throw new EmployeeNotAmongLegalAgeException("Employee's age not valid");
        }
        employee.setId(employeeRepository.getNextUniqueId());
        employeeRepository.createEmployee(employee);
        return Map.of("id", employee.getId());
    }

    public Employee getEmployeeById(long id) {
        Employee employee = employeeRepository.findEmployeeById(id);
        if (employee == null) {
            throw new EmployeeIdNotFoundException("Employee ID not found.");
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
