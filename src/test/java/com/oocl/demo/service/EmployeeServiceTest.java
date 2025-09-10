package com.oocl.demo.service;

import com.oocl.demo.model.Employee;
import com.oocl.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private void assertEmployeeEquals(Employee expected, Employee actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAge(), actual.getAge());
        assertEquals(expected.getGender(), actual.getGender());
        assertEquals(expected.getSalary(), actual.getSalary());
    }

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
        verify(employeeRepository, never()).createEmployee(any());
    }

    @Test
    void should_not_create_employee_when_post_given_age_above_65() {
        Employee employee = new Employee();
        employee.setAge(66);
        employee.setName("Tom");
        employee.setGender("Male");
        employee.setSalary(3000.0);
        assertThrows(EmployeeNotAmongLegalAgeException.class, () -> {
            employeeService.createEmployee(employee);
        });
        verify(employeeRepository, never()).createEmployee(any());
    }

    @Test
    void should_return_employee_when_get_given_employee_id_exist() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setAge(20);
        employee.setName("Tom");
        employee.setGender("Male");

        when(employeeRepository.findEmployeeById(1)).thenReturn(employee);

        Employee foundEmployee = employeeService.getEmployeeById(1);
        assertEmployeeEquals(employee, foundEmployee);

        verify(employeeRepository, times(1)).findEmployeeById(anyLong());
    }

    @Test
    void should_throw_error_when_get_given_employee_id_not_exist() {
        when(employeeRepository.findEmployeeById(1)).thenReturn(null);

        assertThrows(EmployeeIdNotFoundException.class, () ->
                employeeService.getEmployeeById(1));

        verify(employeeRepository, times(1)).findEmployeeById(anyLong());
    }
}