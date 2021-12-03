package com.pplflw.test.pplflwtest.service;

import com.pplflw.test.pplflwtest.model.Employee;
import com.pplflw.test.pplflwtest.model.EmployeeState;
import com.pplflw.test.pplflwtest.repository.EmployeeRepository;
import com.pplflw.test.pplflwtest.service.exceptions.EmployeeNotFoundException;
import com.pplflw.test.pplflwtest.service.exceptions.EmployeeStateChangeNotAllowed;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;


    @Test
    void employeeIsSavedSuccessfully() {
        Employee employee = getDefEmployee();
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeService.save(employee);

        Assertions.assertThat(savedEmployee.getState()).isEqualTo(EmployeeState.ADDED);
        Assertions.assertThat(savedEmployee.getContractId()).isNotNull();
    }

    @Test
    void employeeStateIsChangedSuccessfully() {
        Employee employee = getDefEmployee();
        UUID contractId = UUID.randomUUID();
        when(employeeRepository.findByContractId(contractId)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee changedEmployee = employeeService.changeState(contractId.toString(), EmployeeState.IN_CHECK);

        Assertions.assertThat(changedEmployee.getState()).isEqualTo(EmployeeState.IN_CHECK);
    }

    @Test
    void employeeStateChangeThrowsEmployeeNotFound() {
        UUID contractId = UUID.randomUUID();
        when(employeeRepository.findByContractId(contractId)).thenReturn(Optional.empty());


        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.changeState(contractId.toString(), EmployeeState.IN_CHECK));

        Mockito.verify(employeeRepository, times(0)).save(any(Employee.class));

    }

    @Test
    void employeeStateChangeNotAllowedToAdded() {
        Employee employee = getDefEmployee();
        employee.setState(EmployeeState.ADDED);
        UUID contractId = UUID.randomUUID();
        when(employeeRepository.findByContractId(contractId)).thenReturn(Optional.of(employee));

        assertThrows(EmployeeStateChangeNotAllowed.class,
                () -> employeeService.changeState(contractId.toString(), EmployeeState.ADDED));

        Mockito.verify(employeeRepository, times(0)).save(any(Employee.class));
    }


    @Test
    void employeeStateChangeNotAllowedWhenStateIsAlreadyInCheck() {
        Employee employee = getDefEmployee();
        employee.setState(EmployeeState.IN_CHECK);
        UUID contractId = UUID.randomUUID();
        when(employeeRepository.findByContractId(contractId)).thenReturn(Optional.of(employee));

        assertThrows(EmployeeStateChangeNotAllowed.class,
                () -> employeeService.changeState(contractId.toString(), EmployeeState.IN_CHECK));

        Mockito.verify(employeeRepository, times(0)).save(any(Employee.class));
    }


    private Employee getDefEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("firstName");
        employee.setLastName("lastName");
        employee.setAge(18);
        employee.setEmail("test@email.com");
        return employee;
    }
}