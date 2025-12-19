package com.codewithrihab.employeeapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String location;
    private String role;
    private String photoUrl;

    public EmployeeDto() {

    }
}
