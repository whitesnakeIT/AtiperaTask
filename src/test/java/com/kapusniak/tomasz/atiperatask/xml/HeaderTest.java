package com.kapusniak.tomasz.atiperatask.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HeaderTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("should return status 406 after using xml header instead of json. ")
    void shouldReturnNotAcceptableForXml() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/")
                .param("input", "exampleUser")
                .accept(MediaType.APPLICATION_XML));

        // then
        result.andExpect(status().isNotAcceptable());

    }
}