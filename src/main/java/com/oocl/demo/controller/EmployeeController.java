package com.oocl.demo.controller;

import com.oocl.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.oocl.demo.model.Employee;

import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    public void resetData() {
        employeeService.resetData();
    }

    @PostMapping("/employees")
    public ResponseEntity<Map<String, Long>> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employee));
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeeById(id));
    }

    @GetMapping("/employees/all")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployee();
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployeeInfo(@PathVariable long id, @RequestBody Employee employeeToBeUpdated) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.updateEmployeeInfo(id, employeeToBeUpdated));
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(employeeService.deleteEmployee(id));
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployeesPagination(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (gender != null) {
            return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeesByGender(gender));
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeesWithPagination(page, size));
    }
}
