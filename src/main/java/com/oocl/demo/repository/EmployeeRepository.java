package com.oocl.demo.repository;

import com.oocl.demo.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface EmployeeRepository{
    void resetData();

    int getNextUniqueId();

    boolean isEmployeeDuplicatedByNameAndGender(Employee employee);

    void createEmployee(Employee employee);

    Employee findEmployeeById(long id);

    List<Employee> getAllEmployees();

    Employee updateEmployee(long id, Employee employeeToBeUpdated);

    Employee deleteEmployee(long id);

    List<Employee> queryEmployeeByPagination(int page, int size);
}
