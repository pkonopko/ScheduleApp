package com.example.demo;

import com.example.demo.exceptions.TeacherNotFoundExceptions;
import com.example.demo.models.DTOs.TeacherClassesDto;
import com.example.demo.models.DTOs.TeacherDto;
import com.example.demo.models.TeacherClassesEntity;
import com.example.demo.models.TeacherEntity;
import com.example.demo.models.types.Subject;
import com.example.demo.models.TeacherEntity;
import com.example.demo.models.webDTOs.TeacherWebDto;
import com.example.demo.repositories.TeacherClassesRepository;
import com.example.demo.repositories.TeacherRepository;
import com.example.demo.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TeacherServiceTest {

    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private TeacherClassesRepository teacherClassesRepository;

    private TeacherService teacherService;
    private TeacherEntity teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setup() {
        teacherService = new TeacherService(modelMapper, teacherRepository, teacherClassesRepository);

        Set<Subject> subjects = new HashSet<>();
        subjects.add(Subject.MATH);

        teacher = TeacherEntity.builder()
                .id(1L)
                .name("Krzysztof")
                .lastName("Sor")
                .email("k.s@wp.pl")
                .password("sorkrzy")
                .dateBirth(LocalDate.of(1990, 2, 2))
                .subjects(subjects)
                .build();

        teacher.setTimetable(List.of(TeacherClassesEntity.builder()
                .id(1L)
                .teacher(teacher)
                .studentId(2L)
                .date(LocalDateTime.of(2024, 5, 5, 9, 0))
                .occupied(true)
                .subject(Subject.MATH)
                .build()));

        Set<Subject> subject2 = new HashSet<>();
        subject2.add(Subject.MATH);

        teacherDto = TeacherDto.builder()
                .id(1L)
                .name("Krzysztof")
                .lastName("Sor")
                .email("k.s@wp.pl")
                .password("sorkrzy")
                .dateBirth(LocalDate.of(1990, 2, 2))
                .subjects(subject2)
                .build();
    }

    @Test
    void shouldSaveTeacher() {
        when(modelMapper.map(teacherDto, TeacherEntity.class)).thenReturn(teacher);
        when(modelMapper.map(teacher, TeacherDto.class)).thenReturn(teacherDto);
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        TeacherDto saved = teacherService.save(teacherDto);

        verify(teacherRepository).save(teacher);
        assertEquals(teacherDto, saved);
    }

    @Test
    void shouldGetTimetable() {
        TeacherClassesDto tcd = TeacherClassesDto.builder()
                .id(1L)
                .teacherId(teacher.getId())
                .studentId(2L)
                .date(LocalDateTime.of(2024, 5, 5, 9, 0))
                .occupied(true)
                .subject(Subject.MATH)
                .build();
        List<TeacherClassesDto> expected = List.of(tcd);

        when(teacherRepository.existsById(teacher.getId())).thenReturn(true);
        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.ofNullable(teacher));
        when(modelMapper.map(teacher.getTimetable().get(0), TeacherClassesDto.class)).thenReturn(tcd);

        List<TeacherClassesDto> toCheck = teacherService.getTeacherTimeTable(teacher.getId());

        assertEquals(expected, toCheck);
    }
    @Test
    void shouldNotGetTimetable() {
        when(teacherRepository.existsById(teacher.getId())).thenReturn(false);
        assertThrows(TeacherNotFoundExceptions.class, () -> teacherService.getTeacherTimeTable(teacher.getId()));
    }


    @Test
    void shouldUpdateTeacher() {
        TeacherWebDto tw = TeacherWebDto.builder()
                .name("Ala")
                .lastName("Sor")
                .email("k.s@wp.pl")
                .password("sorkrzy")
                .dateBirth(LocalDate.of(1990, 2, 2))
                .subjects(Set.of(Subject.MATH))
                .build();

        TeacherDto newTeacherDto = TeacherDto.builder()
                .name("Ala")
                .email("k.s@wp.pl")
                .build();

        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.ofNullable(teacher));
        when(modelMapper.map(teacher, TeacherDto.class)).thenReturn(teacherDto);
        when(modelMapper.map(teacherDto, TeacherEntity.class)).thenReturn(teacher);

        TeacherDto expected = teacherService.updateTeacher(teacher.getId(), tw);

        assertEquals(expected.getEmail(), newTeacherDto.getEmail());
        assertEquals(expected.getName(), newTeacherDto.getName());
    }
    @Test
    void shouldNotUpdateTeacher() {
        TeacherWebDto tw = TeacherWebDto.builder()
                .name("Ala")
                .lastName("Sor")
                .email("k.s@wp.pl")
                .password("sorkrzy")
                .dateBirth(LocalDate.of(1990, 2, 2))
                .subjects(Set.of(Subject.MATH))
                .build();
        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.ofNullable(null));
        assertThrows(TeacherNotFoundExceptions.class, () -> teacherService.updateTeacher(teacher.getId(), tw));
    }

    @Test
    void shouldDeleteTeacher() {
        when(teacherRepository.existsById(teacher.getId())).thenReturn(true);

        teacherService.deleteTeacher(teacher.getId());

        verify(teacherRepository).deleteById(teacher.getId());
    }

    @Test
    void shouldNotDeleteTeacher() {
        when(teacherRepository.existsById(teacher.getId())).thenReturn(false);
        assertThrows(TeacherNotFoundExceptions.class, () -> teacherService.deleteTeacher(teacher.getId()));
    }
}