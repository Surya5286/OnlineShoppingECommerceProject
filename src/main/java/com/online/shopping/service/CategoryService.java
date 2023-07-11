package com.online.shopping.service;

import com.online.shopping.dto.CategoryResponseDTO;
import com.online.shopping.dto.ProductResponseDTO;
import com.online.shopping.dto.ResponseModalDTO;
import com.online.shopping.entity.Category;
import com.online.shopping.entity.Product;
import com.online.shopping.exception.ProductNotAvailableException;
import com.online.shopping.exception.ResourceNotFoundException;
import com.online.shopping.repository.CategoryRepository;
import com.online.shopping.repository.ProductRepository;
import com.online.shopping.util.OnlineShoppingAppUtility;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 1.The category page should not display products if their inventory is unavailable or limited.
 * 2.If there are no products available with sufficient inventory, an error message should be displayed on the category page.
 * 3.If a non-existent category is requested, an error message is to be sent back
 */

@Service
@Slf4j
public class CategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private CategoryRepository categoryRepository;

    private ProductRepository productRepository;

    /**
     * Constructor Injection - All Argument Constructor
     * @param categoryRepository
     * @param productRepository
     */
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * 1. Method to retrieve the List of products using the category name with the specified sorting order
     * 2. Sort the List of products based on inventory availability or Price.
     * @param categoryName
     * @param sortBy
     * @param sortOrder
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ResponseModalDTO getCategoryProduct(String categoryName, String sortBy, String sortOrder, @Min(1) int pageNo,
                                               int pageSize) {
        logger.info("CategoryService  getCategoryProducts() method calling..");
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        Optional<Category> categoryObject = categoryRepository.findByName(categoryName);
        if (!categoryObject.isPresent()) {
            logger.debug("category " + categoryName + "  is not available in database !");
            throw new ResourceNotFoundException("category " + categoryName + "  is not available in database !");
        }

        ResponseModalDTO responseModalDTO = new ResponseModalDTO();

        List<Product> product = null;

        product = productRepository.findByCategoryNameIgnoreCase(categoryName);

        if (product != null && !product.isEmpty()) {
            if (sortBy != null) {
                if ("inventory".equalsIgnoreCase(sortBy)) {
                    logger.debug("Sorting product based on inventary assending order ");
                    product.sort(Comparator.comparingInt((Product p) -> p.getInventory().getAvailable()));
                } else if ("price".equalsIgnoreCase(sortBy)) {
                    logger.debug("Sorting product based on price ascending order");
                    product.sort(Comparator.comparingDouble(p -> p.getPrice().getAmount()));
                }
            }
            if (sortOrder != null && "desc".equalsIgnoreCase(sortOrder) && "inventory".equalsIgnoreCase(sortBy)) {
                logger.debug("Sorting product based on inventory descending order ");
                product.sort((p1, p2) -> Integer.compare(p2.getInventory().getAvailable(),
                        p1.getInventory().getAvailable()));
            }
            if (sortOrder != null && "desc".equalsIgnoreCase(sortOrder) && "price".equalsIgnoreCase(sortBy)) {
                logger.debug("Sorting product based on price descending order ");
                product.sort((p1, p2) -> Double.compare(p2.getPrice().getAmount(), p1.getPrice().getAmount()));
            }
            List<Product> availableProducts = product.stream().filter(p -> p.getInventory().getAvailable() > p.getInventory().getReserved())
                    .collect(Collectors.toList());

            List<Product> availableProductsWithPagination = getObjectListWithPagination(availableProducts, pageNo,
                    pageSize);

            ProductResponseDTO productResponseDTO = mapToProduct(availableProductsWithPagination);
            categoryResponseDTO.setId(categoryObject.get().getId());
            categoryResponseDTO.setCategoryName(categoryName);
            responseModalDTO.setProductResponseDTO(productResponseDTO);
            responseModalDTO.setCategoryResponseDTO(categoryResponseDTO);

        } else {
            logger.debug("products-service is not available!");
            throw new ProductNotAvailableException("products-service is not available, please try after some time ! ");
        }

        return responseModalDTO;
    }

    /**
     * Method to map list of products to ProductResponseDTO
     * @param products
     * @return
     */
    private ProductResponseDTO mapToProduct(List<Product> products) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProducts(products);
        return productResponseDTO;
    }

    /**
     * Method to set the list of products with the provided pagination details.
     * @param objectList
     * @param page
     * @param pageSize
     * @return
     */
    private List<Product> getObjectListWithPagination(List<Product> objectList, int page, int pageSize) {

        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, objectList.size());
        if (startIndex >= endIndex) {
            return Collections.emptyList();
        }
        return objectList.subList(startIndex, endIndex);
    }

    /**
     * Method to save a category details to the DB.
     * @param categoryName
     * @return
     */
    public Category saveCategory(String categoryName) {
        logger.info("Started CategoryService  saveCategory() method calling..");
        Optional<Category> name = categoryRepository.findByName(categoryName);
        if (!name.isEmpty()) {
            logger.debug("category " + categoryName + "  is already available in database !");
            throw new ResourceNotFoundException("category " + categoryName + "  is already available in database !");
        }
        Category obj = new Category(OnlineShoppingAppUtility.generateUserId(10), categoryName);
        categoryRepository.save(obj);
        logger.debug("Category Object Successfully Saved to DB", obj);

        logger.info("Exiting | CategoryService  saveCategory() method calling..");
        return obj;
    }
}
