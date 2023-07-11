package com.online.shopping.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Collection;

import static com.online.shopping.util.OnlineShoppingAppConstants.PRODUCT;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Document(collection = PRODUCT)
@Schema(description = "PRODUCT Entity class")
@CompoundIndexes({
        @CompoundIndex(name = "name_brand_category_index", def = "{'name' : 1, 'brand':1, 'category.name': 1}")})
public class Product {

    @Id
    private String id;

    @NotBlank(message = "Name is mandatory attribute should not be blank or empty")
    @Field(name = "product_name")
    @Schema(description = "product name")
    private String name;

    @NotBlank(message = "Brand is mandatory attribute should not be blank or empty")
    @Schema(description = "product brand")
    private String brand;

    @NotBlank(message = "Description is mandatory attribute should not be blank or empty")
    @Schema(description = "product description")
    private String description;

    @Schema(description = "product price")
    private @NonNull Price price;

    @Schema(description = "product inventory")
    private Inventory inventory;

    @NotEmpty(message = "Attributes are the specifications and mandatory should not be empty")
    @Schema(description = "product attributes")
    private Collection<Attributes> attributes = new ArrayList<Attributes>();

    @Schema(description = "product category")
    private Category category;
}
