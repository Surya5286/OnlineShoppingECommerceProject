package com.online.shopping.exception;

import com.online.shopping.config.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@Generated
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProductNotAvailableException extends RuntimeException{
    public ProductNotAvailableException(String message){
        super(message);
    }

}