package com.example.demo;

import com.example.demo.exceptions.*;
import com.example.demo.models.DTOs.*;
import com.example.demo.models.StudentClassesEntity;
import com.example.demo.models.StudentEntity;
import com.example.demo.models.TeacherClassesEntity;
import com.example.demo.models.TeacherEntity;
import com.example.demo.models.types.Subject;
import com.example.demo.repositories.StudentClassesRepository;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.repositories.TeacherClassesRepository;
import com.example.demo.repositories.TeacherRepository;
import com.example.demo.services.LessonService;
import com.example.demo.services.StudentService;
import com.example.demo.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LessonServiceTest {
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private StudentService studentService;
    @MockBean
    private TeacherClassesRepository teacherClassesRepository;
    @MockBean
    private StudentClassesRepository studentClassesRepository;

    private LessonService lessonService;
    private TeacherEntity teacher;
    private TeacherClassesEntity teacherClasses;
    private StudentClassesEntity studentClasses;
    private StudentEntity student;
    private StudentClassesDto studentClassesDto;
    private BookLessonDto bookLessonDto;

    @BeforeEach
    void setup() {
        lessonService = new LessonService(modelMapper, studentRepository, teacherRepository, teacherService, studentService, teacherClassesRepository, studentClassesRepository);
        student = StudentEntity.builder()
                .id(1L)
                .name("Mike")
                .lastName("Fox")
                .email("mf@gmail.com")
                .password("qwww")
                .dateBirth(LocalDate.of(1999, 2, 2))
                .build();

        Set<Subject> subjects = new HashSet<>();
        subjects.add(Subject.MATH);
        teacher = TeacherEntity.builder()
                .id(1L)
                .name("Krzysztof")
                .subjects(subjects)
                .lastName("Sor")
                .email("k.s@wp.pl")
                .password("sorkrzy")
                .dateBirth(LocalDate.of(1990, 2, 2))
                .build();

        LocalDateTime date = LocalDateTime.now().plusDays(2);
        teacherClasses = TeacherClassesEntity.builder()
                .id(1L)
                .date(date.withSecond(0).truncatedTo(ChronoUnit.SECONDS))
                .occupied(true)
                .subject(Subject.MATH)
                .studentId(1L)
                .teacher(teacher)
                .build();

        studentClasses = StudentClassesEntity.builder()
                .id(1L)
                .date(date.withSecond(0).truncatedTo(ChronoUnit.SECONDS))
                .teacherEmail("k.s@wp.pl")
                .teacherName("Krzysztof")
                .teacherLastName("Sor")
                .subject(Subject.MATH)
                .student(student)
                .build();

        studentClassesDto = StudentClassesDto.builder()
                .id(1L)
                .date(LocalDateTime.now().plusDays(2))
                .teacherEmail("k.s@wp.pl")
                .teacherName("Krzysztof")
                .teacherLastName("Sor")
                .subject(Subject.MATH)
                .studentId(1L)
                .build();

        bookLessonDto = BookLessonDto.builder()
                .subject(Subject.MATH)
                .teacherId(1L)
                .date(date.withSecond(0).truncatedTo(ChronoUnit.SECONDS))
                .build();
    }

    @Test
    void shouldGetAllAvailableLessonInDay() {
        LocalDateTime date = LocalDateTime.now().plusDays(2);
        TeacherClassesEntity freeTeacherClasses = TeacherClassesEntity.builder()
                .id(1L)
                .date(date.withSecond(0).truncatedTo(ChronoUnit.SECONDS))
                .occupied(false)
                .subject(null)
                .studentId(null)
                .teacher(teacher)
                .build();

        TeacherClassesDto expectedDto = TeacherClassesDto.builder()
                .id(1L)
                .date(date.withSecond(0).truncatedTo(ChronoUnit.SECONDS))
                .occupied(false)
                .subject(null)
                .studentId(null)
                .teacherId(1L)
                .build();

        when(teacherClassesRepository.findAll()).thenReturn(List.of(freeTeacherClasses));
        when(modelMapper.map(freeTeacherClasses, TeacherClassesDto.class)).thenReturn(expectedDto);
        List<TeacherClassesDto> result = lessonService.getAllAvailableLessonInDay(LocalDate.from(teacherClasses.getDate()), teacherClasses.getSubject());
        assertEquals(1, result.size());
        assertEquals(expectedDto, result.get(0));
    }

    @Test
    void shouldGetStudentLessons() {
        when(studentRepository.existsById(student.getId())).thenReturn(true);
        when(studentClassesRepository.findAll()).thenReturn(List.of(studentClasses));
        when(modelMapper.map(studentClasses, StudentClassesDto.class)).thenReturn(studentClassesDto);

        List<StudentClassesDto> result = lessonService.getStudentLessons(student.getId());

        assertEquals(List.of(studentClassesDto), result);
    }

    @Test
    void shouldNotGetStudentLessons() {
        when(studentRepository.existsById(student.getId())).thenReturn(false);
        assertThrows(StudentNotFoundExceptions.class, () -> lessonService.getStudentLessons(student.getId()));

    }

    @Test
    void shouldBookLesson() {
        Set<Subject> subjects = new HashSet<>();
        subjects.add(Subject.MATH);
        TeacherDto teacherDto = TeacherDto.builder()
                .name("Krzysztof")
                .subjects(subjects)
                .lastName("Sor")
                .email("k.s@wp.pl")
                .password("sorkrzy")
                .dateBirth(LocalDate.of(1990, 2, 2))
                .build();

        StudentDto studentDto = StudentDto.builder()
                .id(1L)
                .name("Mike")
                .lastName("Fox")
                .email("mf@gmail.com")
                .password("qwww")
                .dateBirth(LocalDate.of(1999, 2, 2))
                .myClasses(List.of(studentClasses))
                .build();

        LocalDateTime date = LocalDateTime.now().plusDays(2);
        TeacherClassesEntity freeTeacherClasses = TeacherClassesEntity.builder()
                .id(1L)
                .date(date.withSecond(0).truncatedTo(ChronoUnit.SECONDS))
                .occupied(false)
                .subject(null)
                .studentId(null)
                .teacher(teacher)
                .build();

        when(studentRepository.existsById(student.getId())).thenReturn(true);
        when(teacherRepository.existsById(teacher.getId())).thenReturn(true);
        when(studentClassesRepository.findAll()).thenReturn(List.of());
        when(teacherService.getTeacherDtoById(teacher.getId())).thenReturn(teacherDto);
        when(teacherClassesRepository.findAll()).thenReturn(List.of(freeTeacherClasses));
        when(studentService.getStudentDtoById(student.getId())).thenReturn(studentDto);
        when(modelMapper.map(studentDto, StudentEntity.class)).thenReturn(student);

        lessonService.bookLesson(bookLessonDto.getTeacherId(), student.getId(), bookLessonDto.getSubject(), bookLessonDto.getDate());

        verify(teacherClassesRepository).save(freeTeacherClasses);
        verify(studentClassesRepository).save(any(StudentClassesEntity.class));
    }

    @Test
    void shouldNotBookLesson() {
        when(studentRepository.existsById(student.getId())).thenReturn(false);
        assertThrows(StudentNotFoundExceptions.class, () -> lessonService.bookLesson(bookLessonDto.getTeacherId(), student.getId(), bookLessonDto.getSubject(), bookLessonDto.getDate()));
    }

    @Test
    void shouldNotBookLesson_v2() {
        when(studentRepository.existsById(student.getId())).thenReturn(true);
        when(teacherRepository.existsById(teacher.getId())).thenReturn(false);
        assertThrows(TeacherNotFoundExceptions.class, () -> lessonService.bookLesson(bookLessonDto.getTeacherId(), student.getId(), bookLessonDto.getSubject(), bookLessonDto.getDate()));
    }

    @Test
    void shouldNotBookLesson_v3() {
        Set<Subject> subjects = new HashSet<>();
        subjects.add(Subject.BIOLOGY);
        TeacherDto teacherDto = TeacherDto.builder()
                .name("Krzysztof")
                .subjects(subjects)
                .lastName("Sor")
                .email("k.s@wp.pl")
                .password("sorkrzy")
                .dateBirth(LocalDate.of(1990, 2, 2))
                .build();

        when(studentRepository.existsById(student.getId())).thenReturn(true);
        when(teacherRepository.existsById(teacher.getId())).thenReturn(true);
        when(studentClassesRepository.findAll()).thenReturn(List.of(studentClasses));
        when(modelMapper.map(studentClasses, StudentClassesDto.class)).thenReturn(studentClassesDto);
        when(teacherService.getTeacherDtoById(teacher.getId())).thenReturn(teacherDto);

        assertThrows(BadParamException.class, () -> lessonService.bookLesson(bookLessonDto.getTeacherId(), student.getId(), bookLessonDto.getSubject(), bookLessonDto.getDate()));
    }

    @Test
    void shouldNotBookLesson_v4() {

        Set<Subject> subjects = new HashSet<>();
        subjects.add(Subject.MATH);
        TeacherDto teacherDto = TeacherDto.builder()
                .name("Krzysztof")
                .subjects(subjects)
                .lastName("Sor")
                .email("k.s@wp.pl")
                .password("sorkrzy")
                .dateBirth(LocalDate.of(1990, 2, 2))
                .build();

        when(studentRepository.existsById(student.getId())).thenReturn(true);
        when(teacherRepository.existsById(teacher.getId())).thenReturn(true);
        when(studentClassesRepository.findAll()).thenReturn(List.of());
        when(teacherService.getTeacherDtoById(teacher.getId())).thenReturn(teacherDto);
        when(modelMapper.map(studentClasses, StudentClassesDto.class)).thenReturn(studentClassesDto);
        assertThrows(TeacherNotAvailableException.class, () -> lessonService.bookLesson(bookLessonDto.getTeacherId(), student.getId(), bookLessonDto.getSubject(), bookLessonDto.getDate()));
    }

    @Test
    void shouldRemoveLesson() {
        StudentDto studentDto = StudentDto.builder()
                .id(1L)
                .name("Mike")
                .lastName("Fox")
                .email("mf@gmail.com")
                .password("qwww")
                .dateBirth(LocalDate.of(1999, 2, 2))
                .myClasses(List.of(studentClasses))
                .build();

        when(studentService.getStudentDtoById(student.getId())).thenReturn(studentDto);
        when(studentClassesRepository.findAll()).thenReturn(List.of(studentClasses));
        when(teacherService.getIdByEamil(teacher.getEmail())).thenReturn(teacher.getId());
        when(teacherClassesRepository.findAll()).thenReturn(List.of(teacherClasses));

        lessonService.removeLesson(studentClasses.getDate(), student.getId());

        verify(studentClassesRepository).deleteById(studentClasses.getId());
        verify(teacherClassesRepository).save(any(TeacherClassesEntity.class));
    }
    @Test
    void shouldNotRemoveLesson(){
        StudentDto studentDto = StudentDto.builder()
                .id(1L)
                .name("Mike")
                .lastName("Fox")
                .email("mf@gmail.com")
                .password("qwww")
                .dateBirth(LocalDate.of(1999, 2, 2))
                .myClasses(List.of())
                .build();

        when(studentService.getStudentDtoById(student.getId())).thenReturn(studentDto);
        assertThrows(BadParamException.class,() -> lessonService.removeLesson(studentClasses.getDate(), student.getId()));
    }
    @Test
    void shouldNotRemoveLesson_v2(){

        StudentDto studentDto = StudentDto.builder()
                .id(1L)
                .name("Mike")
                .lastName("Fox")
                .email("mf@gmail.com")
                .password("qwww")
                .dateBirth(LocalDate.of(1999, 2, 2))
                .myClasses(List.of(studentClasses))
                .build();

        when(studentService.getStudentDtoById(student.getId())).thenReturn(studentDto);

        assertThrows(BadParamException.class,() -> lessonService.removeLesson(LocalDateTime.now(), student.getId()));
    }

    @Test
    void shouldChangeLessonDate() {
        Map<String, LocalDateTime> dates = new HashMap<>();
        LocalDateTime dateFromWithMili = LocalDateTime.now().plusDays(2);
        LocalDateTime dateToWithMili = LocalDateTime.now().plusDays(3);
        LocalDateTime dateFrom = dateFromWithMili.withSecond(0).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime dateTo = dateToWithMili.withSecond(0).truncatedTo(ChronoUnit.SECONDS);

        TeacherClassesEntity tceTo = TeacherClassesEntity.builder()
                .id(1L)
                .date(dateToWithMili.withSecond(0).truncatedTo(ChronoUnit.SECONDS))
                .occupied(false)
                .subject(null)
                .studentId(null)
                .teacher(teacher)
                .build();

        dates.put("from", dateFrom);
        dates.put("to", dateTo);

        when(studentRepository.existsById(student.getId())).thenReturn(true);
        when(teacherClassesRepository.findAll()).thenReturn(List.of(teacherClasses, tceTo));
        when(studentClassesRepository.getStudentClassesEntityByStudentIdAndDate(student.getId(), dateFrom)).thenReturn(studentClasses);

        lessonService.changeLessonDate(student.getId(), dates);

        verify(teacherClassesRepository, times(2)).save(any(TeacherClassesEntity.class));
        verify(studentClassesRepository).save(any(StudentClassesEntity.class));
    }

    @Test
    void shouldNotChangeLessonDate_v1() {
        Map<String, LocalDateTime> dates = new HashMap<>();
        LocalDateTime dateFromWithMili = LocalDateTime.now().plusDays(2);
        LocalDateTime dateToWithMili = LocalDateTime.now().plusDays(3);
        LocalDateTime dateFrom = dateFromWithMili.withSecond(0).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime dateTo = dateToWithMili.withSecond(0).truncatedTo(ChronoUnit.SECONDS);

        dates.put("from", dateFrom);
        dates.put("to", dateTo);
        when(studentRepository.existsById(student.getId())).thenReturn(false);
        assertThrows(StudentNotFoundExceptions.class, () -> lessonService.changeLessonDate(student.getId(), dates));
    }

    @Test
    void shouldNotChangeLessonDate_v2() {
        Map<String, LocalDateTime> dates = new HashMap<>();
        LocalDateTime dateFromWithMili = LocalDateTime.now();
        LocalDateTime dateToWithMili = LocalDateTime.now().plusDays(3);
        LocalDateTime dateFrom = dateFromWithMili.withSecond(0).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime dateTo = dateToWithMili.withSecond(0).truncatedTo(ChronoUnit.SECONDS);

        dates.put("from", dateFrom);
        dates.put("to", dateTo);

        assertThrows(BadParamException.class, () -> lessonService.changeLessonDate(student.getId(), dates));
    }
    @Test
    void shouldNotChangeLessonDate_v3() {
        Map<String, LocalDateTime> dates = new HashMap<>();
        LocalDateTime dateFromWithMili = LocalDateTime.now().plusDays(2);
        LocalDateTime dateToWithMili = LocalDateTime.now().plusDays(3);
        LocalDateTime dateFrom = dateFromWithMili.withSecond(0).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime dateTo = dateToWithMili.withSecond(0).truncatedTo(ChronoUnit.SECONDS);

        dates.put("from", dateFrom);
        dates.put("to", dateTo);
        when(studentRepository.existsById(student.getId())).thenReturn(true);
        when(teacherClassesRepository.findAll()).thenReturn(List.of());
        assertThrows(BadParamException.class, () -> lessonService.changeLessonDate(student.getId(), dates));
    }
    @Test
    void shouldNotChangeLessonDate_v4() {
        Map<String, LocalDateTime> dates = new HashMap<>();
        LocalDateTime dateFromWithMili = LocalDateTime.now().plusDays(2);
        LocalDateTime dateToWithMili = LocalDateTime.now().plusDays(3);
        LocalDateTime dateFrom = dateFromWithMili.withSecond(0).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime dateTo = dateToWithMili.withSecond(0).truncatedTo(ChronoUnit.SECONDS);

        dates.put("from", dateFrom);
        dates.put("to", dateTo);
        when(studentRepository.existsById(student.getId())).thenReturn(true);
        when(teacherClassesRepository.findAll()).thenReturn(List.of(teacherClasses));
        assertThrows(TeacherNotAvailableException.class, () -> lessonService.changeLessonDate(student.getId(), dates));
    }
}