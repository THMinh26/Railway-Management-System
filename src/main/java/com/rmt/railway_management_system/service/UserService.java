package com.rmt.railway_management_system.service;

import java.util.Map;

import com.rmt.railway_management_system.dto.LoginRequestDTO;
import com.rmt.railway_management_system.dto.LoginResponseDTO;
import com.rmt.railway_management_system.dto.RegisterRequestDTO;

public interface UserService {
    LoginResponseDTO login(LoginRequestDTO loginRequest);

    Map<String, String> register(RegisterRequestDTO registerRequest);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}