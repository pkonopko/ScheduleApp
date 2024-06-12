package com.example.demo.models.mappings;

import com.example.demo.models.DTOs.StudentClassesDto;
import com.example.demo.models.DTOs.TeacherClassesDto;
import com.example.demo.models.StudentClassesEntity;
import com.example.demo.models.TeacherClassesEntity;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class StudentClassesEntityToStudentClassesDto implements Converter<StudentClassesEntity, StudentClassesDto> {
    @Override
    public StudentClassesDto convert(MappingContext<StudentClassesEntity, StudentClassesDto> mappingContext) {
        StudentClassesEntity studentClassesEntity = mappingContext.getSource();
        return StudentClassesDto.builder()
                .studentId(studentClassesEntity.getId())
                .date(studentClassesEntity.getDate())
                .subject(studentClassesEntity.getSubject())
                .teacherEmail(studentClassesEntity.getTeacherEmail())
                .teacherName(studentClassesEntity.getTeacherName())
                .teacherLastName(studentClassesEntity.getTeacherLastName())
                .id(studentClassesEntity.getId())
                .build();
    }
}
