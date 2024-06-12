package com.example.demo.models.DTOs;

import com.example.demo.models.types.Subject;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class BookLessonDto {
    private Subject subject;
    private Long teacherId;
    private LocalDateTime date;
}
