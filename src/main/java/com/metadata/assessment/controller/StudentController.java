package com.metadata.assessment.controller;

import com.metadata.assessment.model.Student;
import com.metadata.assessment.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/all")
    public List<Student> all() {
        return (List<Student>) studentRepository.findAll();
    }

    @GetMapping("/get/{studentId}")
    public Student get(@PathVariable Long studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Student student) {
        if (student == null) {
            return ResponseEntity.badRequest().body("Student data should not be null");
        }
        if (student.getId() != null && studentRepository.existsById(student.getId())) {
            return ResponseEntity.badRequest().body("You are trying to create a student with the ID that already exists, you should use the update method");
        }
        return ResponseEntity.ok(studentRepository.save(student));
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Student student) {
        if (student == null) {
            return ResponseEntity.badRequest().body("Student data should not be null");
        }
        if (student.getId() == null) {
            return ResponseEntity.badRequest().body("You are trying to update a student without the ID, you should use the create method");
        }
        if (!studentRepository.existsById(student.getId())) {
            return ResponseEntity.badRequest().body("You are trying to update a student with an unknown ID, you should use the create method");
        }
        return ResponseEntity.ok(studentRepository.save(student));
    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity delete(@PathVariable Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            return ResponseEntity.ok("The Student ID is unknown");
        }
        studentRepository.deleteById(studentId);
        return ResponseEntity.ok("Deleted successfully");
    }
}
