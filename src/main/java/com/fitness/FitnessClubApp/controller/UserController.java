package com.fitness.FitnessClubApp.controller;

import com.fitness.FitnessClubApp.model.User;
import com.fitness.FitnessClubApp.service.CurrentUser;
import com.fitness.FitnessClubApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CurrentUser  currentUser;


    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile() {
        User loggedIn = currentUser.getLoggedInUser();
        userService.loginTime(loggedIn.getMember().getEmail(), LocalDateTime.now());
        return ResponseEntity.ok(loggedIn);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() throws Exception {
        try{
            List<User> userList = userService.viewAllUsers(currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body(userList);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception Message : " + e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws Exception {
        try{
            User user = userService.viewUser(id, currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }catch (AccessException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Exception Message : " + e);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) throws Exception {
        try{
            User user = userService.viewUserByUserName(username, currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }catch (AccessException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Exception Message : " + e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws Exception {
        try{
            userService.deleteUser(id, currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body("User has been deleted");
        }catch (AccessException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Exception Message : " + e);
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) throws Exception {
        try{
            userService.changeUserPassword(id, oldPassword, newPassword, currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body("User has been changed");
        } catch (AccessException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Exception Message : " + e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User newUser
    ) throws Exception {
        try{
            userService.updateUser(id, newUser, currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body("User has been changed");
        }catch (AccessException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Exception Message : " + e);
        }
    }

    @GetMapping("/last-login")
    public ResponseEntity<?> getLastLoginUsers(@RequestParam int days) throws Exception {
        try{
            List<User> userList = userService.lastLogin(days, currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (AccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception Message : " + e);
        }
    }

    @GetMapping("/last-joined")
    public ResponseEntity<?> getLastJoinedUsers(@RequestParam int days) throws Exception {
        try{
            List<User> userList = userService.lastJoined(days, currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (AccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception Message : " + e);
        }
    }

    @GetMapping("/last-updated")
    public ResponseEntity<?> getLastUpdatedUsers(@RequestParam int days) throws Exception {
        try{
            List<User> userList = userService.lastUpdated(days, currentUser.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body(userList);
        }catch (AccessException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception Message : " + e);
        }
    }

}
