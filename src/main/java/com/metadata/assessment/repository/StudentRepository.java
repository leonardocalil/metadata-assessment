package com.metadata.assessment.repository;

import com.metadata.assessment.model.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
    @Query(value = "Select s From Student s " +
            "inner join StudentCourse sc " +
            "on s.id = sc.id.studentId " +
            "where sc.id.courseId = :courseId ")
    List<Student> getByCourse(@Param("courseId") Long courseId);

    @Query(value = "Select s From Student s " +
            "left join StudentCourse sc " +
            "on s.id = sc.id.studentId " +
            "where sc.id.courseId is null ")
    List<Student> getNoCourse();
}
