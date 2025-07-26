package com.fitness.FitnessClubApp.repository;

import com.fitness.FitnessClubApp.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

    List<Member> findAllByJoinDateAfterOrderByJoinDateDesc(LocalDateTime since);
    List<Member> findAllByUpdatedAtAfterOrderByUpdatedAtDesc(LocalDateTime since);


}
