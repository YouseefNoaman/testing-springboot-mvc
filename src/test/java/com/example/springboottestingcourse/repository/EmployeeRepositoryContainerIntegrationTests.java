package com.example.springboottestingcourse.repository;

import com.example.springboottestingcourse.integrationTests.AbstractionBaseTest;
import com.example.springboottestingcourse.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest    // this will only load @Repository classes, unless testcontainers class is extended
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)    // this will use Mysql DB not H2 for testing, useful for pilot testing
public class EmployeeRepositoryContainerIntegrationTests extends AbstractionBaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder().firstname("first")
                .lastname("test").email("an@gmail.com").build();
    }

    @Test
    @DisplayName("JUnit test for the save employee operation")
    public void givenEmployeeObj_whenSave_thenReturnEmployee() {

        // when - action that will be tested
        Employee savedEmployee = employeeRepository.save(employee);
        // then - the expected output
        // Assertions from assertj
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }


    @DisplayName("JUnit test for get all employees")
    @Test
    public void given2EmployeeObj_whenSaveAndGetAll_thenReturnListOfEmployees() {
        // given - precondition or setup
//        Employee employee = Employee.builder().firstname("first")
//                .lastname("test").email("an@gmail.com").build();

        Employee employee2 = Employee.builder().firstname("second")
                .lastname("test").email("an2@gmail.com").build();


        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when - action that will be tested

        List<Employee> employeeList = employeeRepository.findAll();

        // then - the expected output

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
//        assertThat(employeeList).hasSize(2);
        assertThat(employeeList.getFirst().getFirstname()).isEqualTo("first");
    }

    @DisplayName("JUnit test for return employee by ID")
    @Test
    public void givenEmployeeId_whenFindingById_thenReturnEmployee() {
        // given - precondition or setup
//        Employee employee = Employee.builder().firstname("first")
//                .lastname("test").email("an@gmail.com").build();

        employeeRepository.save(employee);

        // when - action that will be tested
        Employee returnedEmployee = employeeRepository.findById(employee.getId()).get();

        // then - the expected output

        assertThat(returnedEmployee).isNotNull();
        assertThat(returnedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("JUnit test for getting employee by email")
    @Test
    public void givenEmployeeEmail_whenFindingEmployee_thenReturnEmployee() {
        // given - precondition or setup
        String email = "an@gmail.com";
        Employee employee = Employee.builder().firstname("first")
                .lastname("test").email(email).build();
        employeeRepository.save(employee);
        // when - action that will be tested
        Employee employeeDB = employeeRepository.findByEmail(email).get();

        // then - the expected output

        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getEmail()).isEqualTo(email);
    }

    @DisplayName("JUnit test for updating employee")
    @Test
    public void givenEmployeeObj_whenUpdatingEmployeeAndSaving_thenReturnUpdatedEmployee() {
        // given - precondition or setup
//        Employee employee = Employee.builder().firstname("first")
//                .lastname("test").email("an@gmail.com").build();
        employeeRepository.save(employee);
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();
        employeeDB.setFirstname("updatedFirst");
        employeeRepository.save(employeeDB);
        // when - action that will be tested
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // then - the expected output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstname()).isEqualTo("updatedFirst");
    }

    @DisplayName("JUnit test for deleting employee")
    @Test
    public void givenEmployeeId_whenDeletingEmployee_thenEmployeeWillBeDeleted() {
        // given - precondition or setup
//        Employee employee = Employee.builder().firstname("first")
//                .lastname("test").email("an@gmail.com").build();
        employeeRepository.save(employee);

        // when - action that will be tested

        employeeRepository.delete(employee);
        Optional<Employee> employeeDB =employeeRepository.findById(employee.getId());

        // then - the expected output

        assertThat(employeeDB.isPresent()).isFalse();
        assertThat(employeeDB).isEmpty();
    }

    @DisplayName("JUnit test for getting employee using jpql query with index params")
    @Test
    public void givenFirstAndLastNames_whenUsingJPQLQueryWithIndexParams_thenReturnEmployee() {
        // given - precondition or setup
        String firstName = "first";
        String lastName = "last";
        Employee employee = Employee.builder().firstname(firstName)
                .lastname(lastName).email("an@gmail.com").build();
        employeeRepository.save(employee);

        // when - action that will be tested

        Employee employeeDB = employeeRepository.findByJPQL(firstName, lastName);

        // then - the expected output

        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getFirstname()).isEqualTo(firstName);
        assertThat(employeeDB.getLastname()).isEqualTo(lastName);
    }

    @DisplayName("JUnit test for getting employee using jpql using named params")
    @Test
    public void givenFirstAndLastNames_whenUsingJPQLQueryWithNamedParams_thenReturnEmployee() {
        // given - precondition or setup
        String firstName = "first";
        String lastName = "last";
        Employee employee = Employee.builder().firstname(firstName)
                .lastname(lastName).email("an@gmail.com").build();
        employeeRepository.save(employee);

        // when - action that will be tested
        Employee employeeDB = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        // then - the expected output
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getFirstname()).isEqualTo(firstName);
        assertThat(employeeDB.getLastname()).isEqualTo(lastName);
    }

    @DisplayName("JUnit test for getting employee using native SQL query using index params")
    @Test
    public void givenFirstAndLastNames_whenUsingNativeSQLQueryWithIndexParams_thenReturnEmployee() {
        // given - precondition or setup
        String firstName = "first";
        String lastName = "last";
        Employee employee = Employee.builder().firstname(firstName)
                .lastname(lastName).email("an@gmail.com").build();
        employeeRepository.save(employee);

        // when - action that will be tested
        Employee employeeDB = employeeRepository.findByNativeSQLWithIndexPrams(firstName, lastName);

        // then - the expected output
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getFirstname()).isEqualTo(firstName);
        assertThat(employeeDB.getLastname()).isEqualTo(lastName);
    }

    @DisplayName("JUnit test for getting employee using native SQL query using named params")
    @Test
    public void givenFirstAndLastNames_whenUsingNativeSQLQueryWithNamedParams_thenReturnEmployee() {
        // given - precondition or setup
        String firstName = "first";
        String lastName = "last";
        Employee employee = Employee.builder().firstname(firstName)
                .lastname(lastName).email("an@gmail.com").build();
        employeeRepository.save(employee);

        // when - action that will be tested
        Employee employeeDB = employeeRepository.findByNativeSQLWithNamedPrams(firstName, lastName);

        // then - the expected output
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getFirstname()).isEqualTo(firstName);
        assertThat(employeeDB.getLastname()).isEqualTo(lastName);
    }

}
