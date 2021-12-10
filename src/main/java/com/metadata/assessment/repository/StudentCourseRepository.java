package com.metadata.assessment.repository;

import com.metadata.assessment.model.StudentCourse;
import com.metadata.assessment.model.StudentCourseKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCourseRepository extends CrudRepository<StudentCourse, StudentCourseKey> {

}
