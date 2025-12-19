package com.codewithrihab.employeeapp.controllers;

import com.codewithrihab.employeeapp.dtos.CreateEmployeeRequest;
import com.codewithrihab.employeeapp.dtos.EmployeeDto;
import com.codewithrihab.employeeapp.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;


    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/add")
    public ResponseEntity<EmployeeDto> add(
            @ModelAttribute CreateEmployeeRequest createEmployeeRequest,
            @RequestPart(required = false) MultipartFile photo) throws Exception {
        if (employeeService.emailExists(createEmployeeRequest.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        EmployeeDto dto = employeeService.createEmployee(createEmployeeRequest, photo);
        return ResponseEntity.ok().body(dto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        try {
            EmployeeDto  dto = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Employee deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
}
