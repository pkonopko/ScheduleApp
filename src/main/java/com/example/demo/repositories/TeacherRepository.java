package com.example.demo.repositories;

import com.example.demo.models.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TeacherRepository extends JpaRepository<TeacherEntity, Long> {
}
