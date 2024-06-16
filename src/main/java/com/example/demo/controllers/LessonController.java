package com.example.demo.controllers;

import com.example.demo.models.DTOs.BookLessonDto;
import com.example.demo.models.DTOs.StudentClassesDto;
import com.example.demo.models.DTOs.TeacherClassesDto;
import com.example.demo.models.types.Subject;
import com.example.demo.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/{id}")
    public ResponseEntity<List<StudentClassesDto>> getStudentLessons(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getStudentLessons(id));
    }

    @PostMapping("/{studentId}")
    public ResponseEntity<String> bookLesson(@PathVariable Long studentId, @RequestBody BookLessonDto bookLessonDto) {
        lessonService.bookLesson(bookLessonDto.getTeacherId(), studentId, bookLessonDto.getSubject(), bookLessonDto.getDate());
        return ResponseEntity.ok("Successfully booked lesson");
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> deleteLesson(@PathVariable Long studentId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime date) {
        lessonService.removeLesson(date, studentId);
        return ResponseEntity.ok("Successfully removed lesson");
    }

    @GetMapping
    public ResponseEntity<List<TeacherClassesDto>> getAllAvailableLessonInDay(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam Subject subject) {
        return ResponseEntity.ok(lessonService.getAllAvailableLessonInDay(date, subject));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<String> changeLessonDate(@PathVariable Long studentId, @RequestBody Map<String, LocalDateTime> dates) {
        lessonService.changeLessonDate(studentId, dates);
        return ResponseEntity.ok("Lesson date has changed");
    }
}