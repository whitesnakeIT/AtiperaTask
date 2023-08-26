package com.kapusniak.tomasz.atiperatask.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class GitHubControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("should return index page")
    void showHomePage() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("should return status 406 after using xml accept header instead of json")
    void getUserDetailsAcceptXml() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/")
                .param("input", "exampleUser")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_XML));

        // then
        result.andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("should return status 200 after using json accept header")
    void getUserDetailsAcceptJson() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/")
                .param("input", "exampleUser")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpect(status().isOk());
    }

}