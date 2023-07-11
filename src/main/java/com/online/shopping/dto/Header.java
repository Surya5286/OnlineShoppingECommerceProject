package com.online.shopping.dto;

import com.online.shopping.config.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Generated
public class Header {
    private String message;
    private String code;
    private String traceId;
}
