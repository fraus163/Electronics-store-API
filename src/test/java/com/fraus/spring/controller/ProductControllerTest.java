package com.fraus.spring.controller;

import com.fraus.spring.shop.repository.entity.BrandType;
import com.fraus.spring.shop.repository.entity.ProductType;
import com.fraus.spring.shop.service.ShopService;
import com.fraus.spring.shop.web.Dto.ProductDto;
import com.fraus.spring.shop.web.ShopController;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShopController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {
    @MockitoBean
    private ShopService shopService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateProduct() throws Exception {
        ProductDto productDto = new ProductDto(
                BrandType.INTEL,
                "Core Ultra 5 245K",
                "Процессор",
                new BigDecimal("21000"),
                ProductType.CPU,
                10
        );

        mockMvc.perform(post("/api/catalog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated());

        verify(shopService)
                .createProduct(any());
    }

    @Test
    void shouldGetProductById() throws Exception {
        ProductDto productDto = new ProductDto(
                BrandType.INTEL,
                "Core Ultra 5 245K",
                "Процессор",
                new BigDecimal("21000"),
                ProductType.CPU,
                10
        );

        when(shopService.findProductById(1L))
                .thenReturn(productDto);

        mockMvc.perform(get("/api/catalog/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("INTEL"))
                .andExpect(jsonPath("$.name").value("Core Ultra 5 245K"))
                .andExpect(jsonPath("$.description").value("Процессор"))
                .andExpect(jsonPath("$.price").value(new BigDecimal("21000")))
                .andExpect(jsonPath("$.type").value("CPU"))
                .andExpect(jsonPath("$.quantity").value("10"));
    }

    @Test
    void shouldGetProductByFilter() throws Exception {
        ProductType type = ProductType.CPU;
        BrandType brand = BrandType.INTEL;
        Pageable pageable = PageRequest.of(0, 1);
        ProductDto productDto = new ProductDto(
                brand,
                "Core Ultra 5 245K",
                "Процессор",
                new BigDecimal("21000"),
                type,
                10
        );

        List<ProductDto> products = new ArrayList<>(List.of(productDto));

        when(shopService.findByFilter(brand, type, pageable))
                .thenReturn(products);

        mockMvc.perform(get("/api/catalog?")
                .param("brand", brand.name())
                .param("type", type.name())
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size",String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value(productDto.brand().name()))
                .andExpect(jsonPath("$[0].name").value(productDto.name()))
                .andExpect(jsonPath("$[0].description").value(productDto.description()))
                .andExpect(jsonPath("$[0].price").value(productDto.price()))
                .andExpect(jsonPath("$[0].type").value(productDto.type().name()))
                .andExpect(jsonPath("$[0].quantity").value(productDto.quantity()));
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        when(shopService.findProductById(1L))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/catalog/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteProduct() throws Exception{
        mockMvc.perform(delete("/api/catalog/1"))
                .andExpect(status().isOk());

        verify(shopService)
                .deleteProduct(1L);
    }

    @Test
    void shouldReturn400WhenNameIsNull() throws Exception {
        ProductDto productDto = new ProductDto(
                BrandType.INTEL,
                null,
                "Процессор",
                new BigDecimal("21000"),
                ProductType.CPU,
                10
        );

        mockMvc.perform(post("/api/catalog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        ProductDto productDto = new ProductDto(
                BrandType.INTEL,
                "Core Ultra 5 245K",
                "Процессор",
                new BigDecimal("21000"),
                ProductType.CPU,
                10
        );

        when(shopService.updateProduct(eq(1L), any()))
                .thenReturn(productDto);

        mockMvc.perform(put("/api/catalog/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("INTEL"))
                .andExpect(jsonPath("$.name").value("Core Ultra 5 245K"))
                .andExpect(jsonPath("$.description").value("Процессор"))
                .andExpect(jsonPath("$.price").value(new BigDecimal("21000")))
                .andExpect(jsonPath("$.type").value("CPU"))
                .andExpect(jsonPath("$.quantity").value("10"));
    }
}
