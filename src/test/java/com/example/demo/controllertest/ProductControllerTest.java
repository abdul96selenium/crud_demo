package com.example.demo.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.example.demo.controller.ProductController;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@WebMvcTest(controllers =  ProductController.class)
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Product> productList;

    @BeforeEach
    void setUp() {
        productList = new ArrayList<>();
        productList.add(new Product("1", "Product 1", 10.0));
        productList.add(new Product("2", "Product 2", 20.0));
    }

    @Test
    void testGetAllProducts() throws Exception{

        when(productService.getAllProducts()).thenReturn(productList);

        ResultActions response = mockMvc.perform(get("/api/products/getall")
                .contentType(MediaType.APPLICATION_JSON));
        String expectedJson = objectMapper.writeValueAsString(productList);
        response.andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    void testGetProductById_Success() throws Exception {
        String productId = "1";
        Product expectedProduct = new Product(productId, "Test", 10.0);
        when(productService.getProductById(Mockito.anyString())).thenReturn(expectedProduct);
        ResultActions response = mockMvc.perform(get("/api/products/getbyid/1")
                .contentType(MediaType.APPLICATION_JSON));
        String expectedJson = objectMapper.writeValueAsString(expectedProduct);
        response.andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));

    }

    @Test
    void testCreateProduct() throws Exception{
        Product newProduct = new Product("1", "New Product", 20.0);
        given(productService.saveProduct(Mockito.any(Product.class))).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/api/products/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)));
        String expectedJson = objectMapper.writeValueAsString(newProduct);
        response.andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

}