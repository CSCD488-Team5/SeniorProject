package com.Team5.SeniorProject.controller;

import com.Team5.SeniorProject.service.UserService;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.model.PasswordChangeRequest;
import com.Team5.SeniorProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http: //localhost:5173")
public class AccountChangesController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request, Principal principal) {
        String username = principal.getName(); //gets username from JWT

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }


    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(Principal principal) {
        String username = principal.getName();
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok("Account deleted successfully");
    }




}
