package com.example.demo.services;

import com.example.demo.exceptions.TeacherNotFoundExceptions;
import com.example.demo.models.DTOs.TeacherClassesDto;
import com.example.demo.models.DTOs.TeacherDto;
import com.example.demo.models.TeacherClassesEntity;
import com.example.demo.models.TeacherEntity;
import com.example.demo.models.User;
import com.example.demo.models.webDTOs.TeacherWebDto;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.repositories.TeacherClassesRepository;
import com.example.demo.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class TeacherService {
    private final ModelMapper modelMapper;
    private final TeacherRepository teacherRepository;
    private final TeacherClassesRepository teacherClassesRepository;

    public TeacherDto save(TeacherDto newTeacher) {
        TeacherEntity teacherToSave = modelMapper.map(newTeacher, TeacherEntity.class);
        teacherRepository.save(teacherToSave);
        if (newTeacher.getId() == null) {
            setTimetable(teacherToSave);
        }
        return modelMapper.map(teacherToSave, TeacherDto.class);
    }

    @Scheduled(cron = "0 0 0 */7 * *")
    public void setTimetableForAllTeachers() {
        teacherRepository.findAll().forEach(this::setTimetable);
    }
    public void setTimetable(TeacherEntity teacher) {
        for (int j = 1; j <= 7; j++) {
            for (int i = 8; i <= 13; i++) {
                TeacherClassesEntity tce = TeacherClassesEntity.builder()
                        .date(LocalDateTime.of(LocalDate.now().plusDays(j), LocalTime.of(i, 0)))
                        .occupied(false)
                        .subject(null)
                        .studentId(null)
                        .teacher(teacher)
                        .build();
                teacherClassesRepository.save(tce);
            }
        }
    }

    public TeacherDto updateTeacher(Long id, TeacherWebDto newTeacherData) {
        TeacherDto teacher = getTeacherDtoById(id);
        teacher.setName(newTeacherData.getName());
        teacher.setLastName(newTeacherData.getLastName());
        teacher.setEmail(newTeacherData.getEmail());
        teacher.setPassword(newTeacherData.getPassword());
        teacher.setDateBirth(newTeacherData.getDateBirth());
        teacher.setSubjects(newTeacherData.getSubjects());
        return save(teacher);
    }

    public void deleteTeacher(Long id) throws TeacherNotFoundExceptions {
        if (!teacherRepository.existsById(id))
            throw new TeacherNotFoundExceptions();
        teacherRepository.deleteById(id);
    }

    public TeacherDto getTeacherDtoById(Long id) {
        return modelMapper.map(
                teacherRepository.findById(id)
                        .orElseThrow(TeacherNotFoundExceptions::new),
                TeacherDto.class);
    }

    public List<TeacherClassesDto> getTeacherTimeTable(Long id) {
        if (!teacherRepository.existsById(id))
            throw new TeacherNotFoundExceptions();
        TeacherEntity teacher = teacherRepository.findById(id)
                .orElseThrow(TeacherNotFoundExceptions::new);
        List<TeacherClassesEntity> teacherClasses = teacher.getTimetable();
        return teacherClasses.stream()
                .map(tce -> modelMapper.map(tce, TeacherClassesDto.class))
                .toList();
    }

    public Long getIdByEamil(String email) {
        return teacherRepository.findAll()
                .stream()
                .filter(t -> t.getEmail().equals(email))
                .mapToLong(User::getId)
                .findAny()
                .getAsLong();
    }
}