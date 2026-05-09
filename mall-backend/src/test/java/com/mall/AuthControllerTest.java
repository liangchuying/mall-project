package com.mall;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.dto.LoginDTO;
import com.mall.dto.RegisterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void testHello() throws Exception {
        mockMvc.perform(get("/api/test/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("商城系统启动成功！"));
    }

    @Test
    @Transactional
    void testRegister() throws Exception {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("123456");
        dto.setNickname("测试用户");
        dto.setPhone("13800138000");
        dto.setEmail("test@example.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @Transactional
    void testRegisterDuplicateUsername() throws Exception {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("duplicateuser");
        dto.setPassword("123456");
        dto.setPhone("13800138001");
        dto.setEmail("test1@example.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    @Transactional
    void testRegisterInvalidPhone() throws Exception {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("testuser2");
        dto.setPassword("123456");
        dto.setPhone("12345");
        dto.setEmail("test2@example.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void testLogin() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("loginuser");
        registerDTO.setPassword("123456");
        registerDTO.setPhone("13900139000");
        registerDTO.setEmail("login@example.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("loginuser");
        loginDTO.setPassword("123456");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.userId").exists());
    }

    @Test
    @Transactional
    void testLoginWrongPassword() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("wronguser");
        registerDTO.setPassword("123456");
        registerDTO.setPhone("13900139001");
        registerDTO.setEmail("wrong@example.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("wronguser");
        loginDTO.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("notexist");
        loginDTO.setPassword("123456");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    @Transactional
    void testGetUserInfo() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("infouser");
        registerDTO.setPassword("123456");
        registerDTO.setPhone("13900139002");
        registerDTO.setEmail("info@example.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("infouser");
        loginDTO.setPassword("123456");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(loginResponse).path("data").path("token").asText();

        mockMvc.perform(get("/api/auth/info")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("infouser"));
    }

    @Test
    void testGetUserInfoWithoutToken() throws Exception {
        mockMvc.perform(get("/api/auth/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401));
    }
}
