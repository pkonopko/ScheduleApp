package com.example.demo.models.DTOs;

import com.example.demo.models.StudentClassesEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private LocalDate dateBirth;
    private List<StudentClassesEntity> myClasses; //zamien na dto
}