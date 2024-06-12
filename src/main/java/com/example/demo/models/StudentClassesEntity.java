package com.example.demo.models;

import com.example.demo.models.types.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentClassesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private String teacherEmail;
    private String teacherName;
    private String teacherLastName;
    private Subject subject;
    @ManyToOne(targetEntity = StudentEntity.class)
    private StudentEntity student;
}