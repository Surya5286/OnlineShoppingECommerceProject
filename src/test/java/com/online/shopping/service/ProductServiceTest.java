package com.online.shopping.service;

import com.online.shopping.entity.*;
import com.online.shopping.exception.ResourceNotFoundException;
import com.online.shopping.repository.CategoryRepository;
import com.online.shopping.repository.ProductRepository;
import com.online.shopping.util.OnlineShoppingAppUtility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Category category;

    @BeforeEach
    public void setup() {
        productService = new ProductService(productRepository, categoryRepository);
        category = new Category("testCategId", "test-category");
        product = new Product("TestProdId", "test name",
                "test brand", "description for the test product",
                new Price("INR", 1234.56),
                new Inventory(50, 40, 10),
                List.of(new Attributes("color", "test color")),
                category);

    }

//    @AfterAll
//    public static void close() {
//        mockedStatic.close();
//    }

    @DisplayName("Junit Test case for SaveCategory method - returns new created category object")
    @Test
    void givenCategoryObject_whenSaveCategory_thenReturnCategoryObject() {
        // given - precondition or setup
        given(categoryRepository.findByName(category.getName()))
                .willReturn(Optional.empty());

        given(categoryRepository.save(category)).willReturn(category);

        // when -  action or the behaviour that we are going test
        Category savedCategory = categoryRepository.save(category);

        // then - verify the output
        assertThat(savedCategory).isNotNull();
    }

    @DisplayName("Junit Test case for SaveCategory method - should not create a category object")
    @Test
    void givenCategoryObjectAvailable_whenSaveCategory_thenShouldNotCreateCategoryObject() {
        // given - precondition or setup
        given(categoryRepository.findByName(category.getName()))
                .willReturn(Optional.of(category));

        given(categoryRepository.save(category)).willReturn(null);

        // when -  action or the behaviour that we are going test
        Category savedCategory = categoryRepository.save(category);

        // then - verify the output
        assertThat(savedCategory).isNull();
    }

    @DisplayName("Junit Test case for SaveProduct method - should create a Product object")
    @Test
    void givenProductObject_whenSaveProduct_thenReturnProductObject() {
        // given - precondition or setup
        ProductService productServiceSpy = spy(new ProductService(productRepository, categoryRepository));
        willDoNothing().given(productServiceSpy).saveCategory(category);

        given(productRepository.findById(product.getId()))
                .willReturn(Optional.empty());

        given(productRepository.save(product)).willReturn(product);

        // when -  action or the behaviour that we are going test
        Product savedProduct = productRepository.save(product);

        // then - verify the output
        assertThat(savedProduct).isNotNull();
    }

    @DisplayName("Junit Test case for SaveProduct method - throws exception (negative scenario) ")
    @Test
    void givenProductObject_whenSaveProduct_thenThrowsException() {
        // given - precondition or setup
        given(categoryRepository.findByName(category.getName()))
                .willReturn(Optional.of(category));

        MockedStatic<OnlineShoppingAppUtility> mockedStatic = mockStatic(OnlineShoppingAppUtility.class);
        mockedStatic.when(() -> OnlineShoppingAppUtility.generateUserId(10)).thenReturn("TestProdId");

        given(productRepository.findById(product.getId()))
                .willReturn(Optional.of(product));

        // when -  action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.saveProduct(product);
        });

        // then - verify the output
        verify(productRepository, never()).save(any(Product.class));

    }

    @DisplayName("Junit Test case for GetAllProducts method - should return Product List")
    @Test
    void givenProductList_whenGetAllProducts_thenReturnProductList() {
        // given - precondition or setup
        given(productRepository.findAll()).willReturn(List.of(product));

        // when -  action or the behaviour that we are going test
        List<Product> productList = productService.getAllProduct();

        // then - verify the output
        assertThat(productList).isNotNull();
        assertThat(productList.size()).isEqualTo(1);
    }

    @DisplayName("Junit Test case for GetAllProducts method - throws exception (negative scenario) ")
    @Test
    void givenProductList_whenGetAllProducts_thenThrowsException() {
        // given - precondition or setup
        given(productRepository.findAll()).willReturn(Collections.emptyList());

        // when -  action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getAllProduct();
        });

        // then - verify the output
        verify(productRepository, times(1)).findAll();
    }

    @DisplayName("Junit Test case for GetProductById method - should return Product Object")
    @Test
    void givenProductId_whenGetProductById_thenReturnProductObject() {
        // given - precondition or setup
        given(productRepository.findById("TestProdId")).willReturn(Optional.of(product));

        // when -  action or the behaviour that we are going test
        Product retreivedProduct = productService.getProductById("TestProdId");

        // then - verify the output
        assertThat(retreivedProduct).isNotNull();
        assertThat(retreivedProduct.getId()).isEqualTo("TestProdId");
    }

    @DisplayName("Junit Test case for GetProductById method - throws exception (negative scenario) ")
    @Test
    void givenProductId_whenGetProductById_thenThrowsException() {
        // given - precondition or setup
        given(productRepository.findById("TestProdId")).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById("TestProdId");
        });

        // then - verify the output
        verify(productRepository, times(1)).findById("TestProdId");
    }

    @DisplayName("Junit Test case for UpdateProduct method - should return updated Product Object")
    @Test
    void givenProductObject_whenUpdateProduct_thenReturnUpdatedProductObject() {
        // given - precondition or setup
        given(productRepository.findById("TestProdId")).willReturn(Optional.of(product));
        given(productRepository.save(product)).willReturn(product);

        product.getPrice().setAmount(1254.36);
        product.getInventory().setAvailable(10);

        // when -  action or the behaviour that we are going test
        Product updatedProduct = productService.updateProduct("TestProdId", product);

        // then - verify the output
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getPrice().getAmount()).isEqualTo(1254.36);
        assertThat(updatedProduct.getInventory().getAvailable()).isEqualTo(10);
    }

    @DisplayName("Junit Test case for UpdateProduct method - throws exception (negative scenario) ")
    @Test
    void givenProductObject_whenUpdateProduct_thenThrowsException() {
        // given - precondition or setup
        given(productRepository.findById("TestProdId")).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById("TestProdId");
        });

        // then - verify the output
        verify(productRepository, times(1)).findById("TestProdId");
    }

    @DisplayName("Junit Test case for DeleteProduct method - should return nothing")
    @Test
    void givenProductId_whenDeleteProduct_thenReturnNothing() {
        // given - precondition or setup
        given(productRepository.findById("TestProdId")).willReturn(Optional.of(product));
        willDoNothing().given(productRepository).deleteById("TestProdId");

        // when -  action or the behaviour that we are going test
        productService.deleteProduct("TestProdId");

        // then - verify the output
        verify(productRepository, times(1)).deleteById("TestProdId");
    }

    @DisplayName("Junit Test case for DeleteProduct method - throws exception (negative scenario) ")
    @Test
    void givenProductId_whenDeleteProduct_thenThrowsException() {
        // given - precondition or setup
        given(productRepository.findById("TestProdId")).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct("TestProdId");
        });

        // then - verify the output
        verify(productRepository, times(0)).deleteById("TestProdId");
    }

    @DisplayName("Junit Test case for ProductListByCategoryName method - should return Product List")
    @Test
    void givenCategoryName_whenGetProductListByCategoryName_thenReturnProductList() {
        // given - precondition or setup
        given(productRepository.findByCategoryNameIgnoreCase(category.getName())).willReturn(List.of(product));

        // when -  action or the behaviour that we are going test
        List<Product> productList = productService.getProductByCategory(category.getName());

        // then - verify the output
        assertThat(productList).isNotNull();
        assertThat(productList.size()).isEqualTo(1);
    }

    @DisplayName("Junit Test case for ProductListByCategoryName method - throws exception (negative scenario) ")
    @Test
    void givenCategoryName_whenGetProductListByCategoryName_thenThrowsException() {
        // given - precondition or setup
        given(productRepository.findByCategoryNameIgnoreCase(category.getName())).willReturn(Collections.emptyList());

        // when -  action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductByCategory(category.getName());
        });

        // then - verify the output
        verify(productRepository, times(1)).findByCategoryNameIgnoreCase(category.getName());
    }

}