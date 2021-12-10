package com.metadata.assessment.repository;

import com.metadata.assessment.model.Course;
import com.metadata.assessment.model.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
    @Query(value = "Select c from Course c " +
            "inner join StudentCourse sc " +
            "on c.id = sc.id.courseId " +
            "where sc.id.studentId = :studentId ")
    List<Course> getByStudent(@Param("studentId") Long studentId);

    @Query(value = "Select c From Course c " +
            "left join StudentCourse sc " +
            "on c.id = sc.id.courseId " +
            "where sc.id.studentId is null ")
    List<Course> getNoStudent();
}
