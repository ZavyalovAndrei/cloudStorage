package com.cloudStorage.tests;

import com.cloudStorage.controller.FilesController;
import com.cloudStorage.transfer.Request;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllersTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mockMvc;

    private static final String WRONG_USER_LOGIN = "user@internet.ru";
    private static final String USER_LOGIN = "user@internet.com";
    private static final String USER_PASSWORD = "hadgehog";
    private static final String LOGIN = "/login";
    private static final String LOGOUT = "/logout";

    private static Request validRequest;
    private static Request invalidRequest;
    private static final Gson gson = new Gson();

    @BeforeAll
    public static void beforeAll() {
        validRequest = new Request(USER_LOGIN, USER_PASSWORD);
        invalidRequest = new Request(WRONG_USER_LOGIN, USER_PASSWORD);
    }

    @Test
    void FilesControllerTest() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        Assertions.assertNotNull(servletContext);
        Assertions.assertTrue(servletContext instanceof MockServletContext);
        Assertions.assertNotNull(webApplicationContext.getBean(FilesController.class));
    }

    @Test
    void testLoginSuccess() throws Exception {
        mockMvc.perform(post(LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(validRequest))).andExpect(status().isOk());
    }

    @Test
    void testLoginFail() throws Exception {
        mockMvc.perform(post(LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(invalidRequest))).andExpect(status().isUnauthorized());
    }

    @Test
    void testLogoutSuccess() throws Exception {
        mockMvc.perform(post(LOGOUT).contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(validRequest))).andExpect(status().is3xxRedirection());
    }
}