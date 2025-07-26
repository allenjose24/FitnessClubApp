package com.fitness.FitnessClubApp.service;

import com.fitness.FitnessClubApp.configuration.CustomUserDetails;
import com.fitness.FitnessClubApp.model.User;
import com.fitness.FitnessClubApp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user  = userRepository.findByMemberEmail(identifier).
                orElseGet(() -> userRepository.findByUsername(identifier).
                        orElseThrow(() -> new UsernameNotFoundException("User not found")));

        return new CustomUserDetails(user);
    }

}
