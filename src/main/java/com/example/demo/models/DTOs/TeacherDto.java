package com.example.demo.models.DTOs;

import com.example.demo.models.types.Subject;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private LocalDate dateBirth;
    private Set<Subject> subjects;
}
