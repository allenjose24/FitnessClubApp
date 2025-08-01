package com.fitness.FitnessClubApp.repository;

import com.fitness.FitnessClubApp.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    boolean existsByMember_MemberId(Long memberId);

    Optional<Object> findByMember_MemberId(Long memberId);

    void deleteByMember_MemberId(Long memberId);

}
