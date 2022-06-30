package com.example.task.repo;

import com.example.task.controller.StudentController;
import com.example.task.model.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;


    @Test
    void findStudentByEmail() {

        Boolean expectedResult = true;

        Optional<Student> actualResult = studentRepository.findStudentByEmail("bean@gmail.com");
        assertEquals(expectedResult, actualResult.isPresent());

    }


    @Test
    void findNoStudentByEmail() {

        Boolean expectedResult = false;

        Optional<Student> actualResult = studentRepository.findStudentByEmail("ram@gmail.com");
        Assertions.assertEquals(expectedResult, actualResult.isPresent());
    }
}