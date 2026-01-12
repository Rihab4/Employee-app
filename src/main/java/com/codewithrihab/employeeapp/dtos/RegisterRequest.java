package com.codewithrihab.employeeapp.dtos;

import com.codewithrihab.employeeapp.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;

}
