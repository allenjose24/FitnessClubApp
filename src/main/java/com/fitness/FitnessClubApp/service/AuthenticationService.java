package com.fitness.FitnessClubApp.service;

import com.fitness.FitnessClubApp.dtos.AuthenticationResponse;
import com.fitness.FitnessClubApp.dtos.LoginDTO;
import com.fitness.FitnessClubApp.model.User;
import com.fitness.FitnessClubApp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthenticationResponse loginWithEmail(LoginDTO loginDTO) throws Exception {
        User user = userRepository.findByMemberEmail(loginDTO.getEmail())
                .orElseThrow(() -> new Exception("User with email not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new Exception("Invalid password");
        }

        userRepository.updateLastLoginAt(loginDTO.getEmail(), LocalDateTime.now());

        String token = jwtService.generateToken(user.getMember().getEmail(), user.getRole());

        return new AuthenticationResponse(token,
                user.getMember().getEmail(),
                user.getUsername(),
                user.getRole()
        );
    }
}

