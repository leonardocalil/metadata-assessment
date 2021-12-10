package com.metadata.assessment.controller;

import com.google.gson.Gson;
import com.metadata.assessment.model.Course;
import com.metadata.assessment.model.Student;
import com.metadata.assessment.model.StudentCourse;
import com.metadata.assessment.model.StudentCourseKey;
import com.metadata.assessment.repository.CourseRepository;
import com.metadata.assessment.repository.StudentCourseRepository;
import com.metadata.assessment.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class SchoolControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentCourseRepository scRepository;


    private final Gson gson = new Gson();

    @Test
    @Order(1)
    public void givenStudentCourse_whenAssignStudentsToCourse_thenStatus200()
            throws Exception {
        createTestStudent("Leo");
        createTestCourse("English");

        List<Student> students = getStudents();
        List<Course> courses = getCourses();

        Long studentId = students.get(0).getId();
        Long courseId = courses.get(0).getId();

        MvcResult result = mvc.perform(get("/school/assign-student-course/" + studentId + "/" + courseId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE)).andReturn();

        Assertions.assertTrue(checkExists(studentId, courseId));

    }

    @Test
    @Order(2)
    public void givenStudentAssignedCourse_whenRemove_thenStatus200()
            throws Exception {

        StudentCourse studentCourse = getStudentCourses().get(0);
        Long studentId = studentCourse.getId().getStudentId();
        Long courseId = studentCourse.getId().getCourseId();

        MvcResult result = mvc.perform(get("/school/remove-student-course/" + studentId + "/" + courseId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE)).andReturn();
        Assertions.assertFalse(checkExists(studentId, courseId));

    }


    private void createTestCourse(String name) {
        courseRepository.save(Course.builder().name(name).build());
    }

    private void createTestStudent(String name) {
        studentRepository.save(Student.builder().name(name).build());
    }

    private List<Student> getStudents() {
        return (List<Student>) studentRepository.findAll();
    }

    private List<Course> getCourses() {
        return (List<Course>) courseRepository.findAll();
    }

    private List<StudentCourse> getStudentCourses() {
        return (List<StudentCourse>) scRepository.findAll();
    }

    private boolean checkExists(Long studentId, Long courseId) {
        StudentCourseKey key = new StudentCourseKey(studentId, courseId);
        return scRepository.existsById(key);
    }
}
