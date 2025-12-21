package com.rmt.railway_management_system.dto;

public class LoginResponseDTO {
    private Integer userId;
    private String message;
    private String username;
    private String fullname;
    private String email;
    private String phone;
    private String role;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(Integer userId, String message, String username, String fullname, String email,
            String phone,
            String role) {
        this.userId = userId;
        this.message = message;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String role() {
        return role;
    }

    public void setAdmin(String role) {
        this.role = role;
    }
}