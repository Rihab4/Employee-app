package com.codewithrihab.employeeapp.services;

import com.codewithrihab.employeeapp.dtos.CreateEmployeeRequest;
import com.codewithrihab.employeeapp.dtos.EmployeeDto;
import com.codewithrihab.employeeapp.entities.Employee;
import com.codewithrihab.employeeapp.mappers.EmployeeDtoMapper;
import com.codewithrihab.employeeapp.repositories.EmployeeRepository;
import com.codewithrihab.employeeapp.service.EmployeeService;
import com.codewithrihab.employeeapp.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeDtoMapper employeeDtoMapper;
    @Mock
    private ImageService imageService;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService(employeeRepository, employeeDtoMapper, imageService);
    }

    @Test
    void createEmployeeAlreadyExistInDb() {
        // given
        CreateEmployeeRequest emp = new CreateEmployeeRequest();
        MultipartFile photo = new MockMultipartFile("photo", "test.jpg", "image/png", MediaType.IMAGE_JPEG_VALUE.getBytes());
        doReturn(true).when(employeeRepository).existsByEmail(emp.getEmail());
        // when
        assertThrows(Exception.class, () -> employeeService.createEmployee(emp, photo));
    }

    @Test
    void createNewEmployeeWithoutPhoto() throws Exception {
        // given
        CreateEmployeeRequest emp = new CreateEmployeeRequest();
        emp.setEmail("new@example.com");

        Employee employeeEntity = new Employee();
        when(employeeDtoMapper.toEntity(emp)).thenReturn(employeeEntity);

        when(employeeRepository.existsByEmail(emp.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employeeEntity);

        // when
        employeeService.createEmployee(emp, null);

        // then
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployeeSuccess() throws Exception {
        // given
        CreateEmployeeRequest emp = new CreateEmployeeRequest();
        emp.setEmail("new@example.com");

        Employee employeeEntity = new Employee();
        EmployeeDto employeeDto = new EmployeeDto();

        MultipartFile photo = new MockMultipartFile(
                "photo", "test.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]
        );

        doReturn(false).when(employeeRepository).existsByEmail(emp.getEmail());
        when(employeeDtoMapper.toEntity(emp)).thenReturn(employeeEntity);
        when(employeeRepository.save(employeeEntity)).thenReturn(employeeEntity);
        when(employeeDtoMapper.toDto(employeeEntity)).thenReturn(employeeDto);

        // when
        EmployeeDto result = employeeService.createEmployee(emp, photo);

        // then
        assert (result == employeeDto);
    }

    @Test
    void getAllEmployees() {
        // given
        List<Employee> employeeList = new ArrayList<>();
        Employee employee = new Employee();
        EmployeeDto employeeDto = new EmployeeDto();
        employeeList.add(employee);
        when(employeeRepository.findAll()).thenReturn(employeeList);
        when(employeeDtoMapper.toDto(employee)).thenReturn(employeeDto);
        // when
        var result = employeeService.getAllEmployees();

        // then
        assertEquals(1, result.size());
        assertEquals(employeeDto, result.get(0));
    }

    @Test
    void getEmployeeById() {
        // given
        Employee employee = new Employee();
        EmployeeDto employeeDto = new EmployeeDto();
        when(employeeDtoMapper.toDto(employee)).thenReturn(employeeDto);

        employee.setId(1L);
        employee.setId(1L);
        doReturn(Optional.of(employee)).when(employeeRepository).findById(1L);

        // when
        var result = employeeService.getEmployeeById(1L);

        // then
        assertEquals(employeeDto, result);
    }

    @Test
    void getEmployeeByIdNotFound() {
        // given
        Employee employee = new Employee();
        employee.setId(1L);
        doReturn(null).when(employeeRepository).findById(1L);

        // when // then
        assertThrows(RuntimeException.class, () -> employeeService.getEmployeeById(1L));

    }

    @Test
    void deleteEmployeeById() {
        //given
        Employee emp1 = new Employee();
        emp1.setId(1L);

        doReturn(Optional.of(emp1)).when(employeeRepository).findById(1L);
        // when
        employeeService.deleteEmployee(1L);
        // then
        verify(employeeRepository, times(1)).delete(emp1);
    }

}