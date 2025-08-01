package com.fitness.FitnessClubApp.service;

import com.fitness.FitnessClubApp.model.*;
import com.fitness.FitnessClubApp.repository.MembershipRepository;
import com.fitness.FitnessClubApp.repository.PlanRepository;
import com.fitness.FitnessClubApp.repository.PlatformAccountRepository;
import com.fitness.FitnessClubApp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final PlatformAccountRepository platformAccountRepository;
    private final MembershipRepository membershipRepository;
    private final PlanRepository planRepository;

    public Transaction processMembershipChange(Member member, PlanType selectedPlanType) throws Exception{
        Membership membership = (Membership) membershipRepository.findByMember_MemberId(member.getMemberId())
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        Plan currentPlan = membership.getPlan();
        Plan selectedPlan = planRepository.findByPlanType(selectedPlanType)
                .orElseThrow(() -> new RuntimeException("Selected plan not found"));

        // Decide transaction type
        TransactionType transactionType;
        if (selectedPlanType.ordinal() > currentPlan.getPlanType().ordinal()) {
            transactionType = TransactionType.UPGRADE;
        } else if (selectedPlanType == currentPlan.getPlanType()) {
            transactionType = TransactionType.RENEWAL;
        } else {
            throw new IllegalArgumentException("Cannot downgrade to a lower plan");
        }

        // Update membership duration (for both upgrade or renewal)
        membership.setPlan(selectedPlan);
        membership.setStartDate(LocalDateTime.now());
        membership.setEndDate(LocalDateTime.now().plusDays(selectedPlan.getDurationInDays()));
        membershipRepository.save(membership);

        // Credit platform account
        PlatformAccount account = platformAccountRepository.findSingleton();
        account.setBalance(account.getBalance().add(selectedPlan.getPrice()));
        platformAccountRepository.save(account);

        // Save transaction
        Transaction transaction = new Transaction();
        transaction.setMember(member);
        transaction.setPlan(selectedPlan);
        transaction.setType(transactionType);
        transaction.setAmount(selectedPlan.getPrice());
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

}
