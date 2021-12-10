package com.metadata.assessment.controller;

import com.google.gson.Gson;
import com.metadata.assessment.model.Course;
import com.metadata.assessment.repository.CourseRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class CourseControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CourseRepository repository;


    @Test
    @Order(1)
    public void givenCourse_whenGetAllCourses_thenStatus200()
            throws Exception {
        createTestCourse(1l, "English");
        createTestCourse(2l, "Programming");

        mvc.perform(get("/course/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":1,\"name\":\"English\"}," +
                        "{\"id\":2,\"name\":\"Programming\"}]"));
    }

    @Test
    @Order(2)
    public void givenCourse_whenGetCourse_thenStatus200()
            throws Exception {

        mvc.perform(get("/course/get/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":2,\"name\":\"Programming\"}"));
    }

    @Test
    @Order(3)
    public void createNewCourse_thenStatus200()
            throws Exception {

        mvc.perform(post("/course/create")
                .content(new Gson().toJson(Course.builder().name("Frances").build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\":\"Frances\"}"));
    }

    @Test
    @Order(4)
    public void updateCourse_thenStatus200()
            throws Exception {

        mvc.perform(put("/course/update")
                .content(new Gson().toJson(Course.builder().id(3l).name("French").build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":3,\"name\":\"French\"}"));
    }

    @Test
    @Order(5)
    public void deleteCourse_thenStatus200()
            throws Exception {

        mvc.perform(delete("/course/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        MvcResult result = mvc.perform(get("/course/get/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Assertions.assertEquals("", result.getResponse().getContentAsString());

    }

    private void createTestCourse(Long id, String name) {
        repository.save(Course.builder().id(id).name(name).build());
    }
}
