package com.example.travelgenix.userservice.service;

import com.example.travelgenix.userservice.client.AdminAuthClient;
import com.example.travelgenix.userservice.dto.RegisterRequestDto;
import com.example.travelgenix.userservice.dto.UserResponseDto;
import com.example.travelgenix.userservice.dto.UserUpdateDto;
import com.example.travelgenix.userservice.model.User;
import com.example.travelgenix.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private EmailService emailService;

    private final ModelMapper modelMapper;

    public UserService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    private AdminAuthClient adminAuthClient;
    public void registerUser(RegisterRequestDto dto) {

        if (repo.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        adminAuthClient.createCredentials(
                dto.getFirstName(),
                dto.getEmail(),
                dto.getPassword(),
                true

        );

        User user = modelMapper.map(dto, User.class);
        user.setRole("Admin");
        user.setActive(true);
        repo.save(user);


        //send email
        emailService.sendInvite(user.getEmail(),dto.getPassword());

    }

    public void updateUser(Long userId, UserUpdateDto dto) {
        User user = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String oldEmail = user.getEmail();

        adminAuthClient.updateCredentials(
                dto.getFirstName(),
                oldEmail,
                dto.getEmail()
        );
        modelMapper.map(dto, user);

        User updatedUser = repo.save(user);
        modelMapper.map(updatedUser, UserResponseDto.class);
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> users = repo.findAll();

        return users.stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();
    }


    public void deleteUser(RegisterRequestDto dto) {

        User user = repo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        long adminCount = repo.count();

        if (adminCount <= 1) {
            throw new RuntimeException("Cannot delete the last admin account");
        }

        adminAuthClient.deleteCredentials(dto.getEmail());
        user.setRole("User");
        user.setActive(false);
        repo.delete(user);

    }

    public UserResponseDto getUserById(Long userId) {
        User user=repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
        return dto;
    }


    @Transactional
    public void toggleUserStatus(Long userId) {
        User user = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean newActive = !user.isActive();
        user.setActive(newActive);

        if ("Admin".equalsIgnoreCase(user.getRole())) {
            adminAuthClient.updateStatus(user.getEmail(), newActive);
        }
        repo.save(user);
    }

}
