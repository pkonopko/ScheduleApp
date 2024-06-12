package com.example.demo.models;

import com.example.demo.models.types.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherClassesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private boolean occupied;
    private Subject subject;
    private Long studentId;
    @ManyToOne(targetEntity = TeacherEntity.class)
    private TeacherEntity teacher;
}