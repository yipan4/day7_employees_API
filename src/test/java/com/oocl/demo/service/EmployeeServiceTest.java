package com.oocl.demo.service;

import com.oocl.demo.model.Employee;
import com.oocl.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void should_not_create_employee_when_post_given_age_below_18() {
        Employee employee = new Employee();
        employee.setAge(17);
        employee.setName("Tom");
        employee.setGender("Male");
        employee.setSalary(3000.0);
        assertThrows(EmployeeNotAmongLegalAgeException.class, () -> {
            employeeService.createEmployee(employee);
        });
    }
}