package com.online.shopping.service;

import com.online.shopping.entity.Category;
import com.online.shopping.entity.Product;
import com.online.shopping.exception.ResourceNotFoundException;
import com.online.shopping.repository.CategoryRepository;
import com.online.shopping.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.online.shopping.util.OnlineShoppingAppUtility.generateUserId;

/**
 * 1. Admin can add a new product or delete an existing product.
 * 2. Admin can update a product price or the product inventory details such as availability & reserved attributes.
 * 3. Can be able to access all the list of products using a category name specified.
 */

@Service
@Slf4j
public class ProductService {

    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;

    /**
     * Constructor Injection - All Argument Constructor
     * @param productRepository
     * @param categoryRepository
     */
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Method to save the product details to DB
     * @param product
     * @return
     */
    public Product saveProduct(Product product) {
        log.info("Entering in ProductService saveProduct() method...");
        product.setId(generateUserId(10));
        Optional<Product> savedProduct = productRepository.findById(product.getId());
        if (!savedProduct.isEmpty()) {
            log.debug("Product already exist with given id:" + product.getId());
            throw new ResourceNotFoundException("Product already exist with given id:" + product.getId());
        }
        log.info("Exiting in ProductServiceImpl saveProduct() method...");
        saveCategory(product.getCategory());
        return productRepository.save(product);
    }

    /**
     * Method to save the category if not existed of a product details to DB
     * @param category
     */
    public void saveCategory(Category category) {
        log.info("Entering in ProductService saveCategory() method...");
        Optional<Category> categoryName = categoryRepository.findByName(category.getName());
        if (categoryName.isEmpty()) {
            log.debug("Category database does not contains the record with provided name. Creating a new Entry !!!");
            Category tempCategory = new Category(generateUserId(10), category.getName());
            categoryRepository.save(tempCategory);
        }
        log.debug("Category database already contains the record with provided name...!");
        log.info("Exiting in ProductService saveCategory() method...");
    }

    /**
     * Get All products from Product DB
     * @return
     */
    public List<Product> getAllProduct() {
        log.info("Entering in ProductServiceImpl getAllProduct() method...");
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            log.debug("Product database is empty !");
            throw new ResourceNotFoundException("Product database is empty !");
        }
        log.info("Exiting in ProductServiceImpl getAllProduct() method...");
        return products;
    }

    /**
     * Get the product details from DB using the Product ID
     * @param productId
     * @return
     */
    public Product getProductById(String productId) {
        log.info("Entering in ProductServiceImpl getProductById() method...");
        Product savedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + productId));
        log.info("Exiting in ProductServiceImpl getProductById() method...");
        return savedProduct;
    }

    /**
     * Update a product details to an existing product and save DB
     * @param productId
     * @param product
     * @return
     */
    public Product updateProduct(String productId, Product product) {
        log.info("Entering in ProductServiceImpl updateProduct() method...");
        Product savedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + productId));

        savedProduct.setName(product.getName());
        savedProduct.setBrand(product.getBrand());
        savedProduct.setDescription(product.getDescription());
        savedProduct.setCategory(product.getCategory());
        savedProduct.setPrice(product.getPrice());
        savedProduct.setInventory(product.getInventory());
        savedProduct.setAttributes(product.getAttributes());
        Product updatedProduct = productRepository.save(savedProduct);
        log.info("Exiting in ProductServiceImpl updateProduct() method...");
        return updatedProduct;
    }

    /**
     * Delete a product from DB using the product id.
     * @param id
     */
    public void deleteProduct(String id) {
        log.info("Entering in ProductServiceImpl deleteProduct() method...");
        Optional<Product> savedProduct = productRepository.findById(id);
        if (!savedProduct.isEmpty()) {
            productRepository.deleteById(id);
            log.debug("product deleted with the provided id : " + id);
        } else {
            log.debug("Product is not exist with given id : " + id);
            throw new ResourceNotFoundException("Product is not exist with given id:" + id);
        }
        log.info("Exiting in ProductServiceImpl deleteProduct() method...");
    }

    /**
     * Get all the list of products using a category name specified.
     * @param category
     * @return
     */
    public List<Product> getProductByCategory(String category) {
        log.info("Entering in ProductServiceImpl getProductByCategory() method...");
        List<Product> products = productRepository.findByCategoryNameIgnoreCase(category);

        if (products.isEmpty()) {
            log.debug("Product of " + category + " category is not available in database !");
            throw new ResourceNotFoundException("Product of " + category + " category is not available in database !");
        }
        log.info("Exiting in ProductServiceImpl getProductByCategory() method...");
        return products;

    }

    /**
     * Get all the list of products using a category name specified with Paging.
     * @param sortBy
     * @param pageNo
     * @param pagesize
     * @return
     */
    public List<Product> getProductByCategoryWithPaging(String sortBy, int pageNo, int pagesize) {
        log.info("Entering in ProductServiceImpl getProductByCategoryWithPaging() method...");

        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pagesize, sort);
        Page<Product> products = productRepository.findAll(pageable);
        List<Product> productList = products.getContent();
        if (products.isEmpty()) {
            log.debug("Product is not available in database !");
            throw new ResourceNotFoundException("Product is not available in database !");
        }
        log.info("Exiting in ProductServiceImpl getProductByCategoryWithPaging() method...");
        return productList;
    }

}
