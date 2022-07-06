package com.example.task.service;

import com.example.task.model.User;
import com.example.task.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {MyUserDetailService.class})
@ExtendWith(SpringExtension.class)
class MyUserDetailServiceTest {
    @Autowired
    private MyUserDetailService myUserDetailService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        User user = new User();
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        when(userRepository.findByUsername((String) any())).thenReturn(user);
        assertEquals("janedoe", myUserDetailService.loadUserByUsername("janedoe").getUsername());
        verify(userRepository).findByUsername((String) any());
    }

    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        when(userRepository.findByUsername((String) any())).thenThrow(new UsernameNotFoundException("Msg"));
        assertThrows(UsernameNotFoundException.class, () -> myUserDetailService.loadUserByUsername("janedoe"));
        verify(userRepository).findByUsername((String) any());
    }
}

