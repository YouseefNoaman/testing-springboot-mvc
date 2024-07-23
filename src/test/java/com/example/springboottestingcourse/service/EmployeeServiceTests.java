package com.example.springboottestingcourse.service;

import com.example.springboottestingcourse.exception.ResourceNotFoundException;
import com.example.springboottestingcourse.model.Employee;
import com.example.springboottestingcourse.repository.EmployeeRepository;
import com.example.springboottestingcourse.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // this is used to indicate that mockito will be used in this class
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder().firstname("first")
                .lastname("name").email("an@gmail.com").build();
    }

    @DisplayName("JUnit test for save employee")
    @Test
    public void givenEmployeeObj_whenSaveEmployeeObj_thenReturnEmployeeObj() {
        // given - precondition or setup
// no need to call BDDMockito because the ExtendWith annotation indicated mockito is used
//        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
//                .willReturn(Optional.empty());
//
//        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        // when - action that will be tested

        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - the expected output

        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for save employee which will throw exception")
    @Test
    public void givenEmployeeObj_whenSaveEmployeeObj_thenThrowException() {

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // when - action that will be tested
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,
                () -> employeeService.saveEmployee(employee));
        // this also works
//        assertThatThrownBy(() -> employeeService.saveEmployee(employee));
        // then - the expected output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("JUnit test for get all employees (positive scenario)")
    @Test
    public void givenEmployeeObjs_whenGettingEmployeeObjs_thenReturnEmployeesList() {
        // given - precondition or setup
        Employee employee2 = Employee.builder().firstname("second")
                .lastname("last").email("sec@gmail.com").build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

        // when - action that will be tested

        List<Employee> employees = employeeService.getAllEmployees();

        // then - the expected output
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for get all employees (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGettingEmployeesList_thenReturnEmptyEmployeesList() {
        // given - precondition or setup
        given(employeeRepository.findAll()).willReturn(List.of());

        // when - action that will be tested

        List<Employee> employees = employeeService.getAllEmployees();

        // then - the expected output
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(0);
    }

    @DisplayName("JUnit test for get employee by ID")
    @Test
    public void givenEmployeeID_whenGettingEmployeeById_thenReturnEmployee() {
        // given - precondition or setup
        long id = 1L;
        employee.setId(id);
        given(employeeRepository.findById(id)).willReturn(Optional.ofNullable(employee));

        // when - action that will be tested

        Optional<Employee> employee = employeeService.getEmployeeById(id);

        // then - the expected output
        assertThat(employee.isPresent()).isNotNull();
        assertThat(employee.get().getId()).isEqualTo(1L);
    }

    @DisplayName("JUnit test for get employee by ID (negative scenario)")
    @Test
    public void givenEmployeeID_whenGettingEmployeeById_thenThrowError() {
        // given - precondition or setup
        long id = 1L;
        employee.setId(id);
        given(employeeRepository.findById(id)).willReturn(Optional.empty());

        // when - action that will be tested

        // then - the expected output
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(1L));

    }

    @DisplayName("JUnit test for update employee by ID and object")
    @Test
    public void givenEmployeeIDAndObj_whenUpdatingEmployeeByIdAndObj_thenReturnEmployee() {
        // given - precondition or setup

        long id = 1L;
        employee.setId(id);

        given(employeeRepository.findById(id)).willReturn(Optional.of(employee));

        // when - action that will be tested

        employee.setFirstname("first updated");
        Optional<Employee> updatedEmployee = employeeService.updateEmployee(id, employee);

        // then - the expected output
        assertThat(updatedEmployee.isPresent()).isTrue();
        assertThat(updatedEmployee.get().getFirstname()).isEqualTo("first updated");

    }

    @DisplayName("JUnit test for update employee by ID and object (negative scenario)")
    @Test
    public void givenEmployeeIDAndObj_whenUpdatingEmployeeByIdAndObj_thenThrowError() {
        // given - precondition or setup

        long id = 1L;
        employee.setId(id);
        given(employeeRepository.findById(id)).willReturn(Optional.empty());

        // when - action that will be tested

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> employeeService.updateEmployee(id, employee));

    }

    @DisplayName("JUnit test for delete employee by ID")
    @Test
    public void givenEmployeeID_whenDeletingEmployeeById_thenDeleteEmployee() {
        // given - precondition or setup

        willDoNothing().given(employeeRepository).deleteById(1L);

        // when - action that will be tested

        employeeService.deleteEmployee(1L);

        // then - the expected output

        verify(employeeRepository, times(1)).deleteById(1L);

    }



}
