package com.online.shopping.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Inventory Entity class")
public class Inventory {

    private int total;
    private int available;
    private int reserved;
}
