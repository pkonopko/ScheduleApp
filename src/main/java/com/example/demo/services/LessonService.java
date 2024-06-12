package com.example.demo.services;

import com.example.demo.exceptions.*;
import com.example.demo.models.DTOs.StudentClassesDto;
import com.example.demo.models.DTOs.StudentDto;
import com.example.demo.models.DTOs.TeacherClassesDto;
import com.example.demo.models.DTOs.TeacherDto;
import com.example.demo.models.StudentClassesEntity;
import com.example.demo.models.StudentEntity;
import com.example.demo.models.TeacherClassesEntity;
import com.example.demo.models.TeacherEntity;
import com.example.demo.models.types.Subject;
import com.example.demo.repositories.StudentClassesRepository;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.repositories.TeacherClassesRepository;
import com.example.demo.repositories.TeacherRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository; //wstrzykiwanie zaleznosci
    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final TeacherClassesRepository teacherClassesRepository;
    private final StudentClassesRepository studentClassesRepository;

    public List<StudentClassesDto> getStudentLessons(Long id) throws StudentNotFoundExceptions {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundExceptions();
        }
        return studentClassesRepository.findAll()
                .stream()
                .filter(c -> c.getStudent().getId() == id)
                .map(c -> modelMapper.map(c, StudentClassesDto.class))
                .toList();
    }

    public void bookLesson(Long teacherId, Long studentId, Subject subject, LocalDateTime date) throws ApplicationExceptions {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundExceptions();
        }
        if (!teacherRepository.existsById(teacherId)) {
            throw new TeacherNotFoundExceptions();
        }
        if (getStudentLessons(studentId).stream()
                .anyMatch(c -> c.getDate().isEqual(date))) {
            throw new StudentNotAvailableExceptions();
        }
        TeacherDto teacher = teacherService.getTeacherDtoById(teacherId);

        if (!teacher.getSubjects().contains(subject)) {
            throw new BadParamException();
        }
        List<TeacherClassesEntity> teacherClassesEntity = teacherClassesRepository.findAll()
                .stream()
                .filter(c -> c.getTeacher().getId() == teacherId)
                .toList();
        boolean isTeacherFree = teacherClassesEntity.stream()
                .filter(c -> c.getDate().isEqual(date))
                .anyMatch(c -> !c.isOccupied());
        if (teacherClassesEntity.isEmpty() || !isTeacherFree) {
            throw new TeacherNotAvailableException();
        }
        TeacherClassesEntity teacherClasses = teacherClassesEntity.stream()
                .filter(c -> c.getDate().isEqual(date))
                .findFirst()
                .get();

        teacherClasses.setOccupied(true);
        teacherClasses.setSubject(subject);
        teacherClasses.setStudentId(studentId);
        teacherClassesRepository.save(teacherClasses);

        StudentDto studentDto = studentService.getStudentDtoById(studentId);

        StudentClassesEntity studentClassesEntity = StudentClassesEntity.builder()
                .date(date)
                .subject(subject)
                .teacherEmail(teacher.getEmail())
                .teacherName(teacher.getName())
                .teacherLastName(teacher.getLastName())
                .student(modelMapper.map(studentDto, StudentEntity.class))
                .build();
        studentClassesRepository.save(studentClassesEntity);
    }

    public void removeLesson(LocalDateTime date, Long studentId) throws ApplicationExceptions {
        if (!doesStudentLessonExist(studentId, date)) {
            throw new BadParamException();
        }
        if (!LocalDateTime.now().plusHours(6).isBefore(date)) {
            throw new TimeException();
        }

        StudentClassesEntity studentClasses = studentClassesRepository.findAll()
                .stream()
                .filter(c -> c.getStudent().getId() == studentId)
                .filter(c -> c.getDate().equals(date))
                .findAny()
                .get();
        Long teacherId = teacherService.getIdByEamil(studentClasses.getTeacherEmail());
        studentClassesRepository.deleteById(studentClasses.getId());

        TeacherClassesEntity teacherClass = teacherClassesRepository.findAll()
                .stream()
                .filter(c -> c.getTeacher().getId() == teacherId)
                .filter(c -> c.getDate().equals(date))
                .findAny()
                .get();
        teacherClass.setOccupied(false);
        teacherClass.setSubject(null);
        teacherClass.setStudentId(null);
        teacherClassesRepository.save(teacherClass);
    }

    public boolean doesStudentLessonExist(Long id, LocalDateTime date) {
        StudentDto student = studentService.getStudentDtoById(id);
        List<StudentClassesEntity> classes = student.getMyClasses().stream()
                .filter(studentClassesEntity -> date.isEqual(studentClassesEntity.getDate()))
                .toList();
        return classes.size() == 1;
    }

    public List<TeacherClassesDto> getAllAvailableLessonInDay(LocalDate date, Subject subject) {
        return teacherClassesRepository.findAll()
                .stream()
                .filter(c -> c.getDate().toLocalDate().equals(date))
                .filter(c -> !c.isOccupied())
                .filter(c -> c.getTeacher().getSubjects().contains(subject))
                .map(c -> modelMapper.map(c, TeacherClassesDto.class))
                .toList();
    }

    public void changeLessonDate(Long studentId, Map<String, LocalDateTime> dates) throws ApplicationExceptions {
        LocalDateTime dateFrom = dates.get("from");
        LocalDateTime dateTo = dates.get("to");
        if (dateFrom.minusHours(6).isBefore(LocalDateTime.now()) || ChronoUnit.HOURS.between(dateFrom, dateTo) < 6) {
            throw new BadParamException();
        }
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundExceptions();
        }
        Optional<TeacherClassesEntity> tceOptional = teacherClassesRepository.findAll()
                .stream()
                .filter(c -> c.getStudentId() == studentId)
                .filter(c -> c.getDate().equals(dateFrom))
                .findAny();
        if (tceOptional.isPresent()) {
            TeacherClassesEntity tceFrom = tceOptional.get();
            tceOptional = teacherClassesRepository.findAll()
                    .stream()
                    .filter(c -> c.getDate().equals(dateTo))
                    .filter(c -> !c.isOccupied())
                    .filter(c -> c.getTeacher().getId() == tceFrom.getTeacher().getId())
                    .findAny();
            if (tceOptional.isPresent()) {
                TeacherClassesEntity tceTo = tceOptional.get();
                tceTo.setOccupied(true);
                tceTo.setSubject(tceFrom.getSubject());
                tceTo.setStudentId(studentId);
                tceFrom.setOccupied(false);
                tceFrom.setSubject(null);
                tceFrom.setStudentId(null);
                teacherClassesRepository.save(tceFrom);
                teacherClassesRepository.save(tceTo);
                StudentClassesEntity studentClasses = studentClassesRepository.getStudentClassesEntityByStudentIdAndDate(studentId, dateFrom);
                studentClasses.setDate(dateTo);
                studentClassesRepository.save(studentClasses);
            } else {
                throw new TeacherNotAvailableException();
            }
        } else {
            throw new BadParamException();
        }
    }
}
