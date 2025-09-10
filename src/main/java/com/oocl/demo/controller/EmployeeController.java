package com.oocl.demo.controller;

import com.oocl.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private EmployeeService employeeService = new EmployeeService();

    public void resetData() {
        employeeService.resetData();
    }

    @PostMapping("/employees")
    public ResponseEntity<Map<String, Long>> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employee));
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable long id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/employees/all")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployee();
    }

    @PutMapping("/employees/{id}")
    public Employee updateEmployeeInfo(@PathVariable long id, @RequestBody Employee employeeToBeUpdated) {
        return employeeService.updateEmployeeInfo(id, employeeToBeUpdated);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable long id) {
        Employee deletedEmployee = employeeService.deleteEmployee(id);
        if (deletedEmployee == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(deletedEmployee);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployeesPagination(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (gender != null) {
            return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeesByGender(gender));
        }
        if (page == null || size == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeesWithPagination(page, size));
    }
}
