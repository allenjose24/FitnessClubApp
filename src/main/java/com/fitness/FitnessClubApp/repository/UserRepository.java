package com.fitness.FitnessClubApp.repository;

import com.fitness.FitnessClubApp.model.User;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByMemberEmail(String email);
    boolean existsByUsername(String username);

    List<User> findByLastLoginAtAfterOrderByLastLoginAtDesc(LocalDateTime since);
    List<User> findByJoinDateAfterOrderByJoinDateDesc(LocalDateTime since);
    List<User> findByUpdatedAtAfterOrderByUpdatedAtDesc(LocalDateTime since);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLoginAt = :lastLoginAt WHERE u.member.email = :email")
    void updateLastLoginAt(@Param("email") String email, @Param("lastLoginAt") LocalDateTime lastLoginAt);

}
