package com.reliaquest.api.service;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.api.model.Entity;
import com.reliaquest.api.model.EntityEmployee;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = Logger.getLogger(EmployeeServiceImpl.class.getName());
    private static final int TOP_EARNERS_COUNT = 10;
    private static final String EMPLOYEES_CACHE = "employeesCache";

    private final RestTemplate restTemplate;
    private final String apiBaseUrl;

    @Autowired
    public EmployeeServiceImpl(
            RestTemplate restTemplate,
            @Value("${api.base-url:http://localhost:8112/api/v1/employee}") String apiBaseUrl) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
    }

    @Override
    //@Cacheable(value = EMPLOYEES_CACHE, key = "#root.methodName")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public List<Employee> getAllEmployees() {
        try {
            ResponseEntity<Entity> response = restTemplate.exchange(
                    apiBaseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<Entity>() {});

            return Optional.ofNullable(response.getBody()).map(Entity::getData).orElse(Collections.emptyList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching all employees", e);
            return Collections.emptyList();
        }
    }

    @Override
    @Cacheable(value = EMPLOYEES_CACHE, key = "#searchString")
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        try {
            List<Employee> allEmployees = getAllEmployees();

            return allEmployees.stream()
                    .filter(employee -> employee.getEmployee_name().contains(searchString))
                    .toList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching employees by name", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Employee getEmployeeById(String id) {
        try {
            String url = apiBaseUrl + "/" + id;
            ResponseEntity<EntityEmployee> response = restTemplate.getForEntity(url, EntityEmployee.class);

            return Optional.ofNullable(response.getBody())
                    .map(EntityEmployee::getData)
                    .orElse(null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching employee by ID: " + id, e);
            return null;
        }
    }

    @Override
    public int getHighestSalaryOfEmployees() {
        try {
            List<Employee> employees = getAllEmployees();

            return employees.stream()
                    .map(Employee::getEmployee_salary)
                    .max(Double::compare)
                    .orElse(0);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting highest salary", e);
            return 0;
        }
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        try {
            List<Employee> employees = getAllEmployees();

            return employees.stream()
                    .sorted(Comparator.comparing(Employee::getEmployee_salary).reversed())
                    .limit(TOP_EARNERS_COUNT)
                    .map(Employee::getEmployee_name)
                    .toList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting top earning employees", e);
            return Collections.emptyList();
        }
    }

    @Override
    @CacheEvict(value = "employeesCache", allEntries = true)
    public Employee createEmployee(EmployeeInput employeeInput) {
        try {
            HttpEntity<EmployeeInput> request = new HttpEntity<>(employeeInput);
            ResponseEntity<EntityEmployee> response =
                    restTemplate.exchange(apiBaseUrl, HttpMethod.POST, request, EntityEmployee.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                return null;
            } else {
                return Optional.ofNullable(response.getBody())
                        .map(EntityEmployee::getData)
                        .orElse(null);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating employee", e);
            return null;
        }
    }

    @Override
    public boolean deleteEmployeeById(String id) {
        try {
            Employee employee = getEmployeeById(id);
            if (employee == null) {
                return false;
            }

            EmployeeInput employeeInput = new EmployeeInput();
            employeeInput.setName(employee.getEmployee_name());
            HttpEntity<EmployeeInput> request = new HttpEntity<>(employeeInput);

            ResponseEntity<String> response =
                    restTemplate.exchange(apiBaseUrl, HttpMethod.DELETE, request, String.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting employee with ID: " + id, e);
            return false;
        }
    }
}
