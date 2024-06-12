package com.example.demo.models.mappings;

import com.example.demo.models.DTOs.StudentDto;
import com.example.demo.models.StudentEntity;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

@NoArgsConstructor
public class StudentDtoToStudentEntity implements Converter<StudentDto, StudentEntity> {
    @Override
    public StudentEntity convert(MappingContext<StudentDto, StudentEntity> mappingContext) {
        StudentDto studentDto = mappingContext.getSource();
        return StudentEntity.builder()
                .id(studentDto.getId())
                .name(studentDto.getName())
                .lastName(studentDto.getLastName())
                .email(studentDto.getEmail())
                .dateBirth(studentDto.getDateBirth())
                .password(studentDto.getPassword())
                .myClasses(studentDto.getMyClasses())
                .build();
    }
}
