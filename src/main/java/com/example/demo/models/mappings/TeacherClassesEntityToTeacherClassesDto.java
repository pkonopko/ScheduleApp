package com.example.demo.models.mappings;

import com.example.demo.models.DTOs.TeacherClassesDto;
import com.example.demo.models.TeacherClassesEntity;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;


public class TeacherClassesEntityToTeacherClassesDto implements Converter<TeacherClassesEntity, TeacherClassesDto> {
    @Override
    public TeacherClassesDto convert(MappingContext<TeacherClassesEntity, TeacherClassesDto> mappingContext) {
        TeacherClassesEntity teacherClassesEntity = mappingContext.getSource();
        return TeacherClassesDto.builder()
                .teacherId(teacherClassesEntity.getId())
                .date(teacherClassesEntity.getDate())
                .occupied(teacherClassesEntity.isOccupied())
                .subject(teacherClassesEntity.getSubject())
                .id(teacherClassesEntity.getId())
                .studentId(teacherClassesEntity.getStudentId())
                .build();
    }
}