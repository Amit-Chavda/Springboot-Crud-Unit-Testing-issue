package com.example.task.service;

import com.example.task.model.Student;
import com.example.task.repo.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        underTest = new StudentService(studentRepository);
    }


    @Test
    void canGetStudents() {
        underTest.getStudents();

        verify(studentRepository).findAll();
    }

    @Test
    @Disabled
    void canGetStudent() {

        Boolean s = studentRepository.findById(19L).isPresent();
        assertThat(s).isSameAs(true);
    }

    @Test
    void canAddNewStudent() {
        Student s = new Student("Kabir", LocalDate.parse("1999-12-01"), "kabir@gmail.com");
        underTest.addNewStudent(s);

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

        assertThatThrownBy(() -> underTest.addNewStudent(s))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email taken already");

        verify(studentRepository, never()).save(any());
    }

    @Test
    @Disabled
    void canDeleteStudent() {
        underTest.deleteStudent(1L);


    }

    /**
     * Method under test: {@link StudentService#deleteStudent(Long)}
     */
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

    /**
     * Method under test: {@link StudentService#deleteStudent(Long)}
     */
    @Test
    void testDeleteStudent2() {
        Student student = new Student();
        student.setAge(1);
        student.setDob(LocalDate.ofEpochDay(1L));
        student.setEmail("jane.doe@example.org");
        student.setId(123L);
        student.setName("Name");
        Optional<Student> ofResult = Optional.of(student);
        doThrow(new IllegalStateException("foo")).when(studentRepository).deleteById((Long) org.mockito.Mockito.any());
        when(studentRepository.existsById((Long) org.mockito.Mockito.any())).thenReturn(true);
        when(studentRepository.findById((Long) org.mockito.Mockito.any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> studentService.deleteStudent(123L));
        verify(studentRepository).existsById((Long) org.mockito.Mockito.any());
        verify(studentRepository).findById((Long) org.mockito.Mockito.any());
        verify(studentRepository).deleteById((Long) org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link StudentService#deleteStudent(Long)}
     */
    @Test
    void testDeleteStudent3() {
        Student student = new Student();
        student.setAge(1);
        student.setDob(LocalDate.ofEpochDay(1L));
        student.setEmail("jane.doe@example.org");
        student.setId(123L);
        student.setName("Name");
        Optional<Student> ofResult = Optional.of(student);
        doNothing().when(studentRepository).deleteById((Long) org.mockito.Mockito.any());
        when(studentRepository.existsById((Long) org.mockito.Mockito.any())).thenReturn(false);
        when(studentRepository.findById((Long) org.mockito.Mockito.any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> studentService.deleteStudent(123L));
        verify(studentRepository).existsById((Long) org.mockito.Mockito.any());
        verify(studentRepository).findById((Long) org.mockito.Mockito.any());
    }

    @Test
    @Disabled
    void updateStudent() {
    }
}