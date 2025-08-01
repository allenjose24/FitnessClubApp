package com.fitness.FitnessClubApp.service;

import com.fitness.FitnessClubApp.model.PlatformAccount;
import com.fitness.FitnessClubApp.model.Role;
import com.fitness.FitnessClubApp.model.User;
import com.fitness.FitnessClubApp.repository.PlatformAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PlatformAccountService {

    private final PlatformAccountRepository platformAccountRepository;

    public PlatformAccount viewAccount(User admin) {
        if (admin.getRole() != Role.ADMIN) {
            throw new SecurityException("Access Denied: Not an Admin");
        }
        return platformAccountRepository.findSingleton();
    }

    // Optional: Initialize platform account (one-time setup)
    public PlatformAccount initializeAccount(BigDecimal initialAmount, User admin) {
        if (admin.getRole() != Role.ADMIN) {
            throw new SecurityException("Only admin can initialize account");
        }
        PlatformAccount account = new PlatformAccount();
        account.setBalance(initialAmount);
        return platformAccountRepository.save(account);
    }
}
