package com.online.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.shopping.dto.CategoryResponseDTO;
import com.online.shopping.dto.ProductResponseDTO;
import com.online.shopping.dto.ResponseModalDTO;
import com.online.shopping.entity.*;
import com.online.shopping.repository.CategoryRepository;
import com.online.shopping.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    private Category category;

    private Product product;

    @BeforeEach
    public void setup() {
        categoryController = new CategoryController(categoryService);
        product = new Product("1234567890", "laptop", "apple", "very good", new Price("USD", 12.99),
                new Inventory(35, 20, 5), List.of(new Attributes("colour", "white")), new Category("1234", "electronics"));
        category = new Category("1234567890", "electronics");
    }

    @Test
    public void getCategoryProductsAPITest() throws Exception {

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO("1234567890", "electronics");
        ProductResponseDTO productResponseDTO = new ProductResponseDTO(productList);
        ResponseModalDTO responseModalDTO = new ResponseModalDTO();
        responseModalDTO.setCategoryResponseDTO(categoryResponseDTO);
        responseModalDTO.setProductResponseDTO(productResponseDTO);

        given(categoryService.getCategoryProduct("electronics", "inventory", "asc", 1, 10))
                .willReturn(responseModalDTO);

        // when -  action or the behaviour that we are going test
        ResultActions response = mvc.perform(get("/v1/categories/{categoryName}/products/", "electronics"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void createCategoryAPITest() throws Exception {

        given(categoryService.saveCategory("electronics")).willReturn(category);
        ResultActions response = mvc.perform(post("/v1/categories/{categoryName}/products/", "electronics"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
