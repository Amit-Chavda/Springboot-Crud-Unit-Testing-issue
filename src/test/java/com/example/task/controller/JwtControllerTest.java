package com.example.task.controller;

import com.example.task.helper.JwtUtil;
import com.example.task.model.User;
import com.example.task.service.MyUserDetailService;
import com.example.task.service.UserDetailsImplement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtController jwtController;

    @MockBean
    private MyUserDetailService myUserDetailService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void testGenerateToken() throws Exception {

        String username = "user";
        String password = "pass";

//        JwtRequest jwtRequest = new JwtRequest(username, password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        UserDetailsImplement userDetailsImplement = new UserDetailsImplement(user);
        when(myUserDetailService.loadUserByUsername(any())).thenReturn(userDetailsImplement);
        when(jwtUtil.generateToken(userDetailsImplement)).thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjU3NTIyNDM2LCJpYXQiOjE2NTc1MjIzMTZ9.KPBPtqv9ZNUswDyhiDEUvqU6JPoI1nM0z9c2jKhHKCs");
        String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/token")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(body)).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        System.out.println(response + "1");
        response = response.replace("{\"access_token\": \"", "");
        String token = response.replace("\"}", "");
//
        System.out.println(token);
//
//        mvc.perform(MockMvcRequestBuilders.get("/test")
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isOk());

    }
}

