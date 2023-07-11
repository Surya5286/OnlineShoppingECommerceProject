package com.online.shopping.advice;

import com.online.shopping.config.Generated;
import com.online.shopping.exception.CustomException;
import com.online.shopping.exception.ProductNotAvailableException;
import com.online.shopping.exception.ResourceNotFoundException;
import com.online.shopping.exception.UserAlreadyExistException;
import com.online.shopping.dto.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static com.online.shopping.util.OnlineShoppingAppConstants.EXCEPTION_OCCURRED_PROCESSING;
import static com.online.shopping.util.OnlineShoppingAppConstants.EXCEPTION_RECEIVED;
import static com.online.shopping.util.OnlineShoppingAppUtility.setDataResponse;

@ControllerAdvice
@Slf4j
@Generated
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = CustomException.class)
    public final ResponseEntity<DataResponse<String>> handleCustomException(CustomException e) {
        log.error("Inside the handleCustomException() method.");
        DataResponse<String> errResponse = setDataResponse(EXCEPTION_RECEIVED + e.getClass(), HttpStatus.BAD_REQUEST, MDC.get("traceId"), EXCEPTION_OCCURRED_PROCESSING);
        List<Error> errorList = new ArrayList<>();
        errorList.add(new Error(e.getMessage()));
        errResponse.setErrors(errorList);

        log.error("Received CustomException: ", e.getClass());
        return new ResponseEntity<DataResponse<String>>(errResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserAlreadyExistException.class)
    public final ResponseEntity<DataResponse<String>> handleUserAlreadyExistException(UserAlreadyExistException e) {
        log.error("Inside the handleUserAlreadyExistException() method.");
        DataResponse<String> errResponse = setDataResponse(EXCEPTION_RECEIVED + e.getClass(), HttpStatus.BAD_REQUEST, MDC.get("traceId"), EXCEPTION_OCCURRED_PROCESSING);
        List<Error> errorList = new ArrayList<>();
        errorList.add(new Error(e.getMessage()));
        errResponse.setErrors(errorList);

        log.error("Received UserAlreadyExistException: ", e.getClass());
        return new ResponseEntity<DataResponse<String>>(errResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public final ResponseEntity<DataResponse<String>> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error("Inside the handleResourceNotFoundException() method.");
        DataResponse<String> errResponse = setDataResponse(EXCEPTION_RECEIVED + e.getClass(), HttpStatus.BAD_REQUEST, MDC.get("traceId"), EXCEPTION_OCCURRED_PROCESSING);
        List<Error> errorList = new ArrayList<>();
        errorList.add(new Error(e.getMessage()));
        errResponse.setErrors(errorList);

        log.error("Received ResourceNotFoundException: ", e.getClass());
        return new ResponseEntity<DataResponse<String>>(errResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ProductNotAvailableException.class})
    public final ResponseEntity<DataResponse<String>> handleProductNotAvailableException(ProductNotAvailableException e) {
        log.error("Inside the handleProductNotAvailableException() method.");
        DataResponse<String> errResponse = setDataResponse(EXCEPTION_RECEIVED + e.getClass(), HttpStatus.BAD_REQUEST, MDC.get("traceId"), EXCEPTION_OCCURRED_PROCESSING);
        List<Error> errorList = new ArrayList<>();
        errorList.add(new Error(e.getMessage()));
        errResponse.setErrors(errorList);

        log.error("Received ProductNotAvailableException: ", e.getClass());
        return new ResponseEntity<DataResponse<String>>(errResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public final ResponseEntity<DataResponse<String>> handleOtherException(Exception e) {
        log.error("Inside the handleOtherException() method.");
        DataResponse<String> errResponse = setDataResponse(EXCEPTION_RECEIVED + e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR, MDC.get("traceId"), EXCEPTION_OCCURRED_PROCESSING);
        List<Error> errorList = new ArrayList<>();
        errorList.add(new Error(e.getMessage()));
        errResponse.setErrors(errorList);

        log.error("Received Exception: ", e.getClass());
        return new ResponseEntity<DataResponse<String>>(errResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
