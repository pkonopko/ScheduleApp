package com.example.demo.models.DTOs;

import com.example.demo.models.types.Subject;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherClassesDto {
    private Long id;
    private LocalDateTime date;
    private boolean occupied;
    private Subject subject;
    private Long studentId;
    private Long teacherId;
}
