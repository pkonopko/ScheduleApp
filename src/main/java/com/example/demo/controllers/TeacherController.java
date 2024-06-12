package com.example.demo.controllers;

import com.example.demo.models.DTOs.TeacherClassesDto;
import com.example.demo.models.DTOs.TeacherDto;
import com.example.demo.models.TeacherClassesEntity;
import com.example.demo.models.TeacherEntity;
import com.example.demo.models.webDTOs.TeacherWebDto;
import com.example.demo.services.TeacherService;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/{id}")
    public ResponseEntity<List<TeacherClassesDto>> getTeacherTimeTable (@PathVariable Long id){
        return ResponseEntity.ok(teacherService.getTeacherTimeTable(id));
    }

    @PostMapping
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody TeacherWebDto newTeacher){
        TeacherDto teacherToSave = TeacherDto.builder()
                .name(newTeacher.getName())
                .lastName(newTeacher.getLastName())
                .email(newTeacher.getEmail())
                .password(newTeacher.getPassword())
                .dateBirth(newTeacher.getDateBirth())
                .subjects(newTeacher.getSubjects())
                .build();
        return ResponseEntity.ok(teacherService.save(teacherToSave));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDto> editTeacher(@PathVariable Long id,@RequestBody TeacherWebDto newTeacherData) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, newTeacherData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok("Teacher deleted");
    }
}
