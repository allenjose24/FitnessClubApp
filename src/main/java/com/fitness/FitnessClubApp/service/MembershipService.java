package com.fitness.FitnessClubApp.service;

import com.fitness.FitnessClubApp.model.*;
import com.fitness.FitnessClubApp.repository.MembershipRepository;
import com.fitness.FitnessClubApp.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final PlanRepository planRepository;
    private final MembershipRepository membershipRepository;
    private final CurrentUser currentUser;

    public void assignFreeMembership(Member member) {
        // Check if already has a membership
        if (membershipRepository.existsByMember_MemberId(member.getMemberId())) {
            throw new IllegalStateException("Member already has a membership");
        }

        // Fetch the FREE plan
        Plan freePlan = planRepository.findByPlanType(PlanType.FREE)
                .orElseThrow(() -> new RuntimeException("FREE plan not found in DB"));

        // Create membership
        Membership membership = new Membership();
        membership.setMember(member);
        membership.setPlan(freePlan);
        membership.setStartDate(LocalDateTime.now());
        membership.setEndDate(LocalDateTime.now().plusDays(freePlan.getDurationInDays()));

        membershipRepository.save(membership);
    }

    public Membership viewMembership(long userId, User user) throws Exception {
        if(!Objects.equals(userId, user.getUserId()) && user.getRole()!=Role.ADMIN){
            throw new AccessDeniedException("You are not allowed to view others details");
        }
        return (Membership) membershipRepository.findByMember_MemberId(user.getMember().getMemberId())
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public Membership viewMembership(User user) throws Exception {
        return (Membership) membershipRepository.findByMember_MemberId(user.getMember().getMemberId())
                .orElseThrow(() -> new AccessDeniedException("You are not allowed to view the detail"));
    }

    public void deleteMembership(long userId, User user) throws Exception {
        if(!Objects.equals(userId, user.getUserId()) &&  user.getRole()!=Role.ADMIN){
            throw new AccessDeniedException("You are not allowed to delete others details");
        }
        membershipRepository.deleteByMember_MemberId(user.getMember().getMemberId());
    }

    public void updateMembership(Membership membership, User user) throws Exception {
        if(membershipRepository.existsByMember_MemberId(membership.getMember().getMemberId())) {}
    }









}
