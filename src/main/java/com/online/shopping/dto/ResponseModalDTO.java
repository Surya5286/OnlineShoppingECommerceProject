package com.online.shopping.dto;

import com.online.shopping.config.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Generated
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModalDTO {

    private CategoryResponseDTO categoryResponseDTO;
    private ProductResponseDTO productResponseDTO;

    public CategoryResponseDTO getCategoryResponseDTO() {
        return categoryResponseDTO;
    }

    public void setCategoryResponseDTO(CategoryResponseDTO categoryResponseDTO) {
        this.categoryResponseDTO = categoryResponseDTO;
    }

    public ProductResponseDTO getProductResponseDTO() {
        return productResponseDTO;
    }

    public void setProductResponseDTO(ProductResponseDTO productResponseDTO) {
        this.productResponseDTO = productResponseDTO;
    }


}
