package com.example.demo.repositories;

import com.example.demo.models.StudentClassesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface StudentClassesRepository extends JpaRepository<StudentClassesEntity, Long> {
    StudentClassesEntity getStudentClassesEntityByStudentIdAndDate(Long id, LocalDateTime date);
}
