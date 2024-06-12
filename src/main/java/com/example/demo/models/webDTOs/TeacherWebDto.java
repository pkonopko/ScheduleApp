package com.example.demo.models.webDTOs;

import com.example.demo.models.types.Subject;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDate;
import java.util.Set;

@Value
@Getter
@Builder
public class TeacherWebDto {
    String name;
    String lastName;
    String email;
    String password;
    LocalDate dateBirth;
    Set<Subject> subjects;
}
