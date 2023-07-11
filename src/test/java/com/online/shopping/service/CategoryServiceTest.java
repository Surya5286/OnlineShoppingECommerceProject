package com.online.shopping.service;

import com.online.shopping.dto.ResponseModalDTO;
import com.online.shopping.entity.*;
import com.online.shopping.exception.ResourceNotFoundException;
import com.online.shopping.repository.CategoryRepository;
import com.online.shopping.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CatogoryServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService CategoryService;

    private Product product;
    private Category category;
    private List<Product> productList = new ArrayList<Product>();

    @BeforeEach
    public void setup() {

        category = new Category("22334ID", "Dummy");
        product = new Product("1122ID", "ABC",
                "XYZ", "Great product",
                new Price("INR", 9000.00),
                new Inventory(100, 90, 10),
                List.of(new Attributes("color", "multicolor")),
                category);
        productList.add(product);

    }

    @DisplayName("Junit Test case for SaveCategory method - should create a Category object")
    @Test
    void givenCategoryObject_whenSaveProduct_thenReturnCategoryObject() {

        given(categoryRepository.findByName(category.getName()))
                .willReturn(Optional.empty());

        given(categoryRepository.save(category)).willReturn(category);

        // when -  action or the behaviour that we are going test
        Category savedCategory = CategoryService.saveCategory("Dummy");


        // then - verify the output
        assertThat(savedCategory).isNotNull();
    }

    @DisplayName("Junit Test case for SaveCategory method - throws exception (negative scenario) ")
    @Test
    void givenCategoryObject_whenSaveCategory_thenThrowsException() {

        given(categoryRepository.findByName(category.getName()))
                .willReturn(Optional.of(category));

        // when -  action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            CategoryService.saveCategory("Dummy");
        });

        // then - verify the output
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @DisplayName("Junit Test case for getCategoryProduct method - should retrun list of products")
    @Test
    void givenCategoryNameAndInventorySort_whenGetCategoryProduct_thenReturnListOfProduct() {

        given(categoryRepository.findByName("Dummy"))
                .willReturn(Optional.of(category));

        given(productRepository.findByCategoryNameIgnoreCase("Dummy"))
                .willReturn(productList);

        //given(productRepository.save(product)).willReturn(product);

        // when -  action or the behaviour that we are going test
        ResponseModalDTO savedProduct = CategoryService.getCategoryProduct("Dummy", "inventory", "asc", 1, 10);
        // then - verify the output
        assertThat(savedProduct).isNotNull();
    }

    @DisplayName("Junit Test case for getCategoryProduct method - throws exception (negative scenario) ")
    @Test
    void givenCategoryObject_whenGetCategoryProduct_thenThrowsException() {

        given(categoryRepository.findByName(category.getName()))
                .willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            ResponseModalDTO responseModel = CategoryService.getCategoryProduct("Dummy", "inventory", "asc", 1, 10);
        });

        // then - verify the output
        verify(productRepository, never()).findByCategoryNameIgnoreCase(category.getName());
    }

    @DisplayName("Junit Test case for getCategoryProduct method - should retrun list of products")
    @Test
    void givenCategoryNameAndPriceSort_whenGetCategoryProduct_thenReturnListOfProduct() {

        given(categoryRepository.findByName("Dummy"))
                .willReturn(Optional.of(category));

        given(productRepository.findByCategoryNameIgnoreCase("Dummy"))
                .willReturn(productList);

        //given(productRepository.save(product)).willReturn(product);

        // when -  action or the behaviour that we are going test
        ResponseModalDTO savedProduct = CategoryService.getCategoryProduct("Dummy", "price", "asc", 1, 10);
        // then - verify the output
        assertThat(savedProduct).isNotNull();
    }

    @DisplayName("Junit Test case for getCategoryProduct method - should retrun list of products")
    @Test
    void givenCategoryAndPriceSortAndDesc_whenGetCategoryProduct_thenReturnListOfProduct() {

        given(categoryRepository.findByName("Dummy"))
                .willReturn(Optional.of(category));

        given(productRepository.findByCategoryNameIgnoreCase("Dummy"))
                .willReturn(productList);

        //given(productRepository.save(product)).willReturn(product);

        // when -  action or the behaviour that we are going test
        ResponseModalDTO savedProduct = CategoryService.getCategoryProduct("Dummy", "price", "desc", 1, 10);
        // then - verify the output
        assertThat(savedProduct).isNotNull();
    }

    @DisplayName("Junit Test case for getCategoryProduct method - should retrun list of products")
    @Test
    void givenCategoryAndInventorySortAndDesc_whenGetCategoryProduct_thenReturnListOfProduct() {

        given(categoryRepository.findByName("Dummy"))
                .willReturn(Optional.of(category));

        given(productRepository.findByCategoryNameIgnoreCase("Dummy"))
                .willReturn(productList);

        //given(productRepository.save(product)).willReturn(product);

        // when -  action or the behaviour that we are going test
        ResponseModalDTO savedProduct = CategoryService.getCategoryProduct("Dummy", "inventory", "desc", 1, 10);
        // then - verify the output
        assertThat(savedProduct).isNotNull();
    }
}