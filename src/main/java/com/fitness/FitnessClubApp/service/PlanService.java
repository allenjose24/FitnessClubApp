package com.fitness.FitnessClubApp.service;

import com.fitness.FitnessClubApp.model.Plan;
import com.fitness.FitnessClubApp.model.Role;
import com.fitness.FitnessClubApp.model.User;
import com.fitness.FitnessClubApp.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    public Plan createPlan(Plan plan, User admin) {
        if (admin.getRole() != Role.ADMIN) {
            throw new SecurityException("Only admin can create plans");
        }
        return planRepository.save(plan);
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

}

