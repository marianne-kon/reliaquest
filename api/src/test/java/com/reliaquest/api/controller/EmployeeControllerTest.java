package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.api.service.EmployeeService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee;
    private EmployeeInput employeeInput;
    private List<Employee> employeeList;
    private List<String> employeeNamesList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId("1");
        employee.setEmployee_name("John Doe");

        employeeInput = new EmployeeInput();
        employeeInput.setName("Jane Doe");

        employeeList = Arrays.asList(employee, new Employee());

        employeeNamesList = Arrays.asList("John Doe", "Jane Doe", "Alice Smith");
    }

    @Test
    void getAllEmployees_ShouldReturnAllEmployees() {
        // Arrange
        when(employeeService.getAllEmployees()).thenReturn(employeeList);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeList, response.getBody());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void getEmployeesByNameSearch_ShouldReturnMatchingEmployees() {
        // Arrange
        String searchString = "Doe";
        when(employeeService.getEmployeesByNameSearch(searchString)).thenReturn(employeeList);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch(searchString);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeList, response.getBody());
        verify(employeeService, times(1)).getEmployeesByNameSearch(searchString);
    }

    @Test
    void getEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() {
        // Arrange
        String employeeId = "1";
        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);

        // Act
        ResponseEntity response = employeeController.getEmployeeById(employeeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
        verify(employeeService, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void getEmployeeById_WhenEmployeeDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        String employeeId = "999";
        when(employeeService.getEmployeeById(employeeId)).thenReturn(null);

        // Act
        ResponseEntity response = employeeController.getEmployeeById(employeeId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void getHighestSalaryOfEmployees_ShouldReturnHighestSalary() {
        // Arrange
        Integer highestSalary = 100000;
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(highestSalary);

        // Act
        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(highestSalary, response.getBody());
        verify(employeeService, times(1)).getHighestSalaryOfEmployees();
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_ShouldReturnTopEarningNames() {
        // Arrange
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(employeeNamesList);

        // Act
        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeNamesList, response.getBody());
        verify(employeeService, times(1)).getTopTenHighestEarningEmployeeNames();
    }

    @Test
    void createEmployee_WhenSuccessful_ShouldReturnCreatedEmployee() {
        // Arrange
        when(employeeService.createEmployee(employeeInput)).thenReturn(employee);

        // Act
        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
        verify(employeeService, times(1)).createEmployee(employeeInput);
    }

    @Test
    void createEmployee_WhenUnsuccessful_ShouldReturnBadRequest() {
        // Arrange
        when(employeeService.createEmployee(employeeInput)).thenReturn(null);

        // Act
        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService, times(1)).createEmployee(employeeInput);
    }

    @Test
    void deleteEmployeeById_WhenSuccessful_ShouldReturnDeletedId() {
        // Arrange
        String employeeId = "1";
        when(employeeService.deleteEmployeeById(employeeId)).thenReturn(true);

        // Act
        ResponseEntity<String> response = employeeController.deleteEmployeeById(employeeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeId, response.getBody());
        verify(employeeService, times(1)).deleteEmployeeById(employeeId);
    }

    @Test
    void deleteEmployeeById_WhenUnsuccessful_ShouldReturnNotFound() {
        // Arrange
        String employeeId = "999";
        when(employeeService.deleteEmployeeById(employeeId)).thenReturn(false);

        // Act
        ResponseEntity<String> response = employeeController.deleteEmployeeById(employeeId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService, times(1)).deleteEmployeeById(employeeId);
    }
}
