package com.example.demo.models.mappings;

        import com.example.demo.models.DTOs.TeacherDto;
        import com.example.demo.models.TeacherEntity;
        import lombok.NoArgsConstructor;
        import org.modelmapper.Converter;
        import org.modelmapper.spi.MappingContext;
@NoArgsConstructor
public class TeacherEntityToTeacherDto implements Converter<TeacherEntity, TeacherDto> {

    @Override
    public TeacherDto convert(MappingContext<TeacherEntity, TeacherDto> mappingContext) {
        TeacherEntity teacherEntity = mappingContext.getSource();
        return TeacherDto.builder()
                .id(teacherEntity.getId())
                .name(teacherEntity.getName())
                .lastName(teacherEntity.getLastName())
                .email(teacherEntity.getEmail())
                .dateBirth(teacherEntity.getDateBirth())
                .password(teacherEntity.getPassword())
                .subjects(teacherEntity.getSubjects())
                .build();
    }
}
