package com.fitness.FitnessClubApp.repository;

import com.fitness.FitnessClubApp.model.Plan;
import com.fitness.FitnessClubApp.model.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Integer> {
    Optional<Plan> findByPlanType(PlanType planType);

}
