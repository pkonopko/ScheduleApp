package com.example.demo.models;

import com.example.demo.models.types.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Set;

@Entity(name = "teacher")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherEntity extends User {
    @CollectionTable(name = "teacher_subjects")
    @Column(name = "subject")
    @ElementCollection(targetClass = Subject.class)
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Subject> subjects;
    @OneToMany(mappedBy = "teacher")
    @Fetch(FetchMode.SUBSELECT)
    private List<TeacherClassesEntity> timetable;

}
