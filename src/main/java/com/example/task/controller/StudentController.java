package com.example.task.controller;


import com.example.task.model.Student;
import com.example.task.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/student")
public class StudentController {

    @Autowired
    private StudentService student_service;

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(StudentController.class);


    public StudentController(StudentService student_service) {
        this.student_service = student_service;
    }

    @GetMapping
    public List<Student> getStudents() {
        return student_service.getStudents();
    }

    @GetMapping(path = "/{studentId}")
    public Student getStudent(@PathVariable("studentId") Long studentId) {
        return student_service.getStudent(studentId);
    }

    @PostMapping(path = "/create")
    public void registerNewStudent(@RequestBody Student student) {

        student_service.addNewStudent(student);
        LOGGER.info("New Student added!");
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId) {

        student_service.deleteStudent(studentId);
        LOGGER.info("Student with id {} deleted!", studentId);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(@PathVariable("studentId") Long studentId, @RequestParam(required = false) String name, @RequestParam(required = false) String email) {

        student_service.updateStudent(studentId, name, email);
        LOGGER.info("Student details with id {} updated!", studentId);
    }

}
