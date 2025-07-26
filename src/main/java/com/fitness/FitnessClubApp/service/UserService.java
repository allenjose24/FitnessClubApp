package com.fitness.FitnessClubApp.service;

import com.fitness.FitnessClubApp.model.Role;
import com.fitness.FitnessClubApp.model.User;
import com.fitness.FitnessClubApp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.expression.AccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> viewAllUsers(User loggedIn) throws AccessException {
        if(loggedIn.getRole()!= Role.ADMIN){
            throw new AccessException("You are not allowed to view the User Details");
        }
        return userRepository.findAll();
    }

    public User viewUser(long id, User loggedIn) throws Exception {
        if(!Objects.equals(loggedIn.getUserId(), id) && loggedIn.getRole()!= Role.ADMIN){
            throw new AccessException("You are not allowed to view the User Details");
        }

        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("User does not exist");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new Exception("Not found"));
    }

    public User viewUserByUserName(String userName, User loggedIn) throws Exception {
        if(!Objects.equals(userName, loggedIn.getUsername()) && loggedIn.getRole()!= Role.ADMIN){
            throw new AccessException("You are not allowed to view the User Details");
        }
        if(!userRepository.existsByUsername(userName)){
            throw new EntityNotFoundException("User does not exist");
        }
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new Exception("Not found"));
    }

    public void deleteUser(long id, User loggedIn) throws Exception {
        if(!Objects.equals(loggedIn.getUserId(), id) && loggedIn.getRole()!= Role.ADMIN){
            throw new AccessException("You are not allowed to delete the User Details");
        }
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("User does not exist");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void changeUserPassword(long id, String oldPassword, String newPassword, User loggedIn) throws Exception {
        if(!Objects.equals(loggedIn.getUserId(), id) && loggedIn.getRole()!= Role.ADMIN){
            throw new AccessException("You are not allowed to change the User Details");
        }
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("User does not exist");
        }
        if(!passwordEncoder.matches(oldPassword, loggedIn.getPassword())){
            throw new AccessException("Old Password does not match");
        }

        loggedIn.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(loggedIn);
    }

    @Transactional
    public void updateUser(long id, User newUser, User loggedIn) throws Exception {
        User  oldUser = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User does not exist"));

        if(!Objects.equals(loggedIn.getUserId(), id)  &&  loggedIn.getRole()!= Role.ADMIN){
            throw new AccessException("You are not allowed to change the User Details");
        }

        if(newUser.getUsername()!= null){
            oldUser.setUsername(newUser.getUsername());
        }
        if(newUser.getMember()!= null){
            oldUser.setMember(newUser.getMember());
        }

        userRepository.save(oldUser);
    }

    public List<User> lastLogin(int days, User loggedIn) throws Exception {

        if(loggedIn.getRole()!= Role.ADMIN){
            throw new  AccessException("You are not allowed to view the User Details");
        }
        LocalDateTime date = LocalDateTime.now().minusDays(days);
        return userRepository.findByLastLoginAtAfterOrderByLastLoginAtDesc(date);
    }

    public List<User> lastJoined(int days, User loggedIn) throws Exception {

        if(loggedIn.getRole()!= Role.ADMIN){
            throw new  AccessException("You are not allowed to view the User Details");
        }
        LocalDateTime date = LocalDateTime.now().minusDays(days);
        return userRepository.findByJoinDateAfterOrderByJoinDateDesc(date);
    }

    public List<User> lastUpdated(int days, User loggedIn) throws Exception {

        if(loggedIn.getRole()!= Role.ADMIN){
            throw new  AccessException("You are not allowed to view the User Details");
        }
        LocalDateTime date = LocalDateTime.now().minusDays(days);
        return userRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(date);
    }

}
