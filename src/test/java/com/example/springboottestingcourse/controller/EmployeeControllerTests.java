package com.example.springboottestingcourse.controller;

import com.example.springboottestingcourse.model.Employee;
import com.example.springboottestingcourse.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL= "/api/v1/employees";

    @DisplayName("JUnit test to create employee endpoint")
    @Test
    public void givenEmployeeObj_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup

        Employee employee = Employee.builder().firstname("first")
                .lastname("last").email("adndf@gmail.com").build();

        // this will return the employee object created when invoking the saveEmployee method and given any Employee object
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // this also works
//        given(employeeService.saveEmployee(employee)).willReturn(employee);

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

        given(employeeService.getAllEmployees()).willReturn(employees);

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

        Employee employee = Employee.builder().id(1L).firstname("first")
                .lastname("last").email("adndf@gmail.com").build();

        given(employeeService.getEmployeeById(1L)).willReturn(Optional.ofNullable(employee));

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

        Employee employee = Employee.builder().id(1L).firstname("first")
                .lastname("last").email("adndf@gmail.com").build();

        given(employeeService.getEmployeeById(1L)).willReturn(Optional.empty());

        // when - action that will be tested

        ResultActions response = mockMvc.perform(get(URL + "/{id}", employee.getId()));

        // then - verify the expected output

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("JUnit test to update employee endpoint")
    @Test
    public void givenEmployeeObj_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {

        // given - precondition or setup

        Employee employee = Employee.builder().id(1L).firstname("first")
                .lastname("last").email("adndf@gmail.com").build();
        Employee updatedEmployee = Employee.builder().id(1L).firstname("first updated")
                .lastname("last updated").email("adndf@gmail.com").build();

        given(employeeService.updateEmployee(1L, employee)).willReturn(Optional.ofNullable(updatedEmployee));
        // when - action that will be tested

        ResultActions response = mockMvc.perform(put(URL + "/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

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


    @DisplayName("JUnit test to update employee endpoint (negative scenario)")
    @Test
    public void givenEmployeeObj_whenUpdateEmployee_thenThrowException() throws Exception {

        // given - precondition or setup

        Employee employee = Employee.builder().id(1L).firstname("first")
                .lastname("last").email("adndf@gmail.com").build();
        Employee updatedEmployee = Employee.builder().id(1L).firstname("first updated")
                .lastname("last updated").email("adndf@gmail.com").build();

        given(employeeService.updateEmployee(1L, employee)).willReturn(Optional.empty());
        // when - action that will be tested

        ResultActions response = mockMvc.perform(put(URL + "/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the expected output

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteEmployee_Success() throws Exception {
        Long id = 1L;

        // Mock the deleteEmployee method
        doNothing().when(employeeService).deleteEmployee(id);

        // Perform the DELETE request
        mockMvc.perform(delete(URL+"/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee is deleted with ID: " + id));
    }

    @Test
    public void deleteEmployee_NullId() throws Exception {
        // Perform the DELETE request with null id
        mockMvc.perform(delete(URL+"/{id}", Long.valueOf(-1)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteEmployee_NonExistingId() throws Exception {
        Long id = 999L;

        // Mock the deleteEmployee method
        doNothing().when(employeeService).deleteEmployee(id);

        // Perform the DELETE request
        mockMvc.perform(delete(URL+"/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee is deleted with ID: " + id));
    }
}
