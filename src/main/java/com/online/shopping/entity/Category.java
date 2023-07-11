package com.online.shopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories")
@Schema(description = "Category Entity class")
public class Category {

    @Id
    @JsonIgnore
    private String id;

    @Indexed(name = "product_category_index", expireAfterSeconds = 432000)
    @NotBlank(message = "category is mandatory should not be empty")
    private String name;
}
