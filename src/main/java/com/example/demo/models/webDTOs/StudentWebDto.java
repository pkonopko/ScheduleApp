package com.example.demo.models.webDTOs;

import com.example.demo.models.StudentClassesEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@Getter
@Builder
public class StudentWebDto {
    String name;
    String lastName;
    String email;
    String password;
    LocalDate dateBirth;
}