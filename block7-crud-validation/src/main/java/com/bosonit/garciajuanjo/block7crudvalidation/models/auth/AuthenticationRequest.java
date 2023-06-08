package com.bosonit.garciajuanjo.block7crudvalidation.models.auth;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    @NotNull(message = "The field user cannot be null")
    @Max(value = 10, message = "The field user cannot have more than ten characters")
    @NotBlank(message = "The field user cannot be empty")
    private String user;

    @NotNull(message = "The field password cannot be null")
    @Min(value = 4, message = "The field user cannot have less than four characters")
    @Max(value = 10, message = "The field user cannot have more than ten characters")
    private String password;
}
