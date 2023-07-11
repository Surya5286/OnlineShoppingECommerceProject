package com.online.shopping.controller;

import com.online.shopping.entity.UserInfo;
import com.online.shopping.exception.UserAlreadyExistException;
import com.online.shopping.dto.DataResponse;
import com.online.shopping.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.online.shopping.util.OnlineShoppingAppConstants.USER_ALREADY_EXIST;
import static com.online.shopping.util.OnlineShoppingAppConstants.USER_SAVED_SUCCESSFULLY;
import static com.online.shopping.util.OnlineShoppingAppUtility.setDataResponse;

@RestController
@RequestMapping("/v1/api")
@Tag(name = "User Controller", description = "User Registration Documentation Details")
@Slf4j
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Saved User Successfully in Database  ",
                    content = {@Content(schema = @Schema(implementation = DataResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", description = "User Not Found Exception ",
                    content = {@Content(schema = @Schema(implementation = DataResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error ",
                    content = {@Content(schema = @Schema(implementation = DataResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @Operation(summary = "Register User in DB", description = "Save a User object by specifying User details. The response is User in string format.")
    @PostMapping("/users")
    public ResponseEntity<DataResponse<String>> addNewUser(@RequestBody UserInfo userInfo) {
        log.info("Entering in User Controller getUsers() method");
        Optional<String> responseString = userService.saveUser(userInfo);
        if (responseString.isEmpty()) {
            throw new UserAlreadyExistException(USER_ALREADY_EXIST);
        }
        DataResponse<String> response = setDataResponse(responseString.get(), HttpStatus.CREATED, MDC.get("traceId"), USER_SAVED_SUCCESSFULLY);
        log.info("Exiting from User Controller getUsers() method");
        return new ResponseEntity<DataResponse<String>>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get All Users in DB", description = "save a Category object by specifying Category name. The response is Category object with id, name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Obtained the Created Category List", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request/ Resource already Existed ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error ", content = {
                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")})})
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        log.info("Entering in User Controller getUsers() method");
        List<UserInfo> userInfoList = userService.getAllUsers();
        log.info("Exiting from User Controller getUsers() method");
        return ResponseEntity.ok(userInfoList);
    }

}
