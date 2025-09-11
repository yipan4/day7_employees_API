package com.oocl.demo.repository;

import com.oocl.demo.model.DeleteEmployeeReq;
import com.oocl.demo.model.Employee;
import com.oocl.demo.model.UpdateEmployeeReq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EmployeeRepositoryInMemoryImpl implements EmployeeRepository {
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

    public Employee createEmployee(Employee employee) {
        employees.add(employee);
        return employee;
    }

    public Employee findEmployeeById(long id) {
        return employees.stream().filter(employee -> employee.getId() == id).findAny()
                .orElse(null);
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public Employee updateEmployee(long id, UpdateEmployeeReq employeeToBeUpdated) {
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            if (employee.getId() == id) {
                employee.setSalary(employeeToBeUpdated.getSalary());
                employee.setName(employeeToBeUpdated.getName());
                employee.setAge(employeeToBeUpdated.getAge());
                employees.set(i, employee);
            }
        }
        return null;
    }

    @Override
    public Employee deleteEmployee(long id, DeleteEmployeeReq deleteEmployeeReq) {
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

    @Override
    public List<Employee> findByGender(String gender) {
        return employees.stream().filter(employee -> employee.getGender().equalsIgnoreCase(gender))
                .toList();
    }

}
