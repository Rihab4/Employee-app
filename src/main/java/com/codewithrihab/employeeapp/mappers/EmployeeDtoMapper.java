package com.codewithrihab.employeeapp.mappers;

import com.codewithrihab.employeeapp.dtos.CreateEmployeeRequest;
import com.codewithrihab.employeeapp.dtos.EmployeeDto;
import com.codewithrihab.employeeapp.entities.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeDtoMapper {
    Employee toEntity(CreateEmployeeRequest createEmployeeRequest);
    EmployeeDto toDto(Employee employee);
}
