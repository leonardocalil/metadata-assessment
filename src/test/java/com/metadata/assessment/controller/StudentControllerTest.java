package com.metadata.assessment.controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.metadata.assessment.model.Student;
import com.metadata.assessment.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class StudentControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudentRepository repository;

    private final Gson gson = new Gson();

    @Test
    @Order(1)
    public void givenStudent_whenGetAllStudents_thenStatus200()
            throws Exception {
        createTestStudent("Leo");
        createTestStudent("Joao");
        createTestStudent("Arthur");

        int total = getStudents().size();

        MvcResult result = mvc.perform(get("/student/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();

        List students = gson.fromJson(result.getResponse().getContentAsString(),List.class);
        Assertions.assertEquals(total, students.size());

    }

    @Test
    @Order(2)
    public void givenStudent_whenGetStudents_thenStatus200()
            throws Exception {

        List<Student> list = (List<Student>) repository.findAll();
        Student student = list.get(0);

        MvcResult result = mvc.perform(get("/student/get/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        Assertions.assertEquals(student.getName(), gson.fromJson(result.getResponse().getContentAsString(), Student.class).getName());

    }

    @Test
    @Order(3)
    public void createNewStudent_thenStatus200()
            throws Exception {

        mvc.perform(post("/student/create")
                .content(new Gson().toJson(Student.builder().name("Fran").email("fran@email.com").build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\":\"Fran\",\"email\":\"fran@email.com\"}"));
    }

    @Test
    @Order(4)
    public void updateStudent_thenStatus200()
            throws Exception {

        List<Student> list = (List<Student>) repository.findAll();
        Student student = list.get(0);
        String oldName = student.getName();
        student.setName("NewName");
        student.setCourses(null);


        MvcResult result = mvc.perform(put("/student/update")
                .content(new Gson().toJson(student))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        Assertions.assertEquals("NewName", gson.fromJson(result.getResponse().getContentAsString(), Student.class).getName());
    }

    @Test
    @Order(5)
    public void deleteStudent_thenStatus200()
            throws Exception {


        List<Student> list = (List<Student>) repository.findAll();
        Student student = list.get(0);

        mvc.perform(delete("/student/delete/"+student.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        MvcResult result = mvc.perform(get("/student/get/"+student.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Assertions.assertEquals("", result.getResponse().getContentAsString());

    }

    private List<Student> getStudents() {
        return (List<Student>) repository.findAll();
    }

    private void createTestStudent(String name) {
        repository.save(Student.builder().name(name).build());
    }
}
