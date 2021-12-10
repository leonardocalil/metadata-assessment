package com.metadata.assessment.controller;

import com.metadata.assessment.model.Course;
import com.metadata.assessment.model.Student;
import com.metadata.assessment.model.StudentCourse;
import com.metadata.assessment.model.StudentCourseKey;
import com.metadata.assessment.repository.CourseRepository;
import com.metadata.assessment.repository.StudentCourseRepository;
import com.metadata.assessment.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/school")
public class SchoolController {


    StudentRepository studentRepository;
    CourseRepository courseRepository;
    StudentCourseRepository studentCourseRepository;

    @Autowired
    public SchoolController(StudentRepository studentRepository,
                            CourseRepository courseRepository,
                            StudentCourseRepository studentCourseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.studentCourseRepository = studentCourseRepository;
    }

    @GetMapping("/assign-student-course/{studentId}/{courseId}")
    public ResponseEntity assign(@PathVariable("studentId") Long studentId,
                                 @PathVariable("courseId") Long courseId) {

        try {
            executeAssignValidations(studentId, courseId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        StudentCourseKey key = new StudentCourseKey(studentId, courseId);
        if (studentCourseRepository.existsById(key)) {
            return ResponseEntity.ok("Student is already assigned to this Course");
        }
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setId(key);
        studentCourseRepository.save(studentCourse);
        return ResponseEntity.ok("Student assigned successfully");
    }

    @GetMapping("/remove-student-course/{studentId}/{courseId}")
    public ResponseEntity remove(@PathVariable("studentId") Long studentId,
                                 @PathVariable("courseId") Long courseId) {
        try {
            genericValidations(studentId, courseId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        StudentCourseKey key = new StudentCourseKey(studentId, courseId);
        if (!studentCourseRepository.existsById(key)) {
            return ResponseEntity.ok("Student is already unassigned to this Course");
        }
        studentCourseRepository.deleteById(key);

        return ResponseEntity.ok("Student is already unassigned successfully to Course");
    }

    @GetMapping("/student-by-course/{courseId}")
    public List<Student> getByCourse(@PathVariable("courseId") Long courseId) {

        return studentRepository.getByCourse(courseId);
    }

    @GetMapping("/course-by-student/{studentId}")
    public List<Course> getByStudent(@PathVariable("studentId") Long studentId) {

        return courseRepository.getByStudent(studentId);
    }

    @GetMapping("/student-no-course")
    public List<Student> getNoCourse() {
        return studentRepository.getNoCourse();
    }

    @GetMapping("/course-no-student")
    public List<Course> getNoStudent() {
        return courseRepository.getNoStudent();
    }

    private void executeAssignValidations(Long studentId, Long courseId) throws Exception {

        Student student = genericValidations(studentId, courseId);
        if (!CollectionUtils.isEmpty(student.getCourses())) {
            if (!student.getCourses().stream().anyMatch(c -> c.getId().equals(courseId))
                    && student.getCourses().size() > 4) {
                throw new Exception("Student is already assigned to 5 courses");
            }
        }
    }

    private Student genericValidations(Long studentId, Long courseId) throws Exception {
        if (studentId == null) {
            throw new Exception("StudentId should not be null");
        }
        if (courseId == null) {
            throw new Exception("CourseId should not be null");
        }
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (!optionalStudent.isPresent()) {
            throw new Exception("Student doesn't exist");
        }
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (!optionalCourse.isPresent()) {
            throw new Exception("Course doesn't exist");
        }
        return optionalStudent.get();
    }

}
