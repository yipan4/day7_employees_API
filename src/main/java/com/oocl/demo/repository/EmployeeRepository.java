package com.oocl.demo.repository;

import com.oocl.demo.model.DeleteEmployeeReq;
import com.oocl.demo.model.Employee;
import com.oocl.demo.model.UpdateEmployeeReq;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface EmployeeRepository{
    void resetData();

    int getNextUniqueId();

    boolean isEmployeeDuplicatedByNameAndGender(Employee employee);

    Employee createEmployee(Employee employee);

    Employee findEmployeeById(long id);

    List<Employee> getAllEmployees();

    Employee updateEmployee(long id, UpdateEmployeeReq employeeToBeUpdated);

    Employee deleteEmployee(long id, DeleteEmployeeReq deleteEmployeeReq);

    List<Employee> queryEmployeeByPagination(int page, int size);

    List<Employee> findByGender(String gender);
}
