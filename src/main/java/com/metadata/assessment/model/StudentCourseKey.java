package com.metadata.assessment.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class StudentCourseKey implements Serializable {

    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "course_id")
    private Long courseId;

}
