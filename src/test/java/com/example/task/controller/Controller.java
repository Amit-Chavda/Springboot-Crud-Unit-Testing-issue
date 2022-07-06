package com.example.task.controller;

import com.example.task.model.Student;
import com.example.task.service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value=StudentController.class,useDefaultFilters = false)
@AutoConfigureMockMvc(addFilters = false)
public class Controller {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private  String mapToJson(Object object) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper.writeValueAsString(object);
    }

    @Test
    public void testAddNewStudent() throws  Exception{
        Student student=new Student("Amit", LocalDate.parse("1999-12-01"),"amithere@gmail.com");

        String inputJson = this.mapToJson(student);

        Mockito.when(studentService.addNewStudent(Mockito.any(Student.class))).thenReturn(student);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/student/create")
                .accept(MediaType.APPLICATION_JSON).content(inputJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult=mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        String outputJson = response.getContentAsString();
        assertThat(outputJson).isEqualTo(inputJson);
        assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    public  void  testGetStudent() throws Exception{
     Student student = new Student(2L,"Amit", LocalDate.parse("1999-12-01"),"amithere@gmail.com");

     Mockito.when(studentService.getStudent(Mockito.anyLong())).thenReturn(student);

     RequestBuilder requestBuilder =MockMvcRequestBuilders.get("/student/2").accept(MediaType.APPLICATION_JSON);

     MvcResult mvcResult=mockMvc.perform(requestBuilder).andReturn();

     String expectedJson = this.mapToJson(student);
     String outputJson = mvcResult.getResponse().getContentAsString();
     assertThat(outputJson).isEqualTo(expectedJson);

    }
}
