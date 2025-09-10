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

        assertThrows(EmployeeNotFoundException.class, () ->
                employeeService.getEmployeeById(1));

        verify(employeeRepository, times(1)).findEmployeeById(anyLong());
    }

    @Test
    void should_throw_error_when_post_given_employee_age_above_30_salary_below_20000() {
        Employee employee = new Employee();
        employee.setAge(30);
        employee.setName("Tom");
        employee.setGender("Male");
        employee.setSalary(3000.0);
        assertThrows(EmployeeAgeAboveAndSalaryBelowThresholdException.class, () -> {
            employeeService.createEmployee(employee);
        });
        verify(employeeRepository, never()).createEmployee(any());
    }

    @Test
    void should_create_employee_when_post_given_employee_age_above_30_salary_above_20000() {
        Employee employee = new Employee();
        employee.setAge(30);
        employee.setName("Tom");
        employee.setGender("Male");
        employee.setSalary(30000.0);
        employeeService.createEmployee(employee);
        verify(employeeRepository, times(1)).createEmployee(any());
    }

    @Test
    void should_create_employee_when_post_given_employee_age_below_30_salary_above_20000() {
        Employee employee = new Employee();
        employee.setAge(28);
        employee.setName("Tom");
        employee.setGender("Male");
        employee.setSalary(30000.0);
        employeeService.createEmployee(employee);
        verify(employeeRepository, times(1)).createEmployee(any());
    }

    @Test
    void should_create_employee_when_post_given_employee_age_below_30_salary_below_20000() {
        Employee employee = new Employee();
        employee.setAge(28);
        employee.setName("Tom");
        employee.setGender("Male");
        employee.setSalary(3000.0);
        employeeService.createEmployee(employee);
        verify(employeeRepository, times(1)).createEmployee(any());
    }

    @Test
    void should_set_status_true_when_post_given_new_employee() {
        Employee employee = new Employee();
        employee.setAge(28);
        employee.setName("Tom");
        employee.setGender("Male");
        employee.setSalary(30000.0);
        employeeService.createEmployee(employee);
        assertTrue(employee.getStatus());
        verify(employeeRepository, times(1)).createEmployee(any());
    }

    @Test
    void should_throw_error_when_post_given_employee_with_same_name_and_gender() {
        Employee employee = new Employee();
        employee.setAge(30);
        employee.setName("Tom");
        employee.setGender("Male");
        employee.setSalary(40000.0);
        when(employeeRepository.isEmployeeDuplicatedByNameAndGender(employee)).thenReturn(true);
        assertThrows(EmployeeDuplicateCreationException.class, () ->
                employeeService.createEmployee(employee));
        verify(employeeRepository, never()).createEmployee(any());
    }

    @Test
    void should_set_status_false_when_delete_given_employee_status_true() {
        Employee employee = new Employee();
        employee.setStatus(true);
        when(employeeRepository.findEmployeeById(1)).thenReturn(employee);
        employeeService.deleteEmployee(1);
        assertFalse(employee.getStatus());
        verify(employeeRepository, times(1)).findEmployeeById(1);
        verify(employeeRepository, times(1)).updateEmployee(1, employee);
    }

    @Test
    void should_do_nothing_when_delete_given_employee_status_false() {
        Employee employee = new Employee();
        employee.setStatus(false);
        when(employeeRepository.findEmployeeById(1)).thenReturn(employee);
        Employee deletedEmployee = employeeService.deleteEmployee(1);
        assertNull(deletedEmployee);
        verify(employeeRepository, times(1)).findEmployeeById(1);
        verify(employeeRepository, never()).updateEmployee(1, employee);
    }

    @Test
    void should_do_nothing_when_update_given_employee_status_false() {
        Employee employee = new Employee();
        employee.setStatus(false);
        when(employeeRepository.findEmployeeById(1)).thenReturn(employee);
        Employee updatedEmployee = employeeService.updateEmployeeInfo(1, employee);
        assertNull(updatedEmployee);
        verify(employeeRepository, times(1)).findEmployeeById(1);
        verify(employeeRepository, never()).updateEmployee(1, employee);
    }
}