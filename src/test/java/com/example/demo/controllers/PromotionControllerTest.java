package com.example.demo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromotionController.class)
public class PromotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetPromotions() throws Exception {
        mockMvc.perform(get("/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.promotions").isArray())
                .andExpect(jsonPath("$.data.promotions[0].id").value(1))
                .andExpect(jsonPath("$.data.promotions[0].name").value("夏季大促銷"))
                .andExpect(jsonPath("$.data.promotions[0].discountType").value("PERCENTAGE"))
                .andExpect(jsonPath("$.data.promotions[0].discountValue").value(50.0))
                .andExpect(jsonPath("$.data.promotions[0].isActive").value(true));
    }
}