package com.metadata.assessment.controller;

import com.metadata.assessment.model.Course;
import com.metadata.assessment.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/all")
    public List<Course> all() {
        return (List<Course>) courseRepository.findAll();
    }

    @GetMapping("/get/{courseId}")
    public Course get(@PathVariable Long courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Course course) {
        if (course == null) {
            return ResponseEntity.badRequest().body("Course data should not be null");
        }
        if (course.getId() != null && courseRepository.existsById(course.getId())) {
            return ResponseEntity.badRequest().body("You are trying to create a course with the ID that already exists, you should use the update method");
        }
        return ResponseEntity.ok(courseRepository.save(course));
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Course course) {
        if (course == null) {
            return ResponseEntity.badRequest().body("Course data should not be null");
        }
        if (course.getId() == null) {
            return ResponseEntity.badRequest().body("You are trying to update a Course without the ID, you should use the create method");
        }
        if (!courseRepository.existsById(course.getId())) {
            return ResponseEntity.badRequest().body("You are trying to update a Course with an unknown ID, you should use the create method");
        }
        return ResponseEntity.ok(courseRepository.save(course));
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity delete(@PathVariable Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            return ResponseEntity.ok("The Course ID is unknown");
        }
        courseRepository.deleteById(courseId);
        return ResponseEntity.ok("Deleted successfully");
    }
}
