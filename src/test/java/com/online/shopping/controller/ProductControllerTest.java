package com.online.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.shopping.entity.*;
import com.online.shopping.repository.ProductRepository;
import com.online.shopping.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setup() {
        productController = new ProductController(productService);
        product = new Product("1234567890", "laptop", "apple", "very good", new Price("USD", 12.99),
                new Inventory(35, 20, 5), List.of(new Attributes("colour", "white")), new Category("1234", "electronics"));
    }

    @Test
    public void createProductAPITest() throws Exception {

        BDDMockito.given(productService.saveProduct(product)).willReturn(product);
        mvc.perform(MockMvcRequestBuilders
                        .post("/v1/api/admin/products")
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void updateProductAPITest() throws Exception {
        String id = "1234567890";
        given(productRepository.findById(id)).willReturn(Optional.ofNullable(product));
        given(productService.updateProduct(id, product))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mvc.perform(put("/v1/api/admin/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product)));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());


    }

    @Test
    public void deleteProductTest() throws Exception {
        // given - precondition or setup
        String id = "1234567890";
        willDoNothing().given(productService).deleteProduct(id);

        // when -  action or the behaviour that we are going test
        ResultActions response = mvc.perform(delete("/v1/api/admin/products/{id}", id));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getProductByCategoryAPITest() throws Exception {

        String category = "electronics";
        List<Product> listOfProduct = new ArrayList<>();
        listOfProduct.add(product);
        given(productService.getProductByCategory(category)).willReturn(listOfProduct);

        // when -  action or the behaviour that we are going test
        ResultActions response = mvc.perform(get("/v1/api/categories/{category}/products", category));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfProduct.size())));
    }

    @Test
    public void getProductByIdAPITest() throws Exception {
        String id = "1234567890";
        BDDMockito.given(productService.getProductById(id)).willReturn(product);
        mvc.perform(MockMvcRequestBuilders
                        .get("/v1/api/products/{id}", id)
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getProductByCategoryWithPaginationAPITest() throws Exception {

        String sortBy = "name";
        int page = 0;
        int pageSize = 10;
        String sortOrder = "asc";

        List<Product> listOfProduct = new ArrayList<>();
        listOfProduct.add(product);
        given(productService.getProductByCategoryWithPaging("name", page, pageSize)).willReturn(listOfProduct);

        // when -  action or the behaviour that we are going test
        ResultActions response = mvc.perform(get("/v1/api/products"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfProduct.size())));
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}