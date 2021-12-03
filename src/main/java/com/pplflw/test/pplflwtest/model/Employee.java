package com.pplflw.test.pplflwtest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "first_name")
    @NotNull(message = "First Name must be provided")
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "Last Name must be provided")
    private String lastName;

    @Column(name = "email")

    @NotNull(message = "Email must be provided")
    @Email(message = "Please provide correct email address")
    private String email;

    @Column(name = "contract_id")
    @Hidden
    private UUID contractId;

    @Column(name = "state_id")
    @Hidden
    private EmployeeState state;

    @Column(name = "age")
    @Min(value = 18, message = "Employee age  must be above 18")
    @Max(value = 60, message = "Employee age  must be bellow 60")
    private int age;

}
