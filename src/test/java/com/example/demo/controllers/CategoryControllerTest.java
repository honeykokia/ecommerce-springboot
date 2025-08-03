package com.example.demo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetCategories() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.categories").isArray())
                .andExpect(jsonPath("$.data.categories[0].id").value(1))
                .andExpect(jsonPath("$.data.categories[0].name").value("耳機"))
                .andExpect(jsonPath("$.data.categories[0].description").value("各式藍芽耳機"))
                .andExpect(jsonPath("$.data.categories[1].id").value(2))
                .andExpect(jsonPath("$.data.categories[1].name").value("手機"))
                .andExpect(jsonPath("$.data.categories[1].description").value("智慧型手機"))
                .andExpect(jsonPath("$.data.categories[2].id").value(3))
                .andExpect(jsonPath("$.data.categories[2].name").value("電腦"))
                .andExpect(jsonPath("$.data.categories[2].description").value("筆記型電腦與桌上型電腦"));
    }
}