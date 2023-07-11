package com.online.shopping.repository;

import com.online.shopping.entity.Category;
import com.online.shopping.dto.CategoryResponseDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    CategoryResponseDTO findByNameAllIgnoreCase(String category);

    Optional<Category> findByName(String categoryName);

}
