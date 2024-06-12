package com.example.demo.models;

import com.example.demo.models.types.Subject;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name must be field")
    private String name;
    @NotEmpty(message = "Lastname must be field")
    private String lastName;
    @Column(unique = true)
    @Email
    @NotEmpty(message = "Email must be field")
    private String email;
    @NotEmpty(message = "Password must be field")
    private String password;
    @Past(message = "Date have to be in past")
    private LocalDate dateBirth;
}