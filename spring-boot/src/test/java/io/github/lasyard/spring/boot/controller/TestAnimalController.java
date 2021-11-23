package io.github.lasyard.spring.boot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = {AnimalController.class})
public class TestAnimalController {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testGetDog() throws Exception {
        mvc.perform(
                get("/dog")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.type").value("dog"))
            .andExpect(jsonPath("$.name").value("John"));
    }

    @Configuration
    @EnableAutoConfiguration
    @Import({
        AnimalController.class
    })
    static class Config {
    }
}
