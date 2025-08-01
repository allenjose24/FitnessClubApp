package com.fitness.FitnessClubApp.repository;

import com.fitness.FitnessClubApp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
