package com.example.task.service;

import com.example.task.model.Student;
import com.example.task.repo.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {StudentService.class})
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    private StudentService underTest;

    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository);
    }


    @Test
    void canGetStudents() {
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
        when(studentRepository.findById((Long) org.mockito.Mockito.any())).thenReturn(ofResult);
        assertSame(student, studentService.getStudent(123L));
        verify(studentRepository).findById((Long) org.mockito.Mockito.any());
    }


    @Test
    void testGetStudent2() {
        when(studentRepository.findById((Long) org.mockito.Mockito.any())).thenThrow(new IllegalStateException("foo"));
        assertThrows(IllegalStateException.class, () -> studentService.getStudent(123L));
        verify(studentRepository).findById((Long) org.mockito.Mockito.any());
    }

    @Test
    void canAddNewStudent() {
        Student s = new Student("Kabir", LocalDate.parse("1999-12-01"), "kabir@gmail.com");
        studentService.addNewStudent(s);

        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(s);
    }

    @Test
    void cantAddNewStudentAsEmailTaken() {
        Student s = new Student("Kabir", LocalDate.parse("1999-12-01"), "kabir@gmail.com");

        given(studentRepository.findStudentByEmail(s.getEmail()))
                .willReturn(Optional.ofNullable(s));

        assertThatThrownBy(() -> studentService.addNewStudent(s))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email taken already");

        verify(studentRepository, never()).save(any());
    }


    @Test
    void testDeleteStudent() {
        Student student = new Student();
        student.setAge(1);
        student.setDob(LocalDate.ofEpochDay(1L));
        student.setEmail("jane.doe@example.org");
        student.setId(123L);
        student.setName("Name");
        Optional<Student> ofResult = Optional.of(student);
        doNothing().when(studentRepository).deleteById((Long) org.mockito.Mockito.any());
        when(studentRepository.existsById((Long) org.mockito.Mockito.any())).thenReturn(true);
        when(studentRepository.findById((Long) org.mockito.Mockito.any())).thenReturn(ofResult);
        studentService.deleteStudent(123L);
        verify(studentRepository).existsById((Long) org.mockito.Mockito.any());
        verify(studentRepository).findById((Long) org.mockito.Mockito.any());
        verify(studentRepository).deleteById((Long) org.mockito.Mockito.any());
        assertTrue(studentService.getStudents().isEmpty());
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student();
        student.setAge(1);
        student.setDob(LocalDate.ofEpochDay(1L));
        student.setEmail("jane.doe@example.org");
        student.setId(123L);
        student.setName("Name");
        Optional<Student> ofResult = Optional.of(student);
        when(studentRepository.findById((Long) org.mockito.Mockito.any())).thenReturn(ofResult);
        studentService.updateStudent(123L, "Name", "jane.doe@example.org");
        verify(studentRepository).findById((Long) org.mockito.Mockito.any());
        assertTrue(studentService.getStudents().isEmpty());
    }

    @Test
    void testUpdateStudent2() {
        Student student = mock(Student.class);
        when(student.getEmail()).thenReturn("foo");
        when(student.getName()).thenReturn("Name");
        doNothing().when(student).setAge((Integer) org.mockito.Mockito.any());
        doNothing().when(student).setDob((LocalDate) org.mockito.Mockito.any());
        doNothing().when(student).setEmail((String) org.mockito.Mockito.any());
        doNothing().when(student).setId((Long) org.mockito.Mockito.any());
        doNothing().when(student).setName((String) org.mockito.Mockito.any());
        student.setAge(1);
        student.setDob(LocalDate.ofEpochDay(1L));
        student.setEmail("jane.doe@example.org");
        student.setId(123L);
        student.setName("Name");
        Optional<Student> ofResult = Optional.of(student);

        Student student1 = new Student();
        student1.setAge(1);
        student1.setDob(LocalDate.ofEpochDay(1L));
        student1.setEmail("jane.doe@example.org");
        student1.setId(123L);
        student1.setName("Name");
        Optional<Student> ofResult1 = Optional.of(student1);
        when(studentRepository.findStudentByEmail((String) org.mockito.Mockito.any())).thenReturn(ofResult1);
        when(studentRepository.findById((Long) org.mockito.Mockito.any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> studentService.updateStudent(123L, "Name", "jane.doe@example.org"));
        verify(studentRepository).findStudentByEmail((String) org.mockito.Mockito.any());
        verify(studentRepository).findById((Long) org.mockito.Mockito.any());
        verify(student).getEmail();
        verify(student).getName();
        verify(student).setAge((Integer) org.mockito.Mockito.any());
        verify(student).setDob((LocalDate) org.mockito.Mockito.any());
        verify(student).setEmail((String) org.mockito.Mockito.any());
        verify(student).setId((Long) org.mockito.Mockito.any());
        verify(student).setName((String) org.mockito.Mockito.any());
    }

}