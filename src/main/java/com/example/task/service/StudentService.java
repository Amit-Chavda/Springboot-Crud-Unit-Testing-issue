package com.example.task.service;

import com.example.task.model.Student;
import com.example.task.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private  StudentRepository student_repository;

    public StudentService(StudentRepository student_repository) {
        this.student_repository = student_repository;
    }

    public List<Student> getStudents() {
        return student_repository.findAll();
    }

    public Student getStudent(Long studentId) {
        return student_repository.findById(studentId).get();
    }

    public Student addNewStudent(Student student) {
        Optional<Student> studentOptional = student_repository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken already");
        }
        return student_repository.save(student);
    }

    public void deleteStudent(Long studentId) {
        student_repository.findById(studentId);
        if (!student_repository.existsById(studentId)) {
            throw new IllegalStateException("student with id " + studentId + " doesn't exist!");
        }
        student_repository.deleteById(studentId);

    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = student_repository.findById(studentId).orElseThrow(() -> new IllegalStateException("student with id " + studentId + " doesn't exist!"));
        if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }
        if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)) {
            Optional<Student> studentOptional = student_repository.findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("email taken already");
            }
            student.setEmail(email);
        }
    }
}
