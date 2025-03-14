# Implement this API

#### In this assessment you will be tasked with filling out the functionality of different methods that will be listed further down.

These methods will require some level of API interactions with Mock Employee API at http://localhost:8112/api/v1/employeeController.

Please keep the following in mind when doing this assessment:
* clean coding practices
* test driven development
* logging
* scalability

### Endpoints to implement

_See `com.reliaquest.api.controller.IEmployeeController` for details._

getAllEmployees()

    output - list of employees
    description - this should return all employees

getEmployeesByNameSearch(...)

    path input - name fragment
    output - list of employees
    description - this should return all employees whose name contains or matches the string input provided

getEmployeeById(...)

    path input - employeeController ID
    output - employeeController
    description - this should return a single employeeController

getHighestSalaryOfEmployees()

    output - integer of the highest salary
    description - this should return a single integer indicating the highest salary of amongst all employees

getTop10HighestEarningEmployeeNames()

    output - list of employees
    description - this should return a list of the top 10 employees based off of their salaries

createEmployee(...)

    body input - attributes necessary to create an employeeController
    output - employeeController
    description - this should return a single employeeController, if created, otherwise error

deleteEmployeeById(...)

    path input - employeeController ID
    output - name of the employeeController
    description - this should delete the employeeController with specified id given, otherwise error

### Testing
Please include proper integration and/or unit tests.
