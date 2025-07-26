package com.fitness.FitnessClubApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "members")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @NotBlank(message = "First Name is Required")
    @Size(max=50, message = "First name must be at most 50 characters")
    private String firstName;

    @NotBlank(message = "Last Name is Required")
    @Size(max=50, message = "Last name must be at most 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be at most 100 characters")
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian phone number")
    @Size(max = 15, message = "Phone number cannot take more than 15 characters")
    private String phone;

    @Size(max=255, message = "Address can only hold maximum of 255 characters")
    private String address;

    @NotNull(message = "Join date is required")
    @CreatedDate
    private LocalDateTime joinDate;

    @NotNull(message = "Updated date is required")
    @LastModifiedDate
    private LocalDateTime updatedAt;

}