package com.rmt.railway_management_system.service;

import java.util.List;
import java.util.Map;

import com.rmt.railway_management_system.dto.LoginRequestDTO;
import com.rmt.railway_management_system.dto.LoginResponseDTO;
import com.rmt.railway_management_system.dto.RegisterRequestDTO;
import com.rmt.railway_management_system.dto.UserResponseDTO;
import com.rmt.railway_management_system.entity.User;

public interface UserService {
    LoginResponseDTO login(LoginRequestDTO loginRequest);

    Map<String, String> register(RegisterRequestDTO registerRequest);

    UserResponseDTO getUserByUsername(String username);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(User user);

    void deleteUser(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}