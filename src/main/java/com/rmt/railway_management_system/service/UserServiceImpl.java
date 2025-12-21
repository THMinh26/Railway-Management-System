package com.rmt.railway_management_system.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rmt.railway_management_system.dto.LoginRequestDTO;
import com.rmt.railway_management_system.dto.LoginResponseDTO;
import com.rmt.railway_management_system.dto.RegisterRequestDTO;
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
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username"));
        System.err.println(loginRequest.getPassword());
        System.err.println(user.getPassword());

        if (!passwordService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid password");
        }

        return new LoginResponseDTO(
                "Login successful",
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole());
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
        user.setFullName(registerRequest.getFullname());
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
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
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