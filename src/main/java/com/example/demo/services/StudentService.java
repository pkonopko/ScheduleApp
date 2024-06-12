package com.example.demo.services;

import com.example.demo.exceptions.StudentNotFoundExceptions;
import com.example.demo.models.DTOs.StudentDto;
import com.example.demo.models.StudentEntity;
import com.example.demo.models.webDTOs.StudentWebDto;
import com.example.demo.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;

    public StudentDto save(StudentDto newStudent) {
        StudentEntity studentToSave = modelMapper.map(newStudent, StudentEntity.class);
        studentRepository.save(studentToSave);
        return modelMapper.map(studentToSave, StudentDto.class);
    }

    public StudentDto updateStudent(Long id, StudentWebDto newStudentData) {
        StudentDto student = getStudentDtoById(id);
        student.setName(newStudentData.getName());
        student.setLastName(newStudentData.getLastName());
        student.setEmail(newStudentData.getEmail());
        student.setPassword(newStudentData.getPassword());
        student.setDateBirth(newStudentData.getDateBirth());
        return save(student);
    }

    public void deleteStudentById(Long id) throws StudentNotFoundExceptions {
        if (!studentRepository.existsById(id))
            throw new StudentNotFoundExceptions();
        studentRepository.deleteById(id);
    }
    public StudentDto getStudentDtoById(Long id) {
        return modelMapper.map(
                studentRepository.findById(id)
                        .orElseThrow(StudentNotFoundExceptions::new),
                StudentDto.class);
    }
}