package com.fitness.FitnessClubApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "platform_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 1L; // Singleton row

    private BigDecimal balance;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

