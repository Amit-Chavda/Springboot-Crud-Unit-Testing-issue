package com.example.task.controller;


import com.example.task.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {StudentController.class})
@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private StudentController studentController;

    ObjectMapper objectMapper =new ObjectMapper();
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .build();
    }

//
//    @Test
//    @Disabled
//    public void testGetStudents() {
//        mockMvc.perform(MockMvcRequestBuilders.get())
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.content().)
//    }

    @Test
    public void testAddNewStudent() throws  Exception {
        Student student = new Student("Amit", LocalDate.parse("1999-12-01"),"amithere@gmail.com");

        String JsonRequest = objectMapper.writeValueAsString(student);

        MvcResult result = mockMvc.perform(post("/student/create").content(JsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String resultContext = result.getResponse().getContentAsString();

        System.out.println(resultContext);
    }
}

