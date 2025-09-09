package com.oocl.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.oocl.demo.model.Employee;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {
    private final List<Employee> employees = new ArrayList<>();
    private int currentUniqueId = 1;

    @PostMapping("/employees")
    public Map<String, Object> createEmployee(@RequestBody Employee employee) {
        employee.setId(currentUniqueId);
        currentUniqueId++;
        employees.add(employee);
        return Map.of("id", employee.getId());
    }

    @PostMapping("/employees-custom-status-code")
    public ResponseEntity<Map<String, Long>> createEmployeeWithCustomStatusCode(@RequestBody Employee employee) {
        employee.setId(currentUniqueId);
        currentUniqueId++;
        employees.add(employee);
        Map<String, Long> result = Map.of("id", employee.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable long id) {
        return employees.stream().filter(employee -> employee.getId() == id).findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    @GetMapping("/employees")
    public List<Employee> queryEmployeeByGender(@RequestParam String gender) {
        return employees.stream().filter(employee -> employee.getGender().
                equalsIgnoreCase(gender)).toList();
    }

    @GetMapping("/employees/all")
    public List<Employee> getAllEmployees() {
        return employees;
    }

    @PutMapping("/employees/{id}")
    public Employee updateEmployeeInfo(@PathVariable long id, @RequestBody Employee employeeToBeUpdated) {
        Employee updatedEmployee = employees.stream().filter(employee ->
                        employee.getId() == id).findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
        updatedEmployee.setAge(employeeToBeUpdated.getAge());
        updatedEmployee.setSalary(employeeToBeUpdated.getSalary());
        return updatedEmployee;
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable long id) {
        Iterator<Employee> iter = employees.iterator();
        while (iter.hasNext()) {
            Employee deletedEmployee = iter.next();
            if (deletedEmployee.getId() == (id)) {
                iter.remove();
                return ResponseEntity.status(HttpStatus.OK).body(deletedEmployee);
            }
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/employees-page")
    public List<Employee> getEmployeesPagination(@RequestParam int page, @RequestParam int size) {
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
