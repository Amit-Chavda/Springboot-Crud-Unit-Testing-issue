package com.example.task.service;

import com.example.task.model.Student;
import com.example.task.repo.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {StudentService.class})
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository);
    }


    @Test
    void testGetStudents() {
        studentService.getStudents();
        verify(studentRepository).findAll();
    }


    @Test
    void testGetStudent() {
        Student student = new Student();
        student.setAge(1);
        student.setDob(LocalDate.ofEpochDay(1L));
        student.setEmail("jane.doe@example.org");
        student.setId(123L);
        student.setName("Name");
        Optional<Student> ofResult = Optional.of(student);
        when(studentRepository.findById(org.mockito.Mockito.any())).thenReturn(ofResult);
        assertSame(student, studentService.getStudent(123L));
        verify(studentRepository).findById(org.mockito.Mockito.any());
    }


    @Test
    void canAddNewStudent() {
        Student student = new Student("Kabir", LocalDate.parse("1999-12-01"), "kabir@gmail.com");
        studentService.addNewStudent(student);

        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void cantAddNewStudentAsEmailTaken() {
        Student student = new Student("Kabir", LocalDate.parse("1999-12-01"), "kabir@gmail.com");

        given(studentRepository.findStudentByEmail(student.getEmail()))
                .willReturn(Optional.ofNullable(student));

        assertThatThrownBy(() -> studentService.addNewStudent(student))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email taken already");

        verify(studentRepository, never()).save(any());
    }


    @Test
    void testDeleteStudent() {
        Student student = new Student(2L, "Amit", LocalDate.parse("1999-12-01"), "amithere@gmail.com");
        student.setAge(22);

        Mockito.when(studentRepository.findById(2L)).thenReturn(Optional.of(student));
        Mockito.when(studentRepository.existsById(student.getId())).thenReturn(false);
        assertFalse(studentRepository.existsById(student.getId()));
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student(2L, "Amit", LocalDate.parse("1999-12-01"), "amithere@gmail.com");
        student.setAge(22);
        Mockito.when(studentRepository.findById(2L)).thenReturn(Optional.of(student));
        student.setName("Namit");
        student.setEmail("namit@gmail.com");
        Mockito.when(studentRepository.save(student)).thenReturn(student);
        assertThat(student.getEmail()).isEqualTo("namit@gmail.com");
        assertThat(student.getName()).isEqualTo("Namit");
    }

}