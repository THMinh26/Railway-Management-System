package com.rmt.railway_management_system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rmt.railway_management_system.dto.LoginRequestDTO;
import com.rmt.railway_management_system.dto.LoginResponseDTO;
import com.rmt.railway_management_system.dto.RegisterRequestDTO;
import com.rmt.railway_management_system.dto.UserResponseDTO;
import com.rmt.railway_management_system.entity.User;
import com.rmt.railway_management_system.exception.DuplicateResourceException;
import com.rmt.railway_management_system.exception.ResourceNotFoundException;
import com.rmt.railway_management_system.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        User user = userRepository.findById(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password"));

        if (!passwordService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid username or password");
        }

        return new LoginResponseDTO(
                "Login successful",
                user.getUsername(),
                user.getFullname(),
                user.getEmail(),
                user.getPhone(),
                user.isAdmin());
    }

    @Override
    public Map<String, String> register(RegisterRequestDTO registerRequest) {
        if (existsByUsername(registerRequest.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (existsByPhone(registerRequest.getPhone())) {
            throw new DuplicateResourceException("Phone number already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setFullname(registerRequest.getFullname());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setPassword(passwordService.hashPassword(registerRequest.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("username", user.getUsername());
        return response;
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return new UserResponseDTO(
                user.getUsername(),
                user.getFullname(),
                user.getEmail(),
                user.getPhone());
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(
                        user.getUsername(),
                        user.getFullname(),
                        user.getEmail(),
                        user.getPhone()))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(User user) {
        if (!userRepository.existsById(user.getUsername())) {
            throw new ResourceNotFoundException("User not found with username: " + user.getUsername());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordService.hashPassword(user.getPassword()));
        }
        User updatedUser = userRepository.save(user);
        return new UserResponseDTO(
                updatedUser.getUsername(),
                updatedUser.getFullname(),
                updatedUser.getEmail(),
                updatedUser.getPhone());
    }

    @Override
    public void deleteUser(String username) {
        if (!userRepository.existsById(username)) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        userRepository.deleteById(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsById(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }
}