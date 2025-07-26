package com.fitness.FitnessClubApp.service;

import com.fitness.FitnessClubApp.dtos.AuthenticationResponse;
import com.fitness.FitnessClubApp.dtos.RegistrationDTO;
import com.fitness.FitnessClubApp.model.Member;
import com.fitness.FitnessClubApp.model.Role;
import com.fitness.FitnessClubApp.model.Status;
import com.fitness.FitnessClubApp.model.User;
import com.fitness.FitnessClubApp.repository.MemberRepository;
import com.fitness.FitnessClubApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;



    public AuthenticationResponse register(RegistrationDTO registrationDTO, Role role) throws RuntimeException {
        if(memberRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if(userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Member member = new Member();
        member.setFirstName(registrationDTO.getFirstName());
        member.setLastName(registrationDTO.getLastName());
        member.setEmail(registrationDTO.getEmail());
        member.setPhone(registrationDTO.getPhone());
        member.setAddress(registrationDTO.getAddress());

        memberRepository.save(member);

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(role);
        user.setIsActive(Status.ACTIVE);
        user.setMember(member);

        userRepository.save(user);

        String token = jwtService.generateToken(user.getMember().getEmail(), role);
        return new AuthenticationResponse(
                token,
                user.getUsername(),
                user.getMember().getEmail(),
                user.getRole()
        );
    }
}
