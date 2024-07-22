package com.example.springboottestingcourse.repository;

import com.example.springboottestingcourse.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
