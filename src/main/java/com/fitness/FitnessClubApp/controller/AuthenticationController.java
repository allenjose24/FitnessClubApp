package com.fitness.FitnessClubApp.controller;

import com.fitness.FitnessClubApp.dtos.AuthenticationResponse;
import com.fitness.FitnessClubApp.dtos.LoginDTO;
import com.fitness.FitnessClubApp.dtos.RegistrationDTO;
import com.fitness.FitnessClubApp.model.Role;
import com.fitness.FitnessClubApp.service.AuthenticationService;
import com.fitness.FitnessClubApp.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid RegistrationDTO registrationDTO) throws Exception{

        try{
            AuthenticationResponse response = registrationService.register(registrationDTO, Role.CUSTOMER);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/register-trainers")
    public ResponseEntity<?> registerTrainers(@RequestBody RegistrationDTO registrationDTO) throws Exception{
        try{
            AuthenticationResponse response = registrationService.register(registrationDTO, Role.TRAINER);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            AuthenticationResponse response = authenticationService.loginWithEmail(loginDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }


}
