package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.api.model.Entity;
import com.reliaquest.api.model.EntityEmployee;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    private static final String API_BASE_URL = "http://test-api.com/employees";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private List<Employee> mockEmployees;
    private Entity mockEntity;
    private EntityEmployee mockEntityEmployee;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(employeeService, "apiBaseUrl", API_BASE_URL);

        // Create mock data
        mockEmployees = new ArrayList<>();

        Employee employee1 = new Employee();
        employee1.setEmployee_name("John Doe");
        employee1.setEmployee_salary(100000);
        employee1.setEmployee_title("test");
        employee1.setEmployee_age(30);
        employee1.setId("1");

        Employee employee2 = new Employee();
        employee2.setEmployee_name("Jane Smith");
        employee2.setEmployee_salary(120000);
        employee2.setId("2");

        Employee employee3 = new Employee();
        employee3.setEmployee_name("Bob Johnson");
        employee3.setEmployee_salary(90000);
        employee3.setId("3");

        mockEmployees.add(employee1);
        mockEmployees.add(employee2);
        mockEmployees.add(employee3);

        mockEntity = new Entity();
        mockEntity.setData(mockEmployees);

        mockEntityEmployee = new EntityEmployee();
        mockEntityEmployee.setData(employee1);
        mockEntityEmployee.setStatus("success");
    }

    @Test
    void getAllEmployees_ShouldReturnAllEmployees() {
        // Arrange
        ResponseEntity<Entity> responseEntity = new ResponseEntity<>(mockEntity, HttpStatus.OK);
        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("John Doe", result.get(0).getEmployee_name());
        assertEquals("Jane Smith", result.get(1).getEmployee_name());
        assertEquals("Bob Johnson", result.get(2).getEmployee_name());

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void getAllEmployees_WhenException_ShouldReturnEmptyList() {
        // Arrange
        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Error fetching all employees"));

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void getAllEmployees_ShouldReturnEmptyList() {
        // Arrange
        ResponseEntity<Entity> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void getEmployeesByNameSearch_ShouldReturnEmployeesByNameSearch() {
        // Arrange
        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(mockEntity, HttpStatus.OK));

        // Act
        List<Employee> result = employeeService.getEmployeesByNameSearch("John Doe");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getEmployee_name());

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void getEmployeesByNameSearch_WhenException_ShouldReturnEmptyList() {
        // Arrange
        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Error fetching all employees"));

        // Act
        List<Employee> result = employeeService.getEmployeesByNameSearch("John Doe");

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void getEmployeesByNameSearch_ShouldReturnEmptyList() {
        // Arrange
        ResponseEntity<Entity> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        // Act
        List<Employee> result = employeeService.getEmployeesByNameSearch("John Doe");

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void getEmployeeById_ShouldReturnEmployeeById() {
        // Arrange
        when(restTemplate.getForEntity(API_BASE_URL + "/1", EntityEmployee.class))
                .thenReturn(new ResponseEntity<>(mockEntityEmployee, HttpStatus.OK));

        // Act
        Employee result = employeeService.getEmployeeById("1");

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getEmployee_name());

        verify(restTemplate, times(1)).getForEntity(API_BASE_URL + "/1", EntityEmployee.class);
    }

    @Test
    void getEmployeeById_WhenException_ShouldReturnNull() {
        // Arrange
        when(restTemplate.getForEntity(API_BASE_URL + "/1", EntityEmployee.class))
                .thenThrow(new RuntimeException("Error fetching employee by ID: 1"));

        // Act
        Employee result = employeeService.getEmployeeById("1");

        // Assert
        assertNull(result);

        verify(restTemplate, times(1)).getForEntity(API_BASE_URL + "/1", EntityEmployee.class);
    }

    @Test
    void getEmployeeById_ShouldReturnNull() {
        // Arrange
        ResponseEntity<EntityEmployee> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.getForEntity(API_BASE_URL + "/1", EntityEmployee.class))
                .thenReturn(responseEntity);

        // Act
        Employee result = employeeService.getEmployeeById("1");

        // Assert
        assertNull(result);

        verify(restTemplate, times(1)).getForEntity(API_BASE_URL + "/1", EntityEmployee.class);
    }

    @Test
    void getHighestSalaryOfEmployees_ShouldReturnHighestSalary() {
        // Arrange
        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(mockEntity, HttpStatus.OK));

        // Act
        int result = employeeService.getHighestSalaryOfEmployees();

        // Assert
        assertEquals(120000, result);

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void getHighestSalaryOfEmployees_WhenException_ShouldReturnZero() {
        // Arrange
        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Error fetching all employees"));

        // Act
        int result = employeeService.getHighestSalaryOfEmployees();

        // Assert
        assertEquals(0, result);

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void getHighestSalaryOfEmployees_ShouldReturnZero() {
        // Arrange
        ResponseEntity<Entity> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        // Act
        int result = employeeService.getHighestSalaryOfEmployees();

        // Assert
        assertEquals(0, result);

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_ShouldReturnTopTenHighestEarningEmployeeNames() {
        // Arrange
        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(mockEntity, HttpStatus.OK));

        // Act
        List<String> result = employeeService.getTopTenHighestEarningEmployeeNames();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Jane Smith", result.get(0));
        assertEquals("John Doe", result.get(1));
        assertEquals("Bob Johnson", result.get(2));

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_WhenException_ShouldReturnEmptyList() {
        // Arrange
        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Error fetching all employees"));

        // Act
        List<String> result = employeeService.getTopTenHighestEarningEmployeeNames();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_ShouldReturnEmptyList() {
        // Arrange
        ResponseEntity<Entity> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        // Act
        List<String> result = employeeService.getTopTenHighestEarningEmployeeNames();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void createEmployee_ShouldCreateEmployee() {
        // Arrange
        EmployeeInput employeeInput = new EmployeeInput();
        employeeInput.setName("John Doe");
        employeeInput.setSalary(100000);
        employeeInput.setAge(30);
        employeeInput.setTitle("test");

        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(EntityEmployee.class)))
                .thenReturn(new ResponseEntity<>(mockEntityEmployee, HttpStatus.OK));

        // Act
        Employee result = employeeService.createEmployee(employeeInput);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getEmployee_name());

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(EntityEmployee.class));
    }

    @Test
    void createEmployee_WhenException_ShouldReturnNull() {
        // Arrange
        EmployeeInput employeeInput = new EmployeeInput();
        employeeInput.setName("John Doe");
        employeeInput.setSalary(100000);
        employeeInput.setAge(30);
        employeeInput.setTitle("test");

        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(EntityEmployee.class)))
                .thenThrow(new RuntimeException("Error creating employee"));

        // Act
        Employee result = employeeService.createEmployee(employeeInput);

        // Assert
        assertNull(result);

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(EntityEmployee.class));
    }

    @Test
    void createEmployee_WhenCreateFails_ShouldReturnNull() {
        // Arrange
        EmployeeInput employeeInput = new EmployeeInput();
        employeeInput.setName("John Doe");
        employeeInput.setSalary(100000);
        employeeInput.setAge(30);
        employeeInput.setTitle("test");

        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(EntityEmployee.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        Employee result = employeeService.createEmployee(employeeInput);

        // Assert
        assertNull(result);

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(EntityEmployee.class));
    }

    @Test
    void createEmployee_WhenValidationFails_ShouldReturnBadRequest() {
        // Arrange
        EmployeeInput employeeInput = new EmployeeInput();
        employeeInput.setName("John Doe");
        employeeInput.setSalary(100000);

        when(restTemplate.exchange(
                        eq(API_BASE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(EntityEmployee.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

        // Act
        Employee result = employeeService.createEmployee(employeeInput);

        // Assert
        assertNull(result);

        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(EntityEmployee.class));
    }

    @Test
    void deleteEmployeeById_ShouldDeleteEmployeeById() {
        // Arrange
        Employee employee = new Employee();
        employee.setEmployee_name("John Doe");
        employee.setId("1");

        when(restTemplate.getForEntity(API_BASE_URL + "/1", EntityEmployee.class))
                .thenReturn(new ResponseEntity<>(mockEntityEmployee, HttpStatus.OK));

        when(restTemplate.exchange(eq(API_BASE_URL), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("1", HttpStatus.OK));

        // Act
        boolean result = employeeService.deleteEmployeeById("1");

        // Assert
        assertTrue(result);

        verify(restTemplate, times(1)).getForEntity(API_BASE_URL + "/1", EntityEmployee.class);
        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void deleteEmployeeById_WhenEmployeeNotFound_ShouldReturnFalse() {
        // Arrange
        when(restTemplate.getForEntity(API_BASE_URL + "/1", EntityEmployee.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // Act
        boolean result = employeeService.deleteEmployeeById("1");

        // Assert
        assertFalse(result);

        verify(restTemplate, times(1)).getForEntity(API_BASE_URL + "/1", EntityEmployee.class);
        verify(restTemplate, never())
                .exchange(eq(API_BASE_URL), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void deleteEmployeeById_WhenException_ShouldReturnFalse() {
        // Arrange
        when(restTemplate.getForEntity(API_BASE_URL + "/1", EntityEmployee.class))
                .thenThrow(new RuntimeException("Error fetching employee by ID: 1"));

        // Act
        boolean result = employeeService.deleteEmployeeById("1");

        // Assert
        assertFalse(result);

        verify(restTemplate, times(1)).getForEntity(API_BASE_URL + "/1", EntityEmployee.class);
        verify(restTemplate, never())
                .exchange(eq(API_BASE_URL), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void deleteEmployeeById_WhenDeleteFails_ShouldReturnFalse() {
        // Arrange
        Employee employee = new Employee();
        employee.setEmployee_name("John Doe");
        employee.setId("1");

        when(restTemplate.getForEntity(API_BASE_URL + "/1", EntityEmployee.class))
                .thenReturn(new ResponseEntity<>(mockEntityEmployee, HttpStatus.OK));

        when(restTemplate.exchange(eq(API_BASE_URL), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        boolean result = employeeService.deleteEmployeeById("1");

        // Assert
        assertFalse(result);

        verify(restTemplate, times(1)).getForEntity(API_BASE_URL + "/1", EntityEmployee.class);
        verify(restTemplate, times(1))
                .exchange(eq(API_BASE_URL), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(String.class));
    }
}
