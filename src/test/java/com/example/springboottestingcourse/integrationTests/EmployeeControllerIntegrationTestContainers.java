package com.example.springboottestingcourse.integrationTests;

import com.example.springboottestingcourse.model.Employee;
import com.example.springboottestingcourse.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTestContainers extends AbstractionBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL= "/api/v1/employees";

    @BeforeEach
    public void setup(){
        employeeRepository.deleteAll();
    }


    @DisplayName("JUnit test to create employee endpoint")
    @Test
    public void givenEmployeeObj_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup

        Employee employee = Employee.builder().firstname("first")
                .lastname("last").email("adndf@gmail.com").build();

        // when - action that will be tested

        ResultActions response = mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the expected output

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname",
                        CoreMatchers.is(employee.getFirstname())))
                .andExpect(jsonPath("$.lastname",
                        CoreMatchers.is(employee.getLastname())))
                .andExpect(jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }


    @DisplayName("JUnit test to get all employees endpoint")
    @Test
    public void given_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {

        // given - precondition or setup

        Employee employee = Employee.builder().firstname("first")
                .lastname("last").email("adndf@gmail.com").build();

        Employee employee2 = Employee.builder().firstname("second")
                .lastname("last").email("adndfsd@gmail.com").build();

        List<Employee> employees = List.of(employee, employee2);

        employeeRepository.saveAll(employees);
        // when - action that will be tested

        ResultActions response = mockMvc.perform(get(URL));

        // then - verify the expected output

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",
                        CoreMatchers.is(employees.size())));
    }


    @DisplayName("JUnit test to get employee by id endpoint")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObj() throws Exception {

        // given - precondition or setup

        Employee employee = Employee.builder().firstname("first")
                .lastname("last").email("adndf@gmail.com").build();

        employeeRepository.save(employee);
        // when - action that will be tested

        ResultActions response = mockMvc.perform(get(URL + "/{id}", employee.getId()));

        // then - verify the expected output

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        CoreMatchers.is((int) employee.getId())))
                .andExpect(jsonPath("$.firstname",
                        CoreMatchers.is(employee.getFirstname())))
                .andExpect(jsonPath("$.lastname",
                        CoreMatchers.is(employee.getLastname())))
                .andExpect(jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    @DisplayName("JUnit test to get employee by id endpoint (negative scenario)")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenThrowException() throws Exception {

        // given - precondition or setup

        long id = 10L;
        Employee employee = Employee.builder().id(1L).firstname("first")
                .lastname("last").email("adndf@gmail.com").build();

        employeeRepository.save(employee);

        // when - action that will be tested

        ResultActions response = mockMvc.perform(get(URL + "/{id}", id));

        // then - verify the expected output

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("JUnit test to update employee endpoint")
    @Test
    public void givenEmployeeObj_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {

        // given - precondition or setup

        Employee employee = Employee.builder().firstname("first")
                .lastname("last").email("adndf@gmail.com").build();
        Employee updatedEmployee = Employee.builder().firstname("first updated")
                .lastname("last updated").email("adndf@gmail.com").build();

        employeeRepository.save(employee);

        // when - action that will be tested

        ResultActions response = mockMvc.perform(put(URL + "/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the expected output

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname",
                        CoreMatchers.is(updatedEmployee.getFirstname())))
                .andExpect(jsonPath("$.lastname",
                        CoreMatchers.is(updatedEmployee.getLastname())))
                .andExpect(jsonPath("$.email",
                        CoreMatchers.is(updatedEmployee.getEmail())));
    }

    @Test
    public void deleteEmployee_Success() throws Exception {
        Long id = 1L;

        Employee employee = Employee.builder().firstname("first")
                .lastname("last").email("adndf@gmail.com").build();

        employeeRepository.save(employee);


        // Perform the DELETE request
        mockMvc.perform(delete(URL+"/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee is deleted with ID: " + id));
    }

    @DisplayName("JUnit test to update employee endpoint (negative scenario)")
    @Test
    public void givenEmployeeObj_whenUpdateEmployee_thenThrowException() throws Exception {

        // given - precondition or setup

        Employee employee = Employee.builder().id(1L).firstname("first")
                .lastname("last").email("adndf@gmail.com").build();

        // when - action that will be tested

        ResultActions response = mockMvc.perform(put(URL + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the expected output

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteEmployee_NonExistingId() throws Exception {
        Long id = 999L;

        // Perform the DELETE request
        mockMvc.perform(delete(URL+"/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee is deleted with ID: " + id));
    }

}
