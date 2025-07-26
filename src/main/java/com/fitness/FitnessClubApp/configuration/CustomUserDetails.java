package com.fitness.FitnessClubApp.configuration;

import com.fitness.FitnessClubApp.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // --- THIS IS THE FIX FOR THE 403 FORBIDDEN ERROR ---
        // Spring Security's authorization filters require roles to be prefixed with "ROLE_".
        // This code explicitly creates a SimpleGrantedAuthority with the correct format,
        // for example, "ROLE_USER". Your original code was likely returning just "USER",
        // which is not recognized as a role, leading to the 403 error.
        if (user.getRole() == null) {
            return Collections.emptyList(); // Return an empty list if no role is assigned
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // --- THIS IS A CRITICAL SUPPORTING FIX ---
        // The JWT is created with the user's email as the "subject".
        // For token validation (userDetails.getUsername().equals(tokenSubject)) to work,
        // this method MUST return the same identifier used in the token.
        // Returning user.getUsername() here while using email in the token would cause validation to fail.
        return user.getMember().getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // A more robust check for active status.
        return user.getIsActive() != null && user.getIsActive().name().equals("ACTIVE");
    }
}
