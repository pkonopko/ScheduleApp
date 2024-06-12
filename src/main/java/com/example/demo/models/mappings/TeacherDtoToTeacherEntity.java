package com.example.demo.models.mappings;

import com.example.demo.models.DTOs.TeacherDto;
import com.example.demo.models.TeacherEntity;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

@NoArgsConstructor
public class TeacherDtoToTeacherEntity implements Converter<TeacherDto, TeacherEntity> {
    @Override
    public TeacherEntity convert(MappingContext<TeacherDto, TeacherEntity> mappingContext) {
        TeacherDto teacherDto = mappingContext.getSource();
        return TeacherEntity.builder()
                .id(teacherDto.getId())
                .name(teacherDto.getName())
                .lastName(teacherDto.getLastName())
                .email(teacherDto.getEmail())
                .dateBirth(teacherDto.getDateBirth())
                .password(teacherDto.getPassword())
                .subjects(teacherDto.getSubjects())
                .build();
    }
}
