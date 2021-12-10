package com.metadata.assessment.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "student_course")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StudentCourse {
    @EmbeddedId
    private StudentCourseKey id;
}
