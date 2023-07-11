package com.online.shopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.online.shopping.config.Generated;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.online.shopping.util.OnlineShoppingAppConstants.USERINFO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "UserInfo Entity class")
@Document(collection = USERINFO)
@Generated
public class UserInfo {

    @Schema(description = "user id")
    @Id
    @JsonIgnore
    private String id;

    @Schema(description = "name of user")
    @Field(name = "name")
    @NotEmpty(message = "name should not be blank or empty")
    @Indexed(name = "Index_user_name", expireAfterSeconds = 864000)
    private String name;

    @Schema(description = "email of user")
    @Email
    @Field(name = "email")
    private String email;

    @Schema(description = "password of user")
    @Field(name = "password")
    @NotEmpty(message = "password must not be null or empty")
    private String password;

    @Schema(description = "roles of user.")
    private String roles;

}
