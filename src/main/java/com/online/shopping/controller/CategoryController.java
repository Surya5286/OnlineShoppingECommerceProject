package com.online.shopping.controller;


import com.online.shopping.dto.DataResponse;
import com.online.shopping.dto.ResponseModalDTO;
import com.online.shopping.entity.Category;
import com.online.shopping.entity.Product;
import com.online.shopping.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/categories")
@Tag(name = "Category Controller", description = "Category API Documentation Details")
public class CategoryController {
    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Retrieve a Product by category", description = "Get a Product object by specifying its category. The response is List of Product object with id, name,brand,description,price,inventary and attributes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Find List of Product ", content = {
                    @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Resource Not Found Exception ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")})})
    @GetMapping("{categoryName}/products/")
    public ResponseEntity<?> getCategoryProducts(@PathVariable("categoryName") String categoryName,
                                                 @Parameter(description = "Sort Using inventory availability / price", example = "price") @RequestParam(value = "sortBy", defaultValue = "inventory") String sortBy,
                                                 @Parameter(description = "Sorting Order", example = "asc/desc") @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder,
                                                 @Parameter(description = "Page Number", example = "Min value is 1") @RequestParam(value = "pageNo", defaultValue = "1") @Min(1) int pageNo,
                                                 @Parameter(description = "Page Size", example = "10") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        logger.info("Entering in CategoryController getCategoryProducts() method");
        ResponseModalDTO responseModalDTO = categoryService.getCategoryProduct(categoryName, sortBy, sortOrder, pageNo, pageSize);
        logger.info("Exiting from CategoryController getCategoryProducts() method");
        return ResponseEntity.ok(responseModalDTO);
    }

    @Operation(summary = "Save Category in DB", description = "save a Category object by specifying Category name. The response is Category object with id, name")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category is Created ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request/ Resource already Existed ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")})})
    @PostMapping("{categoryName}/products/")
    public ResponseEntity<?> createCategory(@PathVariable("categoryName") String categoryName) {
        logger.info("Entering in CategoryController getCategoryProducts() method");
        Category responseObj = categoryService.saveCategory(categoryName);
        logger.info("Exiting from CategoryController getCategoryProducts() method");
        return ResponseEntity.ok(responseObj);
    }

}