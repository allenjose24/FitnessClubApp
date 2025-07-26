package com.fitness.FitnessClubApp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
