package com.reliaquest.api.model;

import lombok.Data;

@Data
public class EmployeeInput {

    private String id;
    private String name;
    private int salary;
    private String title;
    private int age;
    private String email;
}
