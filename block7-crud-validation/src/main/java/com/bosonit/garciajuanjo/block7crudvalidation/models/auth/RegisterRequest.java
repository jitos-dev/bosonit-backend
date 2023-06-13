package com.bosonit.garciajuanjo.block7crudvalidation.models.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotNull(message = "The field user cannot be null")
    @Size(max = 10, message = "The field user cannot have more than ten characters")
    @NotBlank(message = "The field user cannot be empty")
    private String user;

    @NotNull(message = "The field password cannot be null")
    @Size(min = 4, max = 10, message = "The field password cannot have less than four characters and more than ten")
    private String password;

    @NotNull(message = "The field name cannot be null")
    private String name;

    private String surname;

    @NotNull(message = "The field company email cannot be null")
    private String companyEmail;

    @NotNull(message = "The field personal email cannot be null")
    private String personalEmail;

    @NotNull(message = "The field city cannot be null")
    private String city;
}
