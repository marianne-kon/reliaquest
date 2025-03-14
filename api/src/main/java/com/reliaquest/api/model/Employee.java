package com.reliaquest.api.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class Employee implements Serializable {

    private String id;
    private String employee_name;
    private int employee_salary;
    private int employee_age;
    private String employee_title;
    private String employee_email;
}
