package com.online.shopping.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Price Entity class")
public class Price {

    @NotNull(message = "currency type is required,Please Enter currency like USD,INR")
    private String currency;

    @DecimalMin(value = "1.00", message = "Price must be greater than or equal to 0.00")
    @Digits(integer = 10, fraction = 2, message = "Price must have a maximum of 10 integer digits and 2 decimal places")
    private Double amount;
}
