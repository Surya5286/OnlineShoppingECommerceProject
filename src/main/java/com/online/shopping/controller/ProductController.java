package com.online.shopping.controller;

import com.online.shopping.dto.DataResponse;
import com.online.shopping.entity.Product;
import com.online.shopping.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
@Tag(name = "Product Controller", description = "Product Documentation Details")
@Slf4j
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Save Product in DB", description = "save a Product object by specifying Product details. The response is Product object with id, name,brand,description,price and inventary, attributes.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product is Created ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Method Argument Not Valid Exception ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")})})
    @PostMapping("/admin/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a Product by Id", description = "Update a Product object by specifying its id.The response is Product object with id, name,brand,description,price,inventary and attributes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Update Product object ", content = {
                    @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Resource Not Found Exception ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")})})

    @PutMapping("/admin/products/{id}")
    public ResponseEntity<Product> updateProduct(@Parameter(description = "Product Id", example = "10 digit alphanumeric value") @PathVariable("id") String productId,
                                                 @Valid @RequestBody Product product) {
        log.info("Entering in ProductController updateProduct() method");
        Product savedProduct = productService.updateProduct(productId, product);
        log.info("Exiting from ProductController updateProduct() method");
        return new ResponseEntity<>(savedProduct, HttpStatus.OK);
    }

    @Operation(summary = "Delete a Product by Id", description = "Delete a Product object by specifying its id. The response is string object for the delete operation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deleted the Product", content = {
                    @Content(schema = @Schema(implementation = String.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Resource Not Found Exception ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")})})

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<String> deleteProduct(@Parameter(description = "Product Id", example = "10 digit alphanumeric value") @PathVariable("id") String productId) {
        log.info("Entering in ProductController deleteProduct() method");
        productService.deleteProduct(productId);
        log.info("Exiting from ProductController deleteProduct() method");
        return new ResponseEntity<String>("Product deleted successfully!.", HttpStatus.OK);

    }

    // Get All Products By Id
    @Operation(summary = "Retrieve a Product by category", description = "Get a Product object by specifying its category. The response is List of Product object with id, name,brand,description,price,inventary and attributes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Find List of Product ", content = {
                    @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Resource Not Found Exception ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")})})
    @GetMapping("/categories/{category}/products")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String category) throws Exception {
        log.info("Entering in ProductController getProductByCategory() method");
        List<Product> savedProduct = productService.getProductByCategory(category);
        log.info("Exiting from ProductController getProductByCategory() method");
        return new ResponseEntity<>(savedProduct, HttpStatus.OK);
    }


    // Get products By Id
    @Operation(summary = "Retrieve a Product by Id", description = "Get a Product object by specifying its id. The response is Product object with id, name,brand,description,price,inventary and attributes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Find Product object ", content = {
                    @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Resource Not Found Exception ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")})})
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@Parameter(description = "Product Id", example = "10 digit alphanumeric value") @PathVariable("id") String productId) {
        log.info("Entering in ProductController getProductById() method");
        Product savedProduct = productService.getProductById(productId);
        log.info("Exiting from ProductController getProductById() method");
        return new ResponseEntity<>(savedProduct, HttpStatus.OK);
    }


    // Sort Using Pagination
    @Operation(summary = "Retrieve a Product by category with pagination and sorting ", description = "Get a Product object by specifying its category. The response is List of Product object with id, name,brand,description,price,inventary and attributes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Find List of Product ", content = {
                    @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Resource Not Found Exception ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")})})
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProductByCategoryWithPagination(
            @Parameter(description = "Sort Using product name", example = "name") @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @Parameter(description = "Page Number", example = "0") @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @Parameter(description = "Page Size", example = "10") @RequestParam(value = "pagesize", defaultValue = "10") int pagesize,
            @Parameter(description = "Sorting Order", example = "asc/desc") @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder) {
        log.info("Entering in ProductController getProductByCategoryWithPagination() method");
        List<Product> savedProduct = productService.getProductByCategoryWithPaging(sortBy, pageNo, pagesize);
        log.info("Exiting from ProductController getProductByCategoryWithPagination() method");
        return new ResponseEntity<>(savedProduct, HttpStatus.OK);
    }

}
