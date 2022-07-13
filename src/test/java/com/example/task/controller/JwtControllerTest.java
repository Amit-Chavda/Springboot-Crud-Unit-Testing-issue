package com.example.task.controller;

import com.example.task.helper.JwtUtil;
import com.example.task.model.User;
import com.example.task.repo.UserRepository;
import com.example.task.service.MyUserDetailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class JwtControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtController jwtController;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGenerateToken() throws Exception {

        System.out.println(new BCryptPasswordEncoder().encode("123456"));

        String username = "tony";
        String password = "123456";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body);

        MvcResult mvcResult = mvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print()).andReturn();

        Assertions.assertNotNull(mvcResult.getResponse().getContentAsString());
    }
}

