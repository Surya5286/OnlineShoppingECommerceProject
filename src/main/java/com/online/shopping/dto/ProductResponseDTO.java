package com.online.shopping.dto;

import com.online.shopping.config.Generated;
import com.online.shopping.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Generated
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}