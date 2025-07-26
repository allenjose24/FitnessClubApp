package com.fitness.FitnessClubApp.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CUSTOMER,
    TRAINER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
