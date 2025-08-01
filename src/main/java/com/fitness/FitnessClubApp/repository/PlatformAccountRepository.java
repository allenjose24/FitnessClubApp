package com.fitness.FitnessClubApp.repository;

import com.fitness.FitnessClubApp.model.PlatformAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PlatformAccountRepository extends JpaRepository<PlatformAccount,Long> {
    @Query("SELECT p FROM PlatformAccount p WHERE p.id = 1")
    PlatformAccount findSingleton();
}
