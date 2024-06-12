package com.example.demo.repositories;

import com.example.demo.models.TeacherClassesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherClassesRepository extends JpaRepository<TeacherClassesEntity, Long> {
}
