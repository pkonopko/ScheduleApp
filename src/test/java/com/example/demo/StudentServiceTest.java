package com.example.demo;

import com.example.demo.exceptions.StudentNotFoundExceptions;
import com.example.demo.exceptions.TeacherNotFoundExceptions;
import com.example.demo.models.DTOs.StudentDto;
import com.example.demo.models.DTOs.TeacherDto;
import com.example.demo.models.StudentClassesEntity;
import com.example.demo.models.StudentEntity;
import com.example.demo.models.TeacherEntity;
import com.example.demo.models.types.Subject;
import com.example.demo.models.webDTOs.StudentWebDto;
import com.example.demo.models.webDTOs.TeacherWebDto;
import com.example.demo.repositories.StudentClassesRepository;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.repositories.TeacherClassesRepository;
import com.example.demo.repositories.TeacherRepository;
import com.example.demo.services.StudentService;
import com.example.demo.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StudentServiceTest {
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private StudentRepository studentRepository;

    private StudentService studentService;
    private StudentEntity student;
    private StudentDto studentDto;

    @BeforeEach
    void setup() {
        studentService = new StudentService(modelMapper, studentRepository);
        student = StudentEntity.builder()
                .id(1L)
                .name("Mike")
                .lastName("Fox")
                .email("mf@gmail.com")
                .password("qwww")
                .dateBirth(LocalDate.of(1999, 2, 2))
                .build();
        student.setMyClasses(List.of(StudentClassesEntity.builder()
                .id(1L)
                .date(LocalDateTime.of(2024, 5, 25, 13, 0))
                .subject(Subject.MATH)
                .student(student)
                .teacherName("John")
                .teacherLastName("Van Bommel")
                .teacherEmail("j.vb@gmail.com")
                .build()));

        studentDto = StudentDto.builder()
                .id(1L)
                .name("Mike")
                .lastName("Fox")
                .email("mf@gmail.com")
                .password("qwww")
                .dateBirth(LocalDate.of(1999, 2, 2))
                .build();
    }

    @Test
    void shouldSaveStudent() {
        when(modelMapper.map(studentDto, StudentEntity.class)).thenReturn(student);
        when(modelMapper.map(student, StudentDto.class)).thenReturn(studentDto);
        when(studentRepository.save(student)).thenReturn(student);

        StudentDto saved = studentService.save(studentDto);

        verify(studentRepository).save(student);
        assertEquals(studentDto, saved);
    }

    @Test
    void shouldEditStudent() {
        StudentWebDto sw = StudentWebDto.builder()
                .name("Ala")
                .lastName("Sor")
                .email("k.s@wp.pl")
                .password("sorkrzy")
                .dateBirth(LocalDate.of(1990, 2, 2))
                .build();

        StudentDto newStudentDto = StudentDto.builder()
                .name("Ala")
                .email("k.s@wp.pl")
                .build();

        when(studentRepository.findById(student.getId())).thenReturn(Optional.ofNullable(student));
        when(modelMapper.map(student, StudentDto.class)).thenReturn(studentDto);
        when(modelMapper.map(studentDto, StudentEntity.class)).thenReturn(student);

        StudentDto expected = studentService.updateStudent(student.getId(), sw);

        assertEquals(expected.getEmail(), newStudentDto.getEmail());
        assertEquals(expected.getName(), newStudentDto.getName());
    }
    @Test
    void shouldNotEditStudent() {
        StudentWebDto sw = StudentWebDto.builder()
                .name("Ala")
                .lastName("Sor")
                .email("k.s@wp.pl")
                .password("sorkrzy")
                .dateBirth(LocalDate.of(1990, 2, 2))
                .build();
        when(studentRepository.findById(student.getId())).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundExceptions.class, () -> studentService.updateStudent(student.getId(), sw));
    }

    @Test
    void shouldDeleteStudent() {
        when(studentRepository.existsById(student.getId())).thenReturn(true);

        studentService.deleteStudentById(student.getId());

        verify(studentRepository).deleteById(student.getId());
    }

    @Test
    void shouldNotDeleteStudent() {
        when(studentRepository.existsById(student.getId())).thenReturn(false);
        assertThrows(StudentNotFoundExceptions.class, () -> studentService.deleteStudentById(student.getId()));
    }
}
    //dokończyć studenta, zrobić exceptions, setUp Lesson


