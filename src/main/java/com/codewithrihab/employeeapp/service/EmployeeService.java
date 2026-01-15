package com.codewithrihab.employeeapp.service;

import com.codewithrihab.employeeapp.dtos.CreateEmployeeRequest;
import com.codewithrihab.employeeapp.dtos.EmployeeDto;
import com.codewithrihab.employeeapp.entities.Employee;
import com.codewithrihab.employeeapp.mappers.EmployeeDtoMapper;
import com.codewithrihab.employeeapp.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeDtoMapper employeeDtoMapper;
    private final ImageService imageService;


    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeDtoMapper employeeDtoMapper, ImageService imageService
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeDtoMapper = employeeDtoMapper;
        this.imageService = imageService;
    }

    public EmployeeDto createEmployee(CreateEmployeeRequest request, MultipartFile photo) throws Exception {

        String uploadDirectory = "src/main/resources/static/images/ads";
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new Exception("Email already exists");
        }

        Employee emp = employeeDtoMapper.toEntity(request);

        if (photo != null && !photo.isEmpty() && ImageService.isValidImage(photo)) {
            emp.setPhotoUrl(imageService.saveImageToStorage(uploadDirectory, photo));
        }

        return employeeDtoMapper.toDto(employeeRepository.save(emp));
    }


    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public EmployeeDto getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeDtoMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public void deleteEmployee(Long id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeRepository.delete(emp);

    }

    public boolean emailExists(String email) {
        return employeeRepository.existsByEmail(email);
    }
}