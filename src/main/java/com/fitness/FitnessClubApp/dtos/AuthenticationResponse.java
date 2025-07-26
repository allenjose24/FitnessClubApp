package com.fitness.FitnessClubApp.dtos;

import com.fitness.FitnessClubApp.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String username;
    private String email;
    private Role role;
}
