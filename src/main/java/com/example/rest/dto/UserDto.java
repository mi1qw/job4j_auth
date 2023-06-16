package com.example.rest.dto;

import com.example.rest.Exception.NotExistingAccount;
import com.example.rest.Exception.Operation;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@AllArgsConstructor
@Data
@Builder
public class UserDto {
    @Email(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)"
            + "+[a-zA-Z]{2,6}$", message = "Invalid email address",
            groups = Operation.OnCreate.class)
    @NotExistingAccount(groups = Operation.OnCreate.class)
    private String login;

    @Length(min = 6, message = "Password must be at least 6 characters",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String password;

    @Length(min = 1, message = "Fields must be not empty",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String country;

    @Length(min = 1, message = "Fields must be not empty",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String city;

    @Length(min = 1, message = "Fields must be not empty",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String street;

    @Length(min = 1, message = "Fields must be not empty",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String house;
}
