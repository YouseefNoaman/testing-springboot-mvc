package com.example.springboottestingcourse.service.impl;

import com.example.springboottestingcourse.exception.ResourceNotFoundException;
import com.example.springboottestingcourse.model.Employee;
import com.example.springboottestingcourse.repository.EmployeeRepository;
import com.example.springboottestingcourse.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already found with email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id){
        return employeeRepository.findById(id);
    }

    @Override
    public Optional<Employee> updateEmployee(Long id, Employee employee){
        Optional<Employee> employeeDB = getEmployeeById(id);
        if (employeeDB.isEmpty())
            return employeeDB;

        employeeDB.get().setEmail(employee.getEmail());
        employeeDB.get().setFirstname(employee.getFirstname());
        employeeDB.get().setLastname(employee.getLastname());
        employeeRepository.save(employeeDB.get());
        return employeeDB;
    }

    @Override
    public void deleteEmployee(Long id){
        employeeRepository.deleteById(id);
    }
}

