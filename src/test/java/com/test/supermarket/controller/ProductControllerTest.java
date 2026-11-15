package com.test.supermarket.controller;

import com.test.supermarket.dto.ProductDTO;
import com.test.supermarket.service.IProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    private static final String PRODUCTS_URL = "/api/products";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IProductService productService;

    @Test
    void shouldReturnAllProducts() throws Exception {
        given(productService.getProducts()).willReturn(List.of(
                ProductDTO.builder()
                        .id(1L)
                        .name("Whole Milk")
                        .category("Dairy")
                        .price(2.50)
                        .quantity(40)
                        .build(),
                ProductDTO.builder()
                        .id(2L)
                        .name("Whole Wheat Bread")
                        .category("Bakery")
                        .price(3.75)
                        .quantity(25)
                        .build()
        ));

        mockMvc.perform(get(PRODUCTS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Whole Milk"))
                .andExpect(jsonPath("$[0].category").value("Dairy"))
                .andExpect(jsonPath("$[0].price").value(2.50))
                .andExpect(jsonPath("$[0].quantity").value(40))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Whole Wheat Bread"))
                .andExpect(jsonPath("$[1].category").value("Bakery"))
                .andExpect(jsonPath("$[1].price").value(3.75))
                .andExpect(jsonPath("$[1].quantity").value(25));

        verify(productService).getProducts();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoProducts() throws Exception {
        given(productService.getProducts()).willReturn(List.of());

        mockMvc.perform(get(PRODUCTS_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(productService).getProducts();
    }

    @Test
    void shouldCreateProductAndReturnCreatedProduct() throws Exception {
        ProductDTO createdProduct = ProductDTO.builder()
                .id(10L)
                .name("Greek Yogurt")
                .category("Dairy")
                .price(4.25)
                .quantity(15)
                .build();
        given(productService.createProduct(any(ProductDTO.class))).willReturn(createdProduct);

        mockMvc.perform(post(PRODUCTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Greek Yogurt",
                                  "category": "Dairy",
                                  "price": 4.25,
                                  "quantity": 15
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, PRODUCTS_URL + "/10"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Greek Yogurt"))
                .andExpect(jsonPath("$.category").value("Dairy"))
                .andExpect(jsonPath("$.price").value(4.25))
                .andExpect(jsonPath("$.quantity").value(15));

        ArgumentCaptor<ProductDTO> captor = ArgumentCaptor.forClass(ProductDTO.class);
        verify(productService).createProduct(captor.capture());

        ProductDTO request = captor.getValue();
        assertThat(request.getId()).isNull();
        assertThat(request.getName()).isEqualTo("Greek Yogurt");
        assertThat(request.getCategory()).isEqualTo("Dairy");
        assertThat(request.getPrice()).isEqualTo(4.25);
        assertThat(request.getQuantity()).isEqualTo(15);
    }

    @Test
    void shouldUpdateProductAndReturnUpdatedProduct() throws Exception {
        ProductDTO updatedProduct = ProductDTO.builder()
                .id(5L)
                .name("Organic Whole Milk")
                .category("Dairy")
                .price(3.10)
                .quantity(30)
                .build();
        given(productService.updateProduct(eq(5L), any(ProductDTO.class))).willReturn(updatedProduct);

        mockMvc.perform(put(PRODUCTS_URL + "/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Organic Whole Milk",
                                  "category": "Dairy",
                                  "price": 3.10,
                                  "quantity": 30
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Organic Whole Milk"))
                .andExpect(jsonPath("$.category").value("Dairy"))
                .andExpect(jsonPath("$.price").value(3.10))
                .andExpect(jsonPath("$.quantity").value(30));

        ArgumentCaptor<ProductDTO> captor = ArgumentCaptor.forClass(ProductDTO.class);
        verify(productService).updateProduct(eq(5L), captor.capture());

        ProductDTO request = captor.getValue();
        assertThat(request.getName()).isEqualTo("Organic Whole Milk");
        assertThat(request.getCategory()).isEqualTo("Dairy");
        assertThat(request.getPrice()).isEqualTo(3.10);
        assertThat(request.getQuantity()).isEqualTo(30);
    }

    @Test
    void shouldDeleteProductAndReturnNoContent() throws Exception {
        mockMvc.perform(delete(PRODUCTS_URL + "/{id}", 7L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(productService).deleteProduct(7L);
    }
}
