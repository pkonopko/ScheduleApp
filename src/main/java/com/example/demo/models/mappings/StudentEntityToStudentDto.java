package com.example.demo.models.mappings;

import com.example.demo.models.DTOs.StudentDto;
import com.example.demo.models.StudentEntity;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
@NoArgsConstructor
public class StudentEntityToStudentDto implements Converter <StudentEntity, StudentDto> {

    @Override
    public StudentDto convert(MappingContext<StudentEntity, StudentDto> mappingContext) {
        StudentEntity studentEntity = mappingContext.getSource();
        return StudentDto.builder()
                .id(studentEntity.getId())
                .name(studentEntity.getName())
                .lastName(studentEntity.getLastName())
                .email(studentEntity.getEmail())
                .dateBirth(studentEntity.getDateBirth())
                .password(studentEntity.getPassword())
                .myClasses(studentEntity.getMyClasses())
                .build();
    }
}
