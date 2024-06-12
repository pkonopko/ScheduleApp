package com.example.demo.controllers;

import com.example.demo.models.DTOs.StudentDto;
import com.example.demo.models.StudentClassesEntity;
import com.example.demo.models.webDTOs.StudentWebDto;
import com.example.demo.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody StudentWebDto newStudent){
        StudentDto studentToSave = StudentDto.builder()
                .name(newStudent.getName())
                .lastName(newStudent.getLastName())
                .email(newStudent.getEmail())
                .password(newStudent.getPassword())
                .dateBirth(newStudent.getDateBirth())
                .myClasses(new ArrayList<>())
                .build();
        return ResponseEntity.ok(studentService.save(studentToSave));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> editStudent(@PathVariable Long id,@RequestBody StudentWebDto newStudentData) {
        return ResponseEntity.ok(studentService.updateStudent(id, newStudentData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.ok("Student deleted");
    }
}
