package com.fraus.spring.security;

import com.fraus.spring.shop.service.ShopService;
import com.fraus.spring.shop.web.ShopController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShopController.class)
public class ProductSecurityTest {
    @MockitoBean
    private ShopService shopService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldReturn401WithoutAuth() throws Exception {
        mockMvc.perform(get("/api/catalog"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturn403ForUser() throws Exception {
        mockMvc.perform(post("/api/catalog"))
                .andExpect(status().isForbidden());
    }
}
