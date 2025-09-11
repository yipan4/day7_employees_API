package com.oocl.demo.repository.dao;

import com.oocl.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeJpaRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByGender(String gender);
}
