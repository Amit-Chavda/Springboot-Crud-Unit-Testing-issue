package com.example.task.controller;

import com.example.task.model.Student;
import com.example.task.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testAddNewStudent() throws Exception {
        Student student = new Student(2L, "Amit", LocalDate.parse("1999-12-01"), "amithere@gmail.com");
        student.setAge(22);

        when(studentService.addNewStudent(any())).thenReturn(student);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/student/create")
                .accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(student))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testGetStudent() throws Exception {
        Student student = new Student(2L, "Amit", LocalDate.parse("1999-12-01"), "amithere@gmail.com");
        student.setAge(22);
        when(studentService.getStudent(any())).thenReturn(student);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/student/" + student.getId()).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(student)));
    }

    @Test
    public void testGetStudents() throws Exception {

        Student student1 = new Student(2L, "Amit", LocalDate.parse("1999-12-01"), "amithere@gmail.com");
        student1.setAge(22);

        Student student2 = new Student(3L, "Namit", LocalDate.parse("2005-11-10"), "namithere@gmail.com");
        student2.setAge(17);

        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);

        Mockito.when(studentService.getStudents()).thenReturn(studentList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/student/").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(studentList)));

    }

    @Test
    public void testDeleteStudent() throws Exception {
        Student student = new Student(2L, "Amit", LocalDate.parse("1999-12-01"), "amithere@gmail.com");
        student.setAge(22);

        doNothing().when(studentService).deleteStudent(student.getId());
        mockMvc.perform(delete("/student/" + student.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student student = new Student(2L, "Amit", LocalDate.parse("1999-12-01"), "amithere@gmail.com");
        student.setAge(22);
        doNothing().when(studentService).updateStudent(student.getId(), student.getName(), student.getEmail());
        mockMvc.perform(put("/student/" + student.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
