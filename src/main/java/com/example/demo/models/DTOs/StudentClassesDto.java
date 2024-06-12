package com.example.demo.models.DTOs;

import com.example.demo.models.types.Subject;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentClassesDto {
    private Long id;
    private LocalDateTime date;
    private String teacherEmail;
    private String teacherName;
    private String teacherLastName;
    private Subject subject;
    private Long studentId;
}