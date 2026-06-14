package com.example.training.dto;

public class LoginResponse {

    private String token;
    private String email;
    private String username;

    public LoginResponse(
            String token,
            String email,
            String username
    ) {
        this.token = token;
        this.email = email;
        this.username = username;
    }

    // Getter
    public String getToken() { return token; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
}
