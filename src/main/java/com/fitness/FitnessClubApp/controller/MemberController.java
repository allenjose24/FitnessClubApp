package com.fitness.FitnessClubApp.controller;

import com.fitness.FitnessClubApp.model.Member;
import com.fitness.FitnessClubApp.service.CurrentUser;
import com.fitness.FitnessClubApp.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final CurrentUser currentUser;

    @GetMapping("/all")
    public ResponseEntity<?> getAllMembers() throws Exception {

        try{
            List<Member> memberList = memberService.viewAllMembers(currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body((Member) memberList);

        } catch (AccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception Message : " + e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMember(@PathVariable long id) throws Exception {
        try{
            Member member = memberService.viewMember(id,  currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.FOUND).body(member);
        }catch (AccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception Message : " + e);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getMemberByEmail(@PathVariable String email) throws Exception {
        try{
            Member member = memberService.viewByEmail(email,  currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body(member);
        }catch (AccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception Message : " + e);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMember(@PathVariable long id, @RequestBody Member member) throws Exception {
        try{
            memberService.updateMember(id, member, currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body("Member Updated Successfully");
        } catch (AccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception Message : " + e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable long id) throws Exception {
        try{
            memberService.deleteMember(id,  currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body("Member Deleted Successfully");
        } catch (AccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception Message : " + e);
        }
    }

    @GetMapping("/last-joined")
    public ResponseEntity<?> getLastJoinedMembers(@RequestParam int days) throws Exception {
        try{
            List<Member> memberList = memberService.lastJoined(days, currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body(memberList);
        }catch (AccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception Message : " + e);
        }
    }

    @GetMapping("/last-updated")
    public ResponseEntity<?> getLastupdatedMemebers(@RequestParam int days) throws Exception {
        try{
            List<Member> memberList = memberService.lastUpdated(days, currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body(memberList);
        }catch (AccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception Message : " + e);
        }
    }


}
