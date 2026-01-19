package com.example.travelgenix.userservice.controller;

import com.example.travelgenix.userservice.dto.RegisterRequestDto;
import com.example.travelgenix.userservice.dto.UserResponseDto;
import com.example.travelgenix.userservice.dto.UserUpdateDto;
import com.example.travelgenix.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public String registerUser(@RequestBody RegisterRequestDto req){
        service.registerUser(req);
        return "Admin created";
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id,@RequestBody UserUpdateDto req){
        service.updateUser(id, req);
    }

    @DeleteMapping
    public void deleteUser(@RequestBody RegisterRequestDto req){
        service.deleteUser(req);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(service.getUserById(id));
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleStatus(@PathVariable Long id) {
        service.toggleUserStatus(id);
        return ResponseEntity.noContent().build();
    }
}
