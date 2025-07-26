package com.fitness.FitnessClubApp.service;

import com.fitness.FitnessClubApp.model.Member;
import com.fitness.FitnessClubApp.model.Role;
import com.fitness.FitnessClubApp.model.User;
import com.fitness.FitnessClubApp.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member viewMember(Long id, User loggedIn) throws Exception{
        if(!memberRepository.existsById(id)){
            throw new EntityNotFoundException("Member not found");
        }

        if(!Objects.equals(loggedIn.getMember().getMemberId(), id)  || loggedIn.getRole()!=Role.ADMIN){
            throw new AccessException("You are not allowed to view this member");
        }

        return memberRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }

    public List<Member> viewAllMembers(User loggedIn) throws Exception{
        if(loggedIn.getRole()!=Role.ADMIN){
            throw new AccessException("You are not allowed to view this member");
        }
        return memberRepository.findAll();
    }

    public Member viewByEmail(String email, User loggedIn) throws Exception{

        if(!Objects.equals(loggedIn.getMember().getEmail(), email) && loggedIn.getRole()!=Role.ADMIN){
            throw new AccessException("You are not allowed to view this member");
        }
        if(!memberRepository.existsByEmail(email)){
            throw new EntityNotFoundException("Member not found");
        }

        return memberRepository.findByEmail(email).
                orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }
    public void updateMember(Long id, Member newMember, User loggedIn) throws  AccessException{
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if(!Objects.equals(loggedIn.getMember().getMemberId(), id) && loggedIn.getRole()!=Role.ADMIN){
            throw new AccessException("You are not allowed to change this member");
        }

        if(newMember.getFirstName()!=null){
            member.setFirstName(newMember.getFirstName());
        }
        if(newMember.getLastName()!=null){
            member.setLastName(newMember.getLastName());
        }
        if(newMember.getEmail()!=null){
            member.setEmail(newMember.getEmail());
        }
        if(newMember.getAddress() !=null){
            member.setAddress(newMember.getAddress());
        }
        if(newMember.getPhone() !=null){
            member.setPhone(newMember.getPhone());
        }

        memberRepository.save(member);
    }

    public void deleteMember(Long id, User loggedIn) throws Exception{
        if(!memberRepository.existsById(id)){
            throw new EntityNotFoundException("Member not found");
        }
        if(!Objects.equals(loggedIn.getMember().getMemberId(), id) || loggedIn.getRole()!=Role.ADMIN){
            throw new AccessException("You are not allowed to delete this member");
        }
        memberRepository.deleteById(id);
    }



}
