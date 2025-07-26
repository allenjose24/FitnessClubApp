package com.fitness.FitnessClubApp.controller;

import com.fitness.FitnessClubApp.configuration.CustomUserDetails;
import com.fitness.FitnessClubApp.model.User;
import com.fitness.FitnessClubApp.repository.UserRepository;
import com.fitness.FitnessClubApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository,  UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile() {
        User loggedIn = getLoggedInUser();
        userRepository.updateLastLoginAt(loggedIn.getMember().getEmail(), LocalDateTime.now());
        return ResponseEntity.ok(loggedIn);
    }


    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() throws Exception {
        return ResponseEntity.ok(userService.viewAllUsers(getLoggedInUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.viewUser(id, getLoggedInUser()));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(userService.viewUserByUserName(username, getLoggedInUser()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws Exception {
        userService.deleteUser(id, getLoggedInUser());
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) throws Exception {
        userService.changeUserPassword(id, oldPassword, newPassword, getLoggedInUser());
        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody User newUser
    ) throws Exception {
        userService.updateUser(id, newUser, getLoggedInUser());
        return ResponseEntity.ok("User updated successfully");
    }

    @GetMapping("/last-login")
    public ResponseEntity<List<User>> getLastLoginUsers(@RequestParam int days) throws Exception {
        return ResponseEntity.ok(userService.lastLogin(days, getLoggedInUser()));
    }

    @GetMapping("/last-joined")
    public ResponseEntity<List<User>> getLastJoinedUsers(@RequestParam int days) throws Exception {
        return ResponseEntity.ok(userService.lastJoined(days, getLoggedInUser()));
    }

    @GetMapping("/last-updated")
    public ResponseEntity<List<User>> getLastUpdatedUsers(@RequestParam int days) throws Exception {
        return ResponseEntity.ok(userService.lastUpdated(days, getLoggedInUser()));
    }

}
