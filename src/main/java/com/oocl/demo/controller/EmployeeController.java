package com.oocl.demo.controller;

import com.oocl.demo.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.oocl.demo.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {
    private final EmployeeService employeeService = new EmployeeService();

    public void resetData() {
        employeeService.resetData();
    }

    @PostMapping("/employees")
    public ResponseEntity<Map<String, Long>> createEmployee(@RequestBody Employee employee) {
        Map<String, Long> id = null;
        try {
            id = employeeService.createEmployee(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable long id) {
        Employee foundEmployee = null;
        try {
            foundEmployee = employeeService.getEmployeeById(id);
            return ResponseEntity.status(HttpStatus.OK).body(foundEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/employees/all")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployee();
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployeeInfo(@PathVariable long id, @RequestBody Employee employeeToBeUpdated) {
        Employee updatedEmployee = employeeService.updateEmployeeInfo(id, employeeToBeUpdated);
        if (updatedEmployee != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedEmployee);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
        List<Employee> paginatedResult = employeeService.getEmployeesWithPagination(page, size);
        if (paginatedResult == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(paginatedResult);
    }
}
